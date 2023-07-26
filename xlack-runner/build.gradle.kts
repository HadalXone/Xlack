import com.github.jengelman.gradle.plugins.shadow.tasks.ShadowJar

plugins {
  id("kotlin")
  id("com.github.johnrengelman.shadow")
  application
}

application {
  mainClass.set("org.xailor.xlack.MainKt")
  group = "org.xailor.xlack"
  version = "0.1.0"
}

val runnerName = "xlack-runner"

tasks.withType<ShadowJar> {
  archiveBaseName.set(runnerName)
  archiveClassifier.set("")
  archiveVersion.set("")
}

tasks.register<Copy>("deployDemoRunner") {
  group = "xlack"
  dependsOn("shadowJar")
  from(File(project.buildDir, "libs/$runnerName.jar"))
  into(File(rootProject.projectDir, "demo"))
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
