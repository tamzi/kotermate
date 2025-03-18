package org.tamzi.convention

import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.plugins.JavaLibraryPlugin
import org.gradle.kotlin.dsl.*
import org.jetbrains.kotlin.gradle.dsl.KotlinJvmProjectExtension

/**
 * Convention plugin for Kotlin libraries.
 * Applies standard configurations for Kotlin JVM libraries in the project.
 */
class KotlinLibraryConventionPlugin : Plugin<Project> {
    override fun apply(target: Project) {
        with(target) {
            // Apply the necessary plugins
            apply<JavaLibraryPlugin>()
            apply(plugin = "org.jetbrains.kotlin.jvm")
            
            // Configure Kotlin
            extensions.configure<KotlinJvmProjectExtension> {
                jvmToolchain(23) // Use Java 23 as specified in your versions.toml
                
                // Configure Kotlin compiler options
                compilerOptions {
                    freeCompilerArgs.add("-Xjsr305=strict") // JSR 305 annotations support
                    allWarningsAsErrors.set(true) // Treat warnings as errors
                }
            }
            
            // Add standard dependencies
            dependencies {
                // Add test dependencies
                add("testImplementation", kotlin("test"))
                add("testImplementation", kotlin("test-junit5"))
            }
            
            // Configure test tasks
            tasks.withType<org.gradle.api.tasks.testing.Test>().configureEach {
                useJUnitPlatform() // Use JUnit 5 for tests
                
                // Enable parallel test execution for faster builds
                maxParallelForks = (Runtime.getRuntime().availableProcessors() / 2).coerceAtLeast(1)
                
                // Set memory constraints for the test process
                jvmArgs = listOf("-Xmx1g", "-Xms256m")
                
                // Configure test execution logging
                testLogging {
                    events("passed", "skipped", "failed")
                    showExceptions = true
                    showCauses = true
                    showStackTraces = true
                    exceptionFormat = org.gradle.api.tasks.testing.logging.TestExceptionFormat.FULL
                    showStandardStreams = false // Set to true for verbose output
                }
                
                // Generate HTML test reports
                reports {
                    html.required.set(true)
                    junitXml.required.set(true)
                }
                
                // Fail the build on the first test failure
                failFast = false // Set to true to stop testing after first failure
            }
        }
    }
}
