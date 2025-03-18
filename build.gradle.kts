plugins {
    // Use the kotlin-jvm plugin from the version catalog
    alias(libs.plugins.kotlin.jvm)
}

group = "org.tamzi"
version = "0.0.2"


dependencies {
    testImplementation(kotlin("test"))
}

tasks.test {
    useJUnitPlatform()
}
kotlin {
    jvmToolchain(23)
}
