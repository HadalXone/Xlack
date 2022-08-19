plugins {
  id("kotlin")
}

dependencies {
  implementation(project(":xlack-core"))
  implementation(libs.koin.core)
  implementation(libs.slack.core)
  api(libs.slack.apiModelExt)
}
