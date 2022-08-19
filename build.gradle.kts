buildscript {
  repositories {
    google()
    mavenCentral()
  }
}

plugins {
  alias(libs.plugins.kotlin.jvm) apply false
  alias(libs.plugins.shadow) apply false
}

tasks.register("deployDemo") {
  group = "xlack"
  dependsOn(":xlack-runner:deployDemoRunner")
  dependsOn(":xlack-demo-plugin:deployDemoPlugin")
}

tasks.register<Delete>("cleanDemo") {
  group = "xlack"
  delete = setOf(rootProject.files("demo"))
}

tasks.register<JavaExec>("initDemo") {
  group = "xlack"
  dependsOn(":deployDemo")
  mainClass.set("org.arxing.xlack.MainKt")
  args = listOf("init")
  classpath = rootProject.files("demo/xlack-runner.jar")
}

tasks.register<JavaExec>("runDemo") {
  group = "xlack"
  dependsOn(":deployDemo")
  mainClass.set("org.arxing.xlack.MainKt")
  args = listOf("--config", "xlack-config.json", "start")
  classpath = rootProject.files("demo/xlack-runner.jar")
}

tasks.register<JavaExec>("testMain") {
  group = "xlack"
  dependsOn(":deployDemo")
  mainClass.set("org.arxing.xlack.MainKt")
  args = listOf("--config", "xlack-config.json", "test")
  classpath = rootProject.files("demo/xlack-runner.jar")
}