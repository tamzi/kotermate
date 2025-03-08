plugins {
    kotlin("jvm")
    // Apply the Application plugin to add support for building an executable JVM application.
    application
}

group = "org.tamzi"
version = "0.0.2"


dependencies {
    testImplementation(kotlin("test"))
    implementation(project(":utils"))
}

tasks.test {
    useJUnitPlatform()
}
kotlin {
    jvmToolchain(23)
}