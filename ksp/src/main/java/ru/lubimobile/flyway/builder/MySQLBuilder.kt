package ru.lubimobile.flyway.builder

import com.google.devtools.ksp.processing.Resolver
import com.google.devtools.ksp.symbol.KSAnnotated
import ru.lubimobile.flyway.infrastructure.Migration

object MySQLBuilder : MigrationBuilder {
    override var symbols: List<KSAnnotated>
        get() = TODO("Not yet implemented")
        set(value) {}
    override var resolver: Resolver
        get() = TODO("Not yet implemented")
        set(value) {}
    override var migration: Migration
        get() = TODO("Not yet implemented")
        set(value) {}

    override fun symbols(value: Sequence<KSAnnotated>): PostgreMigrationBuilder {
        TODO("Not yet implemented")
    }

    override fun migration(value: Migration): PostgreMigrationBuilder {
        TODO("Not yet implemented")
    }

    override fun resolver(value: Resolver): PostgreMigrationBuilder {
        TODO("Not yet implemented")
    }

    override fun build(): Migration {
        TODO("Not yet implemented")
    }

    override fun getKSAnnotatedList(): List<KSAnnotated> {
        TODO("Not yet implemented")
    }
}