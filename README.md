# ParseLangFile
This is still in early development

##### build.gradle.kts
```kotlin
plugins {
  id("parseLangFile") version "1.1"
}
```
##### settings.gradle.kts
```kotlin
pluginManagement {
  repositories {
    maven("https://jitpack.io")
    gradlePluginPortal()
  }

  resolutionStrategy {
    eachPlugin {
      if (requested.id.id == "parseLangFile") {
        val versions = requested.version!!.split(":")
        val moduleVersion = versions.getOrElse(1) { "-SNAPSHOT" }
        useModule("com.github.Ricky12Awesome:ParseLangFile:$moduleVersion")
        useVersion(versions[0])
      }
    }
  }
}
```

# What it does
Converts a more readable json into a minecraft-lang file friendly format
##### Input
```json
{
  "modid": {
    "group": {
      "id": "value"
    }
  },
  "my_mod_id": {
    "item": {
      "my_item_1_id": "My Item 1",
      "my_item_2_id": {
        "name": "My Item 2",
        "tooltip": [
          "Line 1",
          "Line 2"
        ]
      }
    }
  }
}
```
##### Output
```json
{
  "group.modid.id": "value",
  "item.my_mod_id.my_item_1_id": "My Item 1",
  "item.my_mod_id.my_item_2_id": "My Item 2",
  "item.my_mod_id.my_item_2_id.tooltip": "Line 1\nLine 2"
}
```

# How to use
##### build.gradle.kts
```kotlin
tasks.withType<ParseLangFile> {
  inFile = File("assets/lang/en_us.json")
  outFile = File("resources/assets/modid/lang/en_us.json")
}
```