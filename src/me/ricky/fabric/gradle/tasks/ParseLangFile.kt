package me.ricky.fabric.gradle.tasks

import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.google.gson.JsonObject
import me.ricky.fabric.gradle.*
import org.gradle.api.DefaultTask
import org.gradle.api.tasks.TaskAction
import java.io.File

open class ParseLangFile : DefaultTask() {
  var gson: Gson = GsonBuilder().setPrettyPrinting().create()
  var inFile: File? = null
  var outFile: File? = null
  var parsers: MutableMap<ParserPhase, List<ParserData>> = mutableMapOf(
    ParserPhase.ID to listOf(itemParserData())
  )

  @TaskAction
  fun convert() {
    if (inFile == null || outFile == null) return

    val inJson = gson.fromJson(inFile!!.readText(), JsonObject::class.java)
    val outJson = JsonObject()

    outJson.addElement(inJson, parsers)

    outFile!!.writeText(gson.toJson(outJson))
  }
}