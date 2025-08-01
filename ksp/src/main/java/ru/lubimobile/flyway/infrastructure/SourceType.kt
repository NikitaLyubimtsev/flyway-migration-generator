package ru.lubimobile.flyway.infrastructure

import ru.lubimobile.flyway.builder.MySQLBuilder
import ru.lubimobile.flyway.builder.PostgreMigrationBuilder
import ru.lubimobile.flyway.builder.MigrationBuilder

enum class SourceType {
    POSTGRE_SQL, MYSQL;

    companion object {
        fun of(builder: MigrationBuilder): SourceType = when (builder) {
            PostgreMigrationBuilder -> POSTGRE_SQL
            MySQLBuilder -> MYSQL
            else -> error("Undefined Source Type")
        }
    }
}