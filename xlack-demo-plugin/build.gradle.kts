import com.github.jengelman.gradle.plugins.shadow.tasks.ShadowJar

plugins {
  id("kotlin")
  id("com.github.johnrengelman.shadow")
}

val pluginName = "demo-plugin"

tasks.withType<ShadowJar> {
  archiveBaseName.set(pluginName)
  archiveClassifier.set("")
  archiveVersion.set("")
}

tasks.register<Copy>("deployDemoPlugin") {
  group = "xlack"
  dependsOn("shadowJar")
  from(File(project.buildDir, "libs/$pluginName.jar"))
  into(File(rootProject.projectDir, "demo/plugins"))
}

dependencies {
  implementation(project(":xlack-core"))
  implementation(project(":xlack-ui"))
}
