import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
  `java-gradle-plugin`
  kotlin("jvm") version "1.3.41"
  id("maven")
}

gradlePlugin {
  plugins {
    create("parseLangFile") {
      id = "me.ricky.fabric.gradle.ParseLangFile"
      implementationClass = "me.ricky.fabric.gradle.ParseLangFilePlugin"
    }
  }
}

repositories {
  mavenCentral()
}

dependencies {
  implementation(kotlin("stdlib-jdk8"))
  implementation(gradleApi())
  implementation(localGroovy())

  compile("com.google.code.gson:gson:2.8.5")
}

kotlin.sourceSets["main"].kotlin.srcDirs("src/")
sourceSets["main"].resources.srcDirs("resources/")

tasks.withType<KotlinCompile> {
  kotlinOptions.jvmTarget = "1.8"
}

tasks {
  val sourcesJar by creating(Jar::class) {
    archiveClassifier.set("sources")

    from(sourceSets["main"].allSource)
    dependsOn(JavaPlugin.CLASSES_TASK_NAME)
  }

  val javadocJar by creating(Jar::class) {
    archiveClassifier.set("javadoc")

    from(project.tasks["javadoc"])
    dependsOn(JavaPlugin.JAVADOC_TASK_NAME)
  }

  artifacts {
    add("archives", sourcesJar)
    add("archives", javadocJar)
  }
}
