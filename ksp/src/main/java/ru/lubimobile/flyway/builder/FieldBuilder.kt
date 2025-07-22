package ru.lubimobile.flyway.builder

import com.google.devtools.ksp.symbol.KSClassDeclaration

interface FieldBuilder {
    fun build(classDeclaration: KSClassDeclaration): String
}