enableFeaturePreview("TYPESAFE_PROJECT_ACCESSORS")

pluginManagement {
    includeBuild("buildLogic")
    repositories {
        gradlePluginPortal() // This repository hosts the Foojay plugin
        mavenCentral()
    }
}

// Apply the Foojay plugin directly
plugins {
    id("org.gradle.toolchains.foojay-resolver-convention") version "0.9.0"
}

dependencyResolutionManagement {
    repositories {
        mavenCentral()
    }
    versionCatalogs {
        create("libs") {
            from(files("gradle/versions.toml"))
        }
    }
}

rootProject.name = "kotermate"
include(
    ":app",
    ":utils")

buildCache {
    local {
        isEnabled = true
        directory = File(rootDir, "build-cache")
    }
}
