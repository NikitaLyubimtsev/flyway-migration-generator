# flyway.migration.generator


[![Kotlin Experimental](https://kotl.in/badges/experimental.svg)](https://kotlinlang.org/docs/components-stability.html)
[![Kotlin](https://img.shields.io/badge/kotlin-2.1.21-blue.svg?logo=kotlin)](http://kotlinlang.org)
[![GitHub license](https://img.shields.io/badge/license-Apache%20License%202.0-blue.svg?style=flat)](https://www.apache.org/licenses/LICENSE-2.0)

This library generated a Flyway migration file using annotations. KSP is used for generation.

```kotlin
@Table(name = "entity_user")
data class Entity(
    @Id
    val id: Int,
    val name: String,
    @Column(name = "date_of_birth")
    val dateOfBirth: LocalDate
)
```

## Gradle

Add dependencies

```kotlin
plugins {
    id("org.jetbrains.kotlin.jvm") version "2.1.21"
    id("com.google.devtools.ksp") version "2.1.21-2.0.2"
}

repositories {
    maven {
        name = "GitHubPackages"
        url = uri("https://maven.pkg.github.com/NikitaLyubimtsev/flyway-migration-generator")
    }
}

dependencies {
    implementation("ru.lubimobile:flyway-migration-generator-api:0.0.1")
    
    ksp("ru.lubimobile:flyway-migration-generator-ksp:0.0.1")
}

ksp {
    arg("run_flyway_generator", "true") // Command for run generated migration files
    arg("migration_version", "V1.3_init") // Your version of migrations
    arg("output_dir", "$projectDir/src/main/resources/flyway") // Your directories with generated migration files 
}

// Manual task generation migration file.
tasks.register("generateFlywayMigration") {
    group = "flyway"
    description = "Runs KSP processor to generate Flyway migration"

    dependsOn("kspKotlin")
}
```