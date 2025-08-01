package ru.lubimobile.flyway.builder

import com.google.devtools.ksp.symbol.KSClassDeclaration
import ru.lubimobile.flyway.annotation.processor.ColumnProcessor
import ru.lubimobile.flyway.infrastructure.SqlField

abstract class AbstractFieldBuilder : FieldBuilder {

    override fun build(classDeclaration: KSClassDeclaration): List<SqlField> = classDeclaration.getAllProperties()
        .map(ColumnProcessor::process)
        .toList()
}