package ru.lubimobile.flyway.infrastructure

import com.google.devtools.ksp.symbol.ClassKind
import com.google.devtools.ksp.symbol.KSClassDeclaration
import com.google.devtools.ksp.symbol.KSType

object KSTypeToSQLMapper {

    fun map(type: KSType): String {
        val typeName = type.declaration.qualifiedName?.asString()

        if (typeName == "kotlin.collection.Map") {
            return "JSONB"
        }

        // ENUM
        if ((type.declaration as? KSClassDeclaration)?.classKind == ClassKind.ENUM_CLASS) {
            return "VARCHAR"
        }

        return when (typeName) {
            "kotlin.String", "java.lang.String" -> "VARCHAR"

            "kotlin.Int", "java.lang.Integer", "int" -> "INTEGER"
            "kotlin.Long", "java.lang.Long", "long" -> "BIGINT"
            "kotlin.Short", "java.lang.Short", "short" -> "SMALLINT"

            "kotlin.Double", "java.lang.Double", "double" -> "DOUBLE PRECISION"
            "kotlin.Float", "java.lang.Float", "float" -> "REAL"

            "kotlin.Boolean", "java.lang.Boolean", "boolean" -> "BOOLEAN"

            "kotlin.datetime.LocalDateTime", "java.time.LocalDateTime", "java.unit.Date" -> "TIMESTAMP"
            "kotlin.datetime.LocalDate", "java.time.LocalDate" -> "DATE"
            "kotlin.datetime.Instant", "java.time.Instant" -> "TIMESTAMPTZ"

            "kotlin.uuid.Uuid", "java.util.UUID" -> "UUID"

            "kotlin.ByteArray", "java.sql.Blob" -> "BYTEA"

            else -> "JSONB"
        }
    }
}