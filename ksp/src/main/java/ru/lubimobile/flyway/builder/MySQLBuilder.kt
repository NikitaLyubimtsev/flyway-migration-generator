package ru.lubimobile.flyway.builder

import com.google.devtools.ksp.processing.Resolver
import com.google.devtools.ksp.symbol.KSAnnotated

object MySQLBuilder : SQLBuilder {
    override var symbols: Sequence<KSAnnotated>
        get() = TODO("Not yet implemented")
        set(value) {}
    override var resolver: Resolver
        get() = TODO("Not yet implemented")
        set(value) {}
    override var sql: String
        get() = TODO("Not yet implemented")
        set(value) {}

    override fun symbols(value: Sequence<KSAnnotated>): PostgreSQLBuilder {
        TODO("Not yet implemented")
    }

    override fun resolver(value: Resolver): PostgreSQLBuilder {
        TODO("Not yet implemented")
    }

    override fun build(): String {
        TODO("Not yet implemented")
    }

    override fun getKSAnnotatedList(): List<KSAnnotated> {
        TODO("Not yet implemented")
    }
}