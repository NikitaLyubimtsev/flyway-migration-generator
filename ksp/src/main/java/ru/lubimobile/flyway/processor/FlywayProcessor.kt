package ru.lubimobile.flyway.processor

import com.google.devtools.ksp.processing.Resolver
import com.google.devtools.ksp.processing.SymbolProcessor
import com.google.devtools.ksp.processing.SymbolProcessorEnvironment
import com.google.devtools.ksp.symbol.KSAnnotated
import ru.lubimobile.flyway.builder.MySQLBuilder
import ru.lubimobile.flyway.builder.PostgreMigrationBuilder
import ru.lubimobile.flyway.builder.MigrationBuilder
import ru.lubimobile.flyway.infrastructure.Migration
import ru.lubimobile.flyway.infrastructure.SourceType
import java.io.File

class FlywayProcessor(
    private val environment: SymbolProcessorEnvironment
) : SymbolProcessor {

    private val defaultOutputPath = "src/main/resources/flyway"
    private val defaultSchemaPath = "src/main/resources/schema"

    override fun process(resolver: Resolver): List<KSAnnotated> {
        println("> Run generator with options: ${environment.options}")

        val enable = environment.options[FlywayOptions.RUN_FLYWAY_GENERATOR.option]?.toBoolean() ?: false

        if (!enable) {
            environment.logger.info("Flyway SQL generator is disabled.")
            return emptyList()
        }

        val outputDir: String = environment.options[FlywayOptions.OUTPUT_DIR.option] ?: getDefaultOutputPath(environment)

        val version = environment.options[FlywayOptions.MIGRATION_VERSION.option] ?: "V1.0_default"
        val migrationFile = File(outputDir, "${version}.sql")
        val schemaFile = File(getDefaultSchemaPath(environment), "${version}.json")
        migrationFile.parentFile.mkdirs()
        schemaFile.parentFile.mkdirs()

        val databaseType: String? = environment.options[FlywayOptions.DATABASE_TYPE.option]?.lowercase()

        val sqlBuilder: MigrationBuilder = when (databaseType) {
            "mysql" -> MySQLBuilder
            else -> PostgreMigrationBuilder
        }
        println("> Selected SQLBuilder: $sqlBuilder")

        sqlBuilder.resolver(resolver)
        sqlBuilder.migration(Migration(type = SourceType.of(sqlBuilder), name = version))

        val migration: Migration = sqlBuilder.build()

        migrationFile.writeText(migration.createMigrationSql())
        schemaFile.writeText(migration.createSchemas())

        println("> Generated migration: ${migrationFile.absolutePath}")

        //return sqlBuilder.getKSAnnotatedList()
        return emptyList()
    }

    private fun getDefaultOutputPath(environment: SymbolProcessorEnvironment): String = getDefaultPath(environment, defaultOutputPath)
//    {
//        val root = environment.options[FlywayOptions.PROJECT_DIR.name] ?: System.getProperty("user.dir")
//
//        environment.logger.error("Project directory not provided in KSP args")
//
//        val file = File(root, defaultOutputPath).also { it.mkdirs() }
//        return file.absolutePath
//    }

    private fun getDefaultSchemaPath(environment: SymbolProcessorEnvironment): String = getDefaultPath(environment, defaultSchemaPath)

    private fun getDefaultPath(environment: SymbolProcessorEnvironment, path: String): String {
        val root = environment.options[FlywayOptions.PROJECT_DIR.name] ?: System.getProperty("user.dir")

        environment.logger.error("Project directory not provided in KSP args")

        val file = File(root, path).also { it.mkdirs() }

        return file.absolutePath
    }
}