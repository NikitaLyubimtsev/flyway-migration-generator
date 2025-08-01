package ru.lubimobile.flyway.builder

import com.google.devtools.ksp.processing.Resolver
import com.google.devtools.ksp.symbol.KSAnnotated
import com.google.devtools.ksp.symbol.KSClassDeclaration
import com.google.devtools.ksp.symbol.Origin
import ru.lubimobile.flyway.annotation.processor.TableProcessor
import ru.lubimobile.flyway.infrastructure.AnnotationGroups
import ru.lubimobile.flyway.infrastructure.Migration
import ru.lubimobile.flyway.infrastructure.SQLTable
import ru.lubimobile.flyway.infrastructure.SqlField
import kotlin.apply
import kotlin.sequences.toList

object PostgreMigrationBuilder : MigrationBuilder {

    override lateinit var symbols: List<KSAnnotated>

    override lateinit var resolver: Resolver

//    override var sql: String = ""

     override lateinit var migration: Migration

    override fun symbols(value: Sequence<KSAnnotated>): PostgreMigrationBuilder = this.apply {
        symbols = value.toList()
    }

    override fun resolver(value: Resolver): PostgreMigrationBuilder = this.apply {
        resolver = value
        symbols = AnnotationGroups.Table
            .flatMap { resolver.getSymbolsWithAnnotation(it.qualifiedName).toList() }
            .distinctBy { it }
    }

    override fun migration(value: Migration): PostgreMigrationBuilder = this.apply {
        migration = value
    }

    override fun build(): Migration {
        val tables: List<SQLTable> = symbols.filterIsInstance<KSClassDeclaration>().map { classDeclaration: KSClassDeclaration ->
//            if (sql.isNotBlank()) {
//                sql += "\n\n"
//            }
            addTableName(classDeclaration)
 //           addFields(classDeclaration)
        }

        return migration.copy(tables = tables)

//        return sql
    }

    override fun getKSAnnotatedList(): List<KSAnnotated> = symbols.toList()

    private fun addTableName(classDeclaration: KSClassDeclaration): SQLTable {
        val table: SQLTable = TableProcessor.process(classDeclaration)

        return table.copy(
            fields = addFields(classDeclaration)
        )

//        sql += """
//                |${tableProcessor.generateSqlString()} (
//            """.trimMargin()
    }

    private fun addFields(classDeclaration: KSClassDeclaration): List<SqlField> = when (classDeclaration.origin) {
            Origin.JAVA -> JavaFieldBuilder
            else -> KotlinFieldBuilder
        }
        .build(classDeclaration)
}