# kotermate

Kotlin terminal multimodule template project

* Run `./gradlew run` to build and run the application.
* Run `./gradlew build` to only build the application.
* Run `./gradlew check` to run all checks, including tests.
* Run `./gradlew clean` to clean all build outputs.
* Run `./gradlew codeQuality` to run all code quality checks.
* Run `./gradlew fixCodeQuality` to fix all auto-fixable code quality issues.
* Run `./gradlew :app:spotlessApply` to fix all auto-fixable code quality issues in app module.
* Run `./gradlew :app:codeQuality` to  run code quality checks in app module.

Note the usage of the Gradle Wrapper (`./gradlew`).
This is the suggested way to use Gradle in production projects.

[Learn more about the Gradle Wrapper](https://docs.gradle.org/current/userguide/gradle_wrapper.html).

[Learn more about Gradle tasks](https://docs.gradle.org/current/userguide/command_line_interface.html#common_tasks).

This project follows the suggested multi-module setup and consists of the `app` and `utils` subprojects.
The shared build logic was extracted to a convention plugin located in `buildSrc`.

This project uses a version catalog (see `gradle/versions.toml`) to declare and version dependencies
and both a build cache and a configuration cache (see `gradle.properties`).