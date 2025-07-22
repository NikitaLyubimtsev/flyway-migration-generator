package ru.lubimobile.flyway.processor

enum class FlywayOptions {
    RUN_FLYWAY_GENERATOR, OUTPUT_DIR, PROJECT_DIR, MIGRATION_VERSION, DATABASE_TYPE;

    val option: String = {
        this.name.lowercase()
    }()
}