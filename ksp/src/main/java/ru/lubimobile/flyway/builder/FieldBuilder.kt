package ru.lubimobile.flyway.builder

import com.google.devtools.ksp.symbol.KSClassDeclaration
import ru.lubimobile.flyway.infrastructure.SqlField

interface FieldBuilder {
    fun build(classDeclaration: KSClassDeclaration): List<SqlField>
}