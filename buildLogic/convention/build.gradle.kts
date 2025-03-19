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
            id = "org.tamzi.convention.kotlin-library"
            implementationClass = "org.tamzi.convention.KotlinLibraryConventionPlugin"
        }
        
        register("kotlinApplication") {
            id = "org.tamzi.convention.kotlin-application"
            implementationClass = "org.tamzi.convention.KotlinApplicationConventionPlugin"
        }
        
        register("kotlinTest") {
            id = "org.tamzi.convention.kotlin-test"
            implementationClass = "org.tamzi.convention.KotlinTestConventionPlugin"
        }
        
        register("kotlinSerialization") {
            id = "org.tamzi.convention.kotlin-serialization"
            implementationClass = "org.tamzi.convention.KotlinSerializationConventionPlugin"
        }
    }
}
