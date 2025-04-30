# Parking Management System

A terminal-based application for managing parking lots and vehicles.

## Overview
This project provides a text-based interface for parking management operations, including:
- Vehicle entry and exit tracking
- Parking space allocation and management
- Payment processing and financial reporting
- User and staff management
- Reporting and analytics

## Features
The system is designed to run entirely in the terminal, making it lightweight, fast, and accessible on virtually any computer. For a complete list of features, see the [features document](doc/features.md).

## Getting Started
Run the application using:

`./gradlew run`

Follow the on-screen prompts to navigate the system.

# Running Codequality Checks:
Because of caching, i reommend you to run the following commands in a new terminal window.:
* Run `./gradlew clean` to clean all build outputs.


* Run `./gradlew codeQuality` to run all code quality checks.
* Run `./gradlew fixCodeQuality` to fix all auto-fixable code quality issues.

#### Not needed but incase you want to quickly run a build
* Run `./gradlew build` to only build the application.
* Run `./gradlew clean build --refresh-dependencies`
