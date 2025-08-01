package ru.lubimobile.flyway.infrastructure

import com.google.devtools.ksp.symbol.ClassKind
import com.google.devtools.ksp.symbol.KSClassDeclaration
import com.google.devtools.ksp.symbol.KSType

object KSTypeToSQLMapper {

    const val VARCHAR = "VARCHAR"

    const val SMALLINT = "SMALLINT"
    const val INTEGER = "INTEGER"
    const val BIGINT = "BIGINT"

    /**
     * SMALLINT AUTO GENERATED
     * 1 to 32 767 (2 bytes)
     */
    const val SMALLSERIA = "SMALLSERIA"

    /**
     * INTEGER AUTO GENERATED
     * 1 to 2 147 483 647 (4 bytes)
     */
    const val SERIAL = "SERIAL"

    /**
     * BIGINT AUTO GENERATED
     * 1 to 9 223 372 036 854 775 807
     */
    const val BIGSERIAL = "BIGSERIAL"

    const val DOUBLE_PRECISION = "DOUBLE PRECISION"
    const val REAL = "REAL"

    const val BOOLEAN = "BOOLEAN"

    const val TIMESTAMP = "TIMESTAMP"
    const val DATE = "DATE"
    const val TIMESTAMPTZ = "TIMESTAMPTZ"

    const val UUID = "UUID"

    const val BYTEA = "BYTEA"

    const val JSONB = "JSONB"

    fun map(type: KSType): String {
        val typeName: String? = type.declaration.qualifiedName?.asString()

        if (typeName == "kotlin.collection.Map") {
            return "JSONB"
        }

        // ENUM
        if ((type.declaration as? KSClassDeclaration)?.classKind == ClassKind.ENUM_CLASS) {
            return "VARCHAR"
        }

        return when (typeName) {
            "kotlin.String", "java.lang.String" -> VARCHAR

            "kotlin.Int", "java.lang.Integer", "int" -> INTEGER
            "kotlin.Long", "java.lang.Long", "long" -> BIGINT
            "kotlin.Short", "java.lang.Short", "short" -> SMALLINT

            "kotlin.Double", "java.lang.Double", "double" -> DOUBLE_PRECISION
            "kotlin.Float", "java.lang.Float", "float" -> REAL

            "kotlin.Boolean", "java.lang.Boolean", "boolean" -> BOOLEAN

            "kotlin.datetime.LocalDateTime", "java.time.LocalDateTime", "java.unit.Date" -> TIMESTAMP
            "kotlin.datetime.LocalDate", "java.time.LocalDate" -> DATE
            "kotlin.datetime.Instant", "kotlin.time.Instant", "java.time.Instant" -> TIMESTAMPTZ

            "kotlin.uuid.Uuid", "java.util.UUID" -> UUID

            "kotlin.ByteArray", "java.sql.Blob" -> BYTEA

            else -> JSONB
        }
    }

    fun mapIdType(sqlType: String): String = when (sqlType) {
        SMALLINT -> SMALLSERIA
        INTEGER -> SERIAL
        BIGINT -> BIGSERIAL
        else -> sqlType
    }
}