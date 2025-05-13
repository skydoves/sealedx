plugins {
  kotlin("jvm")
  id(libs.plugins.nexus.plugin.get().pluginId)
}

apply(from ="${rootDir}/scripts/publish-module.gradle.kts")

mavenPublishing {
  val artifactId = "sealedx-core"
  coordinates(
    Configuration.artifactGroup,
    artifactId,
    rootProject.extra.get("libVersion").toString()
  )

  pom {
    name.set(artifactId)
    description.set("Kotlin Symbol Processor that auto-generates extensive sealed classes and interfaces for Android and Kotlin.")
  }
}