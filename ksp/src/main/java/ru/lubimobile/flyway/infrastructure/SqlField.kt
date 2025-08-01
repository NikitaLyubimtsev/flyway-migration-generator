package ru.lubimobile.flyway.infrastructure

import kotlinx.serialization.Serializable

/**
 * SQL Field property with create and modifier table
 */
@Serializable
data class SqlField(
    val name: String,
    val type: String,
    val nullable: Boolean = false,
    val unique: Boolean = false,
) {

    fun generateSqlString(): String = buildString {
        append("    $name $type")
        if (nullable) append(" NULL") else append(" NOT NULL")
        if (unique) append(" UNIQUE")
    }
}