pluginManagement {
    includeBuild("build-logic")
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
    }
}

rootProject.name = "Lastro"
include(":app")
// Core
include(":core:common")
include(":core:database")
include(":core:domain")
include(":core:network")
include(":core:testing")
include(":core:ui")
// Features
include(":feature:auth")
include(":feature:transactions")
include(":feature:dashboard")
include(":feature:couple")
include(":feature:profile")
