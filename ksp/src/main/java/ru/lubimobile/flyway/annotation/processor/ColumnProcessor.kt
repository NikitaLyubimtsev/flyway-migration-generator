package ru.lubimobile.flyway.annotation.processor

import com.google.devtools.ksp.symbol.KSAnnotation
import com.google.devtools.ksp.symbol.KSPropertyDeclaration
import com.google.devtools.ksp.symbol.KSType
import com.google.devtools.ksp.symbol.Nullability
import ru.lubimobile.flyway.infrastructure.AnnotationGroups
import ru.lubimobile.flyway.infrastructure.KSTypeToSQLMapper
import ru.lubimobile.flyway.infrastructure.SqlField
import ru.lubimobile.flyway.infrastructure.findAnnotationFromList
import ru.lubimobile.flyway.infrastructure.getArgumentValue

/**
 * Обработка аннотации `@Column`.
 *
 * На основе аннотации `@Column` создается sql строка `column_name type [NULL | NOT NULL] [UNIQUE]`
 *
 * При установке аннотации @Id строка будет автоматически создаваться вида:
 * `columnName SERIAL NOT NULL` - в последующих версиях будет расширение для изменения типа Id.
 *
 * @see ru.lubimobile.annotation.Column
 * @see ru.lubimobile.annotation.Id
 */
object ColumnProcessor {

    fun process(property: KSPropertyDeclaration): SqlField {
        val columnAnnotation: KSAnnotation? = property.findAnnotationFromList(AnnotationGroups.Column)
        val annotationColumName: String? = columnAnnotation?.getArgumentValue("name")
        val columnName: String = if (annotationColumName.isNullOrBlank())  property.simpleName.asString() else annotationColumName

        val ksType = property.type.resolve()
        var type: String = KSTypeToSQLMapper.map(ksType)

        if (type == "VARCHAR") {
            val length: Int? = columnAnnotation?.getArgumentValue("length")

            length?.let {
                if (it > 0) {
                    type += "($it)"
                }
            }
        }

        if (property.findAnnotationFromList(AnnotationGroups.Id) != null) {
            return SqlField(
                name = columnName,
                type = KSTypeToSQLMapper.mapIdType(type),
                nullable = false
            )
        }

        return SqlField(
            name = columnName,
            type = type,
            nullable = columnAnnotation?.getArgumentValue("nullable") ?: isNullable(ksType)
        )
    }

    private fun isNullable(type: KSType): Boolean = when (type.nullability) {
        Nullability.NULLABLE -> true
        Nullability.NOT_NULL -> false
        else -> true
    }
}