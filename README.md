# Taco Cloud Clone Coding

Spring MVC, Thymeleaf, JDBC, and an embedded H2 database demonstrate a small taco ordering flow.

## Requirements

- Java 17
- Maven Wrapper (included)

## Build and test

```powershell
.\mvnw.cmd clean verify
```

Static analysis and a software bill of materials can be generated with:

```powershell
.\mvnw.cmd spotbugs:check org.cyclonedx:cyclonedx-maven-plugin:makeAggregateBom
```

The application uses only the embedded H2 database, so the automated tests do not require external services.
