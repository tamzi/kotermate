package org.tamzi.convention

import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.artifacts.VersionCatalogsExtension
import org.gradle.kotlin.dsl.apply
import org.gradle.kotlin.dsl.dependencies
import org.gradle.kotlin.dsl.getByType

/**
 * Convention plugin for Kotlin serialization.
 * Applies standard configurations for Kotlin serialization in the project.
 */
class KotlinSerializationConventionPlugin : Plugin<Project> {
    override fun apply(target: Project) {
        with(target) {
            // Apply the Kotlin serialization plugin
            apply(plugin = "org.jetbrains.kotlin.plugin.serialization")
            
            // Get the version catalog
            val libs = extensions.getByType<VersionCatalogsExtension>().named("libs")
            
            // Add serialization dependencies
            dependencies {
                // Add kotlinx.serialization dependency using the version from the catalog
                add("implementation", libs.findLibrary("kotlinx-serialization-json").get())
            }
        }
    }
}
