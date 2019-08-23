# ParseLangFile
This is still in early development

##### build.gradle.kts
```kotlin
plugins {
  id("parseLangFile")
}
```

```kotlin
pluginManagement {
  repositories {
    maven("https://jitpack.io")
    gradlePluginPortal()
  }

  resolutionStrategy {
    eachPlugin {
      if (requested.id.id == "parseLangFile") {
        val version = requested.version ?: "-SNAPSHOT"
        useModule("com.github.Ricky12Awesome:ParseLangFile:$version")
      }
    }
  }
}
```