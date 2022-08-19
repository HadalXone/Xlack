plugins {
  id("kotlin")
}

dependencies {
  api(libs.slack.core)
  api(libs.ktor.server.core)
}
