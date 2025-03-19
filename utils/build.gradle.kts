
plugins {
    alias(libs.plugins.kotlin.jvm)
    alias(libs.plugins.kotermate.kotlin.library)
    alias(libs.plugins.kotermate.kotlin.serialization)
    alias(libs.plugins.kotermate.kotlin.test)
}

group = "org.tamzi"
version = "0.0.2"

dependencies {
    // Main dependencies - Kotlin ecosystem libraries
    implementation(libs.bundles.kotlinxEcosystem)
    
    // No need to add test dependencies here - they're added by the convention plugin
}

kotlin {
    jvmToolchain(21)

    compilerOptions {
        // Enable strict null checks
        freeCompilerArgs.add("-Xjsr305=strict")
    }
}
