package ru.lubimobile.flyway.builder

import com.google.devtools.ksp.symbol.KSAnnotation
import com.google.devtools.ksp.symbol.KSClassDeclaration
import com.google.devtools.ksp.symbol.KSPropertyDeclaration
import com.google.devtools.ksp.symbol.KSType
import com.google.devtools.ksp.symbol.Nullability
import ru.lubimobile.flyway.infrastructure.AnnotationName
import ru.lubimobile.flyway.infrastructure.KSTypeToSQLMapper
import ru.lubimobile.flyway.infrastructure.SqlField
import ru.lubimobile.flyway.infrastructure.getAnnotation
import ru.lubimobile.flyway.infrastructure.getValue
import ru.lubimobile.flyway.infrastructure.hasAnnotationShort

abstract class AbstractFieldBuilder : FieldBuilder {

    override fun build(classDeclaration: KSClassDeclaration): String = buildFields(classDeclaration).joinToString(",\n") { it.generateSqlString() }

    private fun buildFields(classDeclaration: KSClassDeclaration): List<SqlField> = classDeclaration.getAllProperties()
        .mapNotNull(::fieldProcess)
        .toList()

    private fun fieldProcess(property: KSPropertyDeclaration): SqlField? {
        val columnNameAnnotationValue: String? = property.getAnnotation(AnnotationName.COLUMN_NAME)?.arguments?.getValue("name")

        val columnAnnotation: KSAnnotation? = property.getAnnotation(AnnotationName.COLUMN)

        val argumentNameValue: String? = columnAnnotation?.arguments?.getValue("name")

        val columnName = when {
            !columnNameAnnotationValue.isNullOrBlank() -> columnNameAnnotationValue
            !argumentNameValue.isNullOrBlank() -> argumentNameValue
            else -> property.simpleName.asString()
        }

        if (property.hasAnnotationShort(AnnotationName.ID)) {
            return SqlField(
                name = columnName,
                sqlType = "SERIAL",
                isPrimaryKey = true
            )
        }

        val ksType: KSType = property.type.resolve()
        var sqlType: String = KSTypeToSQLMapper.map(ksType)

        if (sqlType == "VARCHAR") {
            val length: String? = columnAnnotation?.arguments?.getValue("length")

            length?.let {
                sqlType += "($it)"
            }
        }

        val argumentNullableValue: Boolean? = columnAnnotation?.arguments?.getValue("nullable")
        println("Argument $columnName nullable Value: $argumentNullableValue")
        val nullable: Boolean = argumentNullableValue ?: isNullable(ksType)

        return SqlField(
            name = columnName,
            sqlType = sqlType,
            isNullable = nullable,
            isUnique = columnAnnotation?.arguments?.getValue("unique") ?: false,
            isAutoIncrement = property.hasAnnotationShort(AnnotationName.GENERATED_VALUE)
        )
    }

    private fun isNullable(type: KSType): Boolean = when (type.nullability) {
        Nullability.NULLABLE -> true
        Nullability.NOT_NULL -> false
        else -> true
    }
}