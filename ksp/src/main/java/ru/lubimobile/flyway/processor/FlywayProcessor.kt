package ru.lubimobile.flyway.processor

import com.google.devtools.ksp.processing.Resolver
import com.google.devtools.ksp.processing.SymbolProcessor
import com.google.devtools.ksp.processing.SymbolProcessorEnvironment
import com.google.devtools.ksp.symbol.KSAnnotated
import ru.lubimobile.flyway.builder.MySQLBuilder
import ru.lubimobile.flyway.builder.PostgreSQLBuilder
import ru.lubimobile.flyway.builder.SQLBuilder
import ru.lubimobile.flyway.infrastructure.AnnotationName
import java.io.File

class FlywayProcessor(
    private val environment: SymbolProcessorEnvironment
) : SymbolProcessor {

    private val defaultOutputPath = "src/main/resources/flyway"

    override fun process(resolver: Resolver): List<KSAnnotated> {
        println("> Run generator with options: ${environment.options}")

        val enable = environment.options[FlywayOptions.RUN_FLYWAY_GENERATOR.option]?.toBoolean() ?: false

        if (!enable) {
            environment.logger.info("Flyway SQL generator is disabled.")
            return emptyList()
        }

        val outputDir: String = environment.options[FlywayOptions.OUTPUT_DIR.option] ?: getDefaultOutputPath(environment)

        val version = environment.options[FlywayOptions.MIGRATION_VERSION.option] ?: "V1.0_default"
        val file = File(outputDir, "${version}.sql")
        file.parentFile.mkdirs()

        val databaseType: String? = environment.options[FlywayOptions.DATABASE_TYPE.option]?.lowercase()

        val sqlBuilder: SQLBuilder = when (databaseType) {
            "mysql" -> MySQLBuilder
            else -> PostgreSQLBuilder
        }
        println("> Selected SQLBuilder: $sqlBuilder")

        sqlBuilder.resolver(resolver)
        //sqlBuilder.symbols()

        file.writeText(sqlBuilder.build())

        println("> Generated migration: ${file.absolutePath}")

        //return sqlBuilder.getKSAnnotatedList()
        return emptyList()
    }

    private fun getDefaultOutputPath(environment: SymbolProcessorEnvironment): String {
        val root = environment.options[FlywayOptions.PROJECT_DIR.name] ?: System.getProperty("user.dir")

        environment.logger.error("Project directory not provided in KSP args")

        val file = File(root, defaultOutputPath).also { it.mkdirs() }
        return file.absolutePath
    }
}