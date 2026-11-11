import java.util.Properties

pluginManagement {
    repositories {
        google {
            content {
                includeGroupByRegex("com\\.android.*")
                includeGroupByRegex("com\\.google.*")
                includeGroupByRegex("androidx.*")
            }
        }
        mavenCentral()
        gradlePluginPortal()
    }
}
plugins {
    id("org.gradle.toolchains.foojay-resolver-convention") version "1.0.0"
}
dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        google()
        mavenCentral()
        maven {
            url = uri("https://api.mapbox.com/downloads/v2/releases/maven")
            credentials {
                username = "mapbox"
                password = Properties().run {
                    val localPropertiesFile = file("local.properties")
                    if (localPropertiesFile.exists()) {
                        localPropertiesFile.inputStream().use { load(it) }
                    }
                    getProperty("MAPBOX_ACCESS_TOKEN", "")
                }
            }
        }
    }
}

rootProject.name = "ProyectoAlertaLocal"
include(":app")
 