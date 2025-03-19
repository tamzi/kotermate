
plugins {
    // Apply Kotlin JVM plugin from `gradle/versions.toml`
    alias(libs.plugins.kotlin.jvm)

    // Apply Kotlin Serialization plugin from `gradle/versions.toml`
    alias(libs.plugins.kotlinPluginSerialization)
}

group = "org.tamzi"
version = "0.0.2"

dependencies {
    // Main dependencies - Kotlin ecosystem libraries
    implementation(libs.bundles.kotlinxEcosystem)

    // Test dependencies
    testImplementation(kotlin("test"))
    testImplementation(libs.bundles.testing)
    
    // Additional test dependencies
    testImplementation(libs.bundles.kotest)
    
    testImplementation(libs.assertk)
    testImplementation(libs.turbine)
    testImplementation(libs.kotlinx.coroutines.test)
    
    // Testcontainers for integration tests
    testImplementation(libs.bundles.testcontainers)

    // JUnit runtime
    testRuntimeOnly(libs.junit.jupiter.engine)
}

tasks.test {
    useJUnitPlatform()

    testLogging {
        events("passed", "skipped", "failed")
        showExceptions = true
        showCauses = true
        showStackTraces = true
        exceptionFormat = org.gradle.api.tasks.testing.logging.TestExceptionFormat.FULL
    }
}

kotlin {
    jvmToolchain(21)

    compilerOptions {
        // Enable strict null checks
        freeCompilerArgs.add("-Xjsr305=strict")
    }
}
