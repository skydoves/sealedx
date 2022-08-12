plugins {
  kotlin("jvm")
}

rootProject.extra.apply {
  set("PUBLISH_GROUP_ID", Configuration.artifactGroup)
  set("PUBLISH_ARTIFACT_ID", "sealedx-processor")
  set("PUBLISH_VERSION", rootProject.extra.get("rootVersionName"))
}

apply(from ="${rootDir}/scripts/publish-module.gradle")

dependencies {
  implementation(project(":sealedx-core"))

  implementation(libs.ksp)
  implementation(libs.ksp.api)
  implementation(libs.kotlinpoet.ksp)

  testImplementation(libs.junit)
  testImplementation(libs.truth)
  testImplementation(libs.kotlin.compile.testing)
}