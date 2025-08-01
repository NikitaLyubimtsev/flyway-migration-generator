package ru.lubimobile.flyway.infrastructure

import kotlinx.serialization.Serializable

@Serializable
data class SQLTable(
    val name: String,
    val fields: List<SqlField> = emptyList()
) {

    fun generateSqlString(): String = buildString {
        appendLine("CREATE TABLE IF NOT EXISTS $name (")
        append(fields.joinToString(",   \n") { it.generateSqlString() })
        appendLine()
        append(");")
    }
}
