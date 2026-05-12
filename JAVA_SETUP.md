# Java Setup (Windows/macOS/Linux)

If Gradle fails with a toolchain/daemon message like:

`Cannot find a Java installation ... matching Java 21 ... from gradle-daemon-jvm.properties`

this project now uses your installed JDK (via `JAVA_HOME`) because `gradle-daemon-jvm.properties` is kept without strict criteria.

## Recommended
- Install **JDK 21** (Temurin or Oracle).
- Set `JAVA_HOME` to the JDK 21 folder.
- Ensure `java -version` reports Java 21.

## Verify
```bash
java -version
./gradlew -version
```

## Why this change
We removed strict daemon matching to avoid local environment lockouts (especially on Windows), while keeping CI pinned to Java 21. The file is still tracked to reduce delete-vs-modify merge conflicts between branches.
