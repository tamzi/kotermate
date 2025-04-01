import org.gradle.api.tasks.testing.logging.TestLogEvent

plugins {
    // Use the kotlin-jvm plugin from the version catalog
    alias(libs.plugins.kotlin.jvm)
    alias(libs.plugins.kotermate.spotless) apply true
    // Apply the detekt plugin directly for the root project
    alias(libs.plugins.detekt)
}

group = "org.tamzi"
version = "0.0.2"

// Configure subprojects
subprojects {
    // Apply the convention plugins to all subprojects
    apply {
        plugin("com.diffplug.spotless")
        // Use the correct plugin ID for the detekt convention plugin
        plugin("org.tamzi.convention.detekt")
    }

    // Additional project-specific configuration can go here
}


repositories {
    mavenCentral()
}

dependencies {
    // For dependencies to work, we need the java plugin applied
    testImplementation(kotlin("test"))
    implementation(
        "com.autonomousapps.build-health:com.autonomousapps.build-health.gradle.plugin:2.13.0"
    )

    // Add Detekt dependencies
    detektPlugins("io.gitlab.arturbosch.detekt:detekt-formatting:1.23.8")
}

// Configure Kotlin
kotlin {
    jvmToolchain(21)
}

// Configure test tasks
tasks.withType<Test>().configureEach {
    useJUnitPlatform()

    testLogging {
        events(
            TestLogEvent.FAILED,
            TestLogEvent.PASSED,
            TestLogEvent.SKIPPED
        )
    }
}

// Define code quality tasks that run across all subprojects
tasks.register("codeQuality") {
    description = "Runs all code quality checks across all subprojects"
    group = "verification"

    // Add root project's code quality checks
    dependsOn("detekt", "spotlessCheck")
}

tasks.register("fixCodeQuality") {
    description = "Fixes all auto-fixable code quality issues across all subprojects"
    group = "verification"

    // Add root project's code quality fixes - only include tasks that can actually fix issues
    dependsOn("spotlessApply")

    // Explicitly exclude detekt as it only reports issues but doesn't fix them
    tasks.findByName("detekt")?.let { mustRunAfter(it) }
}

// Configure all subprojects to add their code quality tasks
subprojects {
    afterEvaluate {
        // Register code quality tasks
        fun registerCodeQualityTasks() {
            // Task to check all code quality issues
            tasks.register("codeQuality") {
                group = "verification"
                description = "Runs all code quality checks for this project"
                dependsOn(
                    tasks.findByName("detekt") ?: emptyList<Task>(),
                    tasks.findByName("spotlessCheck") ?: emptyList<Task>()
                )
            }

            // Task to fix all code quality issues
            tasks.register("fixCodeQuality") {
                group = "verification"
                description = "Fixes all auto-fixable code quality issues for this project"
                dependsOn(
                    tasks.findByName("spotlessApply") ?: emptyList<Task>()
                )
                // Explicitly exclude detekt as it only reports issues but doesn't fix them
                tasks.findByName("detekt")?.let { mustRunAfter(it) }
            }
        }

        // Call the function to register the tasks
        registerCodeQualityTasks()

        // Make the root tasks depend on the project-specific tasks
        rootProject.tasks.named("codeQuality").configure {
            dependsOn(tasks.named("codeQuality"))
        }

        rootProject.tasks.named("fixCodeQuality").configure {
            dependsOn(tasks.named("fixCodeQuality"))
        }
    }
}