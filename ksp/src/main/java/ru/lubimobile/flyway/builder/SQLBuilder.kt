package ru.lubimobile.flyway.builder

import com.google.devtools.ksp.processing.Resolver
import com.google.devtools.ksp.symbol.KSAnnotated

interface SQLBuilder {
    var symbols: Sequence<KSAnnotated>
    var resolver: Resolver
    var sql: String

    fun resolver(value: Resolver): PostgreSQLBuilder

    fun symbols(value: Sequence<KSAnnotated>): PostgreSQLBuilder

    fun build(): String

    fun getKSAnnotatedList(): List<KSAnnotated>
}