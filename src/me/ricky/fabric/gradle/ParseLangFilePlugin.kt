package me.ricky.fabric.gradle

import me.ricky.fabric.gradle.tasks.ParseLangFile
import org.gradle.api.Plugin
import org.gradle.api.Project

class ParseLangFilePlugin : Plugin<Project> {
  override fun apply(project: Project) {
    project.tasks.register("parseLangFile", ParseLangFile::class.java)
  }
}