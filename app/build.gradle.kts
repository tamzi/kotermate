plugins {
    alias(libs.plugins.kotlin.jvm)
    alias(libs.plugins.kotermate.kotlin.application)
    alias(libs.plugins.kotermate.kotlin.serialization)
    alias(libs.plugins.kotermate.kotlin.test)
}

group = "org.tamzi"
version = "0.0.2"

application {
    // Define the main class for the application
    mainClass.set("org.tamzi.AppKt")
}


dependencies {
    testImplementation(kotlin("test"))
    implementation(project(":utils"))
}

tasks.test {
    useJUnitPlatform()
}
kotlin {
    jvmToolchain(21)
}
