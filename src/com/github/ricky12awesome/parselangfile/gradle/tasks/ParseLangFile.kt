package com.github.ricky12awesome.parselangfile.gradle.tasks

import com.github.ricky12awesome.parselangfile.gradle.ParserData
import com.github.ricky12awesome.parselangfile.gradle.ParserPhase
import com.github.ricky12awesome.parselangfile.gradle.addElement
import com.github.ricky12awesome.parselangfile.gradle.itemParserData
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.google.gson.JsonObject
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
    if (inFile == null) return
    if (outFile == null) return

    val inJson = gson.fromJson(inFile!!.readText(), JsonObject::class.java)
    val outJson = JsonObject()

    outJson.addElement(inJson, parsers)

    val parent = outFile!!.parentFile
    if (parent?.exists() == false) {
      parent.mkdirs()
    }

    outFile!!.writeText(gson.toJson(outJson))
  }
}