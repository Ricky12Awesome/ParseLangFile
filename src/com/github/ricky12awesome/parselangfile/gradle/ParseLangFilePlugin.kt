package com.github.ricky12awesome.parselangfile.gradle

import com.github.ricky12awesome.parselangfile.gradle.tasks.ParseLangFile
import org.gradle.api.Plugin
import org.gradle.api.Project

class ParseLangFilePlugin : Plugin<Project> {
  override fun apply(project: Project) {
    project.tasks.register("parseLangFile", ParseLangFile::class.java)
  }
}