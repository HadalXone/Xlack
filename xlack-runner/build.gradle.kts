import com.github.jengelman.gradle.plugins.shadow.tasks.ShadowJar

plugins {
  id("kotlin")
  id("com.github.johnrengelman.shadow")
  application
}

dependencies {
  implementation(project(":xlack-core"))

  implementation(libs.slack.core)
  implementation(libs.slack.ktor)
  implementation(libs.slack.socket)

  implementation(libs.ktor.serialization.gson)
  implementation(libs.ktor.server.core)
  implementation(libs.ktor.server.netty)
  implementation(libs.ktor.server.contentNegotiation)
  implementation(libs.ktor.server.callLogging)

  implementation(libs.koin.core)
  implementation(libs.koin.ktor)

  implementation(libs.clikt)

  implementation(libs.logging.logbackClassic)
  implementation(libs.reflections)
  implementation(libs.websocket)
  implementation(libs.tyrus)
}
