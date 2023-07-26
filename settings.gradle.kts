pluginManagement {
  includeBuild("xlack-build")
  repositories {
    google()
    mavenCentral()
    gradlePluginPortal()
  }
}

dependencyResolutionManagement {
  repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
  repositories {
    google()
    mavenCentral()
  }
}
rootProject.name = "xlack"
include(
  ":xlack-core",
  ":xlack-runner",
  ":xlack-cli",
  ":xlack-ui",
  ":xlack-demo-plugin",
)
