package ru.lubimobile.flyway.infrastructure

import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json

@Serializable
data class Migration(
    val type: SourceType,
    val name: String,
    val tables: List<SQLTable> = emptyList()
) {

    fun createMigrationSql(): String = tables
        .joinToString("\n\n") { it.generateSqlString() }

    fun createSchemas(): String = Json.encodeToString(this)
}