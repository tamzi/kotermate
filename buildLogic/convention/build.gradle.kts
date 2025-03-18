plugins {
    `kotlin-dsl` // Enables the Kotlin DSL for writing Gradle plugins
}

group = "org.tamzi.buildlogic"

// Configure repositories for the convention plugin project
repositories {
    mavenCentral()
    gradlePluginPortal()
}

// Dependencies needed for the convention plugins
dependencies {
    implementation(libs.kotlinGradlePlugin)
    // Add other plugin dependencies that your convention plugins will use
    implementation("org.jetbrains.kotlin:kotlin-gradle-plugin:${libs.versions.kotlin.get()}")
    implementation("org.jetbrains.kotlin.plugin.serialization:org.jetbrains.kotlin.plugin.serialization.gradle.plugin:${libs.versions.kotlin.get()}")
}

// Configure the Kotlin DSL
kotlin {
    jvmToolchain(17) // Use Java 17 for convention plugins
}

// Define the convention plugins
gradlePlugin {
    plugins {
        register("kotlinLibrary") {
            id = "org.tamzi.convention.kotlin.library"
            implementationClass = "org.tamzi.convention.KotlinLibraryConventionPlugin"
        }
        
        register("kotlinApplication") {
            id = "org.tamzi.convention.kotlin.application"
            implementationClass = "org.tamzi.convention.KotlinApplicationConventionPlugin"
        }
        
        register("kotlinSerialization") {
            id = "org.tamzi.convention.kotlin.serialization"
            implementationClass = "org.tamzi.convention.KotlinSerializationConventionPlugin"
        }
    }
}