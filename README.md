# Vueling Search Testing
by Pol Piñol Castuera

## Prerequisites

To build and test this project, you need to have Gradle installed. Here's how you can install it on macOS using Homebrew:

```bash
brew install gradle
```

## Setting Up the Gradle Wrapper

To set up the Gradle wrapper, run the following command:

```bash
gradle wrapper
```

This will generate two executables, and you should use the appropriate one for your operating system. For macOS, we'll use the `gradlew` script.

## Make `gradlew` Executable

Ensure the `gradlew` script has executable permissions:

```bash
chmod +x gradlew
```

## Building the Project

To build only the project, use the following command:

```bash
./gradlew build
```

## Running Tests

To execute the test suite, run:

```bash
./gradlew test
```

This will compile the project, run the tests, and report the results.