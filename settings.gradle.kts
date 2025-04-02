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
include(":app")
include(":utils")

buildCache {
    local {
        isEnabled = true
        directory = File(rootDir, "build-cache")
    }
}

check(JavaVersion.current().isCompatibleWith(JavaVersion.VERSION_21)) {
    """
    kotermate requires JDK 21+ but it is currently using JDK ${JavaVersion.current()}.
    Java Home: [${System.getProperty("java.home")}]
    check this to fix this issue:
    https://www.jetbrains.com/help/idea/sdk.html#change-project-sdk
    """.trimIndent()
}