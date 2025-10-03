# Whack A Shape

Small Java/Swing game with a simple bag data structure and JUnit tests.

## Requirements
- JDK 21
- Gradle (Homebrew Gradle works)

## Build and Test
```sh
gradle test
```

## Run
```sh
gradle run
```

## Project Structure
- `src/main/java/game` : game code and UI
- `src/main/java/bag` : bag interface and node
- `src/test/java/game` : JUnit tests

## Notes
Gradle uses `org.gradle.java.home` from `gradle.properties` to target JDK 21.
