package ru.lubimobile.flyway.builder

import com.google.devtools.ksp.processing.Resolver
import com.google.devtools.ksp.symbol.KSAnnotated
import ru.lubimobile.flyway.infrastructure.Migration

interface MigrationBuilder {
    var symbols: List<KSAnnotated>
    var resolver: Resolver
//    var sql: String
    var migration: Migration

    fun resolver(value: Resolver): PostgreMigrationBuilder

    fun symbols(value: Sequence<KSAnnotated>): PostgreMigrationBuilder

    fun migration(value: Migration): PostgreMigrationBuilder

    fun build(): Migration

    fun getKSAnnotatedList(): List<KSAnnotated>
}