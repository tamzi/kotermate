plugins {
    kotlin("jvm")
    // Apply Kotlin Serialization plugin from `gradle/libs.versions.toml`.
    alias(libs.plugins.kotlinPluginSerialization)
}

group = "org.tamzi"
version = "0.0.2"

dependencies {

    implementation(libs.bundles.kotlinxEcosystem)
    testImplementation(kotlin("test"))
}

tasks.test {
    useJUnitPlatform()
}
kotlin {
    jvmToolchain(23)
}