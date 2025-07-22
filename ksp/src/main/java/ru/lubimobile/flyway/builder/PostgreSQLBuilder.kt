package ru.lubimobile.flyway.builder

import com.google.devtools.ksp.processing.Resolver
import com.google.devtools.ksp.symbol.KSAnnotated
import com.google.devtools.ksp.symbol.KSClassDeclaration
import com.google.devtools.ksp.symbol.KSPropertyDeclaration
import com.google.devtools.ksp.symbol.Origin
import ru.lubimobile.flyway.infrastructure.AnnotationName
import ru.lubimobile.flyway.infrastructure.getAnnotationShort
import ru.lubimobile.flyway.infrastructure.getValue
import kotlin.apply
import kotlin.sequences.filterIsInstance
import kotlin.sequences.firstOrNull
import kotlin.sequences.forEach
import kotlin.sequences.toList
import kotlin.text.isNotBlank
import kotlin.text.lowercase
import kotlin.text.trimMargin

object PostgreSQLBuilder : SQLBuilder {
    override lateinit var symbols: Sequence<KSAnnotated>
    override lateinit var resolver: Resolver
    override var sql: String = ""

    override fun symbols(value: Sequence<KSAnnotated>): PostgreSQLBuilder = this.apply {
        symbols = value
    }

    override fun resolver(value: Resolver): PostgreSQLBuilder = this.apply {
        resolver = value
        symbols = resolver.getSymbolsWithAnnotation(AnnotationName.TABLE.fullName)
    }

    override fun build(): String {
        symbols.filterIsInstance<KSClassDeclaration>().forEach { classDeclaration: KSClassDeclaration ->
            if (sql.isNotBlank()) {
                sql += "\n\n"
            }
            addTableName(classDeclaration)
            addFields(classDeclaration)
        }

        return sql
    }

    override fun getKSAnnotatedList(): List<KSAnnotated> = symbols.toList()

    private fun addTableName(classDeclaration: KSClassDeclaration) {
        val argumentNameValue: String? = classDeclaration.getAnnotationShort(AnnotationName.TABLE)?.arguments?.getValue("name")

        val tableName = if (argumentNameValue.isNullOrBlank()) classDeclaration.simpleName.asString().lowercase() else argumentNameValue

        sql += """
                |CREATE TABLE IF NOT EXISTS $tableName (
            """.trimMargin()
    }

    private fun addFields(classDeclaration: KSClassDeclaration) {
        val fieldBuilder: FieldBuilder = when (classDeclaration.origin) {
            Origin.JAVA -> JavaFieldBuilder
            else -> KotlinFieldBuilder
        }

        println("> File Builder selected with ${classDeclaration.simpleName}: $fieldBuilder")

        val fields = fieldBuilder.build(classDeclaration)

        sql += """ 
            |
            |$fields
            |);
        """.trimMargin()
    }

    private fun addTrigger(property: KSPropertyDeclaration): String {
        val trigger = property.annotations
            .firstOrNull { it.shortName.asString() == "Trigger" }?.shortName?.asString()
        return if (trigger === null) "" else " $trigger"
    }

    private fun mapKotlinTypeToSql(kotlinType: String?): String = when (kotlinType) {
        "kotlin.Int" -> "INTEGER"
        "kotlin.String" -> "VARCHAR(255)"
        "kotlin.Long" -> "BIGINT"
        "kotlin.Boolean" -> "BOOLEAN"
        "kotlin.Float" -> "FLOAT"
        "kotlin.Double" -> "DOUBLE"
        else -> "TEXT"
    }
}