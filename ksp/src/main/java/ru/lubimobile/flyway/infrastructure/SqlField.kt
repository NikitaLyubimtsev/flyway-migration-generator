package ru.lubimobile.flyway.infrastructure

/**
 * SQL Field property with create and modifier table
 */
data class SqlField(
    val name: String,
    val sqlType: String,
    val isNullable: Boolean = false,
    val isUnique: Boolean = false,
    val isPrimaryKey: Boolean = false,
    val isAutoIncrement: Boolean = false
) {

    fun generateSqlString(): String = buildString {
        append("    $name $sqlType")
        if (isPrimaryKey) append(" PRIMARY KEY")
        if (isAutoIncrement) append(" AUTOINCREMENT")
        if (!isNullable) append(" NOT NULL")
        if (isUnique) append(" UNIQUE")
    }
}