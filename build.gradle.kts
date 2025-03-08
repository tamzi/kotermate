plugins {
    kotlin("jvm") version "2.1.10"
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