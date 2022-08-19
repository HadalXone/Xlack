plugins {
  id("kotlin")
}

dependencies {
  implementation(libs.slack.core)
  implementation(libs.clikt)
  implementation(libs.kotlin.coroutines)
  implementation(project(":xlack-ui"))
}
