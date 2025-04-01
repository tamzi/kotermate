/*
 * Copyright 2025 kotermate
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
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