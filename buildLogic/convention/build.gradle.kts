plugins {
    `kotlin-dsl` // Enables the Kotlin DSL for writing Gradle plugins
}

group = "org.tamzi.buildlogic.kotermate"

// Configure repositories for the convention plugin project
repositories {
    mavenCentral()
    gradlePluginPortal()
}

// Dependencies needed for the convention plugins
dependencies {
    compileOnly(libs.kotlinGradlePlugin)
    implementation(libs.kotlinGradlePlugin)
    implementation("org.jetbrains.kotlin:kotlin-serialization:${libs.versions.kotlin.get()}")
}

// Configure the Kotlin DSL
kotlin {
    jvmToolchain(21)
}

// Define the convention plugins
gradlePlugin {
    plugins {
        register("kotlinLibrary") {
            id = libs.plugins.kotermate.kotlin.library.get().pluginId
            implementationClass = "org.tamzi.convention.KotlinLibraryConventionPlugin"
        }
        
        register("kotlinApplication") {
            id = libs.plugins.kotermate.kotlin.application.get().pluginId
            implementationClass = "org.tamzi.convention.KotlinApplicationConventionPlugin"
        }
        
        register("kotlinTest") {
            id = libs.plugins.kotermate.kotlin.test.get().pluginId
            implementationClass = "org.tamzi.convention.KotlinTestConventionPlugin"
        }
        
        register("kotlinSerialization") {
            id = libs.plugins.kotermate.kotlin.serialization.get().pluginId
            implementationClass = "org.tamzi.convention.KotlinSerializationConventionPlugin"
        }
    }
}
