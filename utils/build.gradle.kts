
plugins {
    // Remove redundant kotlin("jvm") as it's already included via alias
    alias(libs.plugins.kotlin.jvm)

    // Apply Kotlin Serialization plugin from `gradle/versions.toml`.
    alias(libs.plugins.kotlinPluginSerialization)
}

group = "org.tamzi"
version = "0.0.2"

// Additional dependencies specific to this module
dependencies {
    implementation(libs.bundles.kotlinxEcosystem)
    testImplementation(kotlin("test"))
}

tasks.test {
    useJUnitPlatform()
}
kotlin {
    jvmToolchain(23)

    // Configure Kotlin targets properly instead of using the deprecated property
    compilerOptions {
        // Modern configuration options
        freeCompilerArgs.add("-Xjsr305=strict")
    }
}