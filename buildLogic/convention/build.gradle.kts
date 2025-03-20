plugins {
    `kotlin-dsl` // Enables the Kotlin DSL for writing Gradle plugins
}

group = "org.tamzi.buildlogic.kotermate"

/**
 * Configure repositories for the convention plugin project.
 * These repositories are used to resolve dependencies for this build script,
 * not for the projects that apply these convention plugins.
 */
repositories {
    mavenCentral()
    gradlePluginPortal()
}

/**
 * Dependencies needed for the convention plugins.
 * These are the plugins that our convention plugins will apply to target projects.
 */
dependencies {
    // Kotlin Gradle plugin is needed to configure Kotlin projects
    implementation(libs.kotlinGradlePlugin)
    
    // Kotlin serialization plugin for the kotlinSerialization convention
    implementation("org.jetbrains.kotlin:kotlin-serialization:${libs.versions.kotlin.get()}")
}

/**
 * Configure the Kotlin DSL for this project.
 * This sets the JVM version used to compile this convention plugin project.
 * Note: This should match the JVM version used in the convention plugins themselves.
 */
kotlin {
    jvmToolchain(23) // Using Java 23 to match the toolchain in the convention plugins
}

/**
 * Define the convention plugins that will be published by this project.
 * Each plugin encapsulates a specific set of build configurations.
 */
gradlePlugin {
    plugins {
        register("kotlinLibrary") {
            id = libs.plugins.kotermate.kotlin.library.get().pluginId
            implementationClass = "org.tamzi.convention.KotlinLibraryConventionPlugin"
            description = "Configures Kotlin libraries with standard settings and dependencies"
        }
        
        register("kotlinApplication") {
            id = libs.plugins.kotermate.kotlin.application.get().pluginId
            implementationClass = "org.tamzi.convention.KotlinApplicationConventionPlugin"
            description = "Configures Kotlin applications with runtime settings and distribution options"
        }
        
        register("kotlinTest") {
            id = libs.plugins.kotermate.kotlin.test.get().pluginId
            implementationClass = "org.tamzi.convention.KotlinTestConventionPlugin"
            description = "Sets up unit and integration testing with JaCoCo coverage reporting"
        }
        
        register("kotlinSerialization") {
            id = libs.plugins.kotermate.kotlin.serialization.get().pluginId
            implementationClass = "org.tamzi.convention.KotlinSerializationConventionPlugin"
            description = "Configures Kotlin serialization for JSON and other formats"
        }
    }
}
