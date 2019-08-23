package com.github.ricky12awesome.parselangfile.gradle

import com.google.gson.JsonArray
import com.google.gson.JsonElement
import com.google.gson.JsonObject
import com.google.gson.JsonPrimitive

typealias ParserPredicate = ParserPredicateData.() -> Boolean
typealias Parser = JsonObject.(path: String, inJson: JsonObject) -> Unit

enum class ParserPhase { MOD_ID, GROUP, ID }

data class ParserPredicateData(
    val modid: String,
    val group: String,
    val id: String
)

data class ParserData(
  val predicate: ParserPredicate,
  val parser: Parser
)

fun itemParserData(): ParserData =
  ParserData({ group == "item" }) { path, inJson ->
    val name = inJson["name"].asJsonPrimitive.asString
    val tooltip = inJson["tooltip"]

    addProperty(path, name)

    if (tooltip != null) {
      addElement("$path.tooltip", tooltip)
    }
  }

fun JsonObject.addElement(path: String, inJson: JsonElement): Unit = when (inJson) {
  is JsonArray -> addProperty(path, inJson.joinToString("\n") { it.asString })
  is JsonPrimitive -> addProperty(path, inJson.asString)
  is JsonObject -> {
    inJson.entrySet().forEach { (key, value) ->
      addElement("$path.$key", value)
    }
  }
  else -> Unit
}

fun JsonObject.addElement(
    inJson: JsonElement,
    listeners: Map<ParserPhase, List<ParserData>> = mapOf()
) {
  inJson.asJsonObject.entrySet().forEach modid@{ (modid, groups) ->
    var path = modid
    if (groups !is JsonObject) return@modid addElement(path, groups)

    listeners[ParserPhase.MOD_ID]?.forEach { (predicate, parser) ->
      if (predicate(ParserPredicateData(modid, "", ""))) {
        parser(path, groups)
        return@modid
      }
    }

    groups.asJsonObject.entrySet().forEach group@{ (group, ids) ->
      path = "$group.$modid"
      if (ids !is JsonObject) return@group addElement(path, ids)

      listeners[ParserPhase.GROUP]?.forEach { (predicate, parser) ->
        if (predicate(ParserPredicateData(modid, group, ""))) {
          parser(path, ids)
          return@group
        }
      }

      ids.asJsonObject.entrySet().forEach id@{ (id, element) ->
        path = "$group.$modid.$id"
        if (element !is JsonObject) return@id addElement(path, element)

        listeners[ParserPhase.ID]?.forEach { (predicate, parser) ->
          if (predicate(ParserPredicateData(modid, group, id))) {
            parser(path, element)
            return@id
          }
        }

        addElement(path, element)
      }
    }
  }
}