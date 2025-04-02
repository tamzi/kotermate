
dependencyResolutionManagement {
    repositories {
        google()
        mavenCentral()
    }
    versionCatalogs {
        create("libs") {
            from(files("../gradle/versions.toml"))
        }
    }
}

rootProject.name = "buildLogic"
include(":convention")