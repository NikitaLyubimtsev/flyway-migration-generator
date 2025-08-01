package ru.lubimobile.flyway.annotation.processor

import com.google.devtools.ksp.symbol.KSAnnotation
import com.google.devtools.ksp.symbol.KSClassDeclaration
import ru.lubimobile.flyway.infrastructure.AnnotationGroups
import ru.lubimobile.flyway.infrastructure.SQLTable
import ru.lubimobile.flyway.infrastructure.findAnnotationFromList
import ru.lubimobile.flyway.infrastructure.getArgumentValue

object TableProcessor {
    fun process(classDeclaration: KSClassDeclaration): SQLTable {
        val annotation: KSAnnotation? = classDeclaration.findAnnotationFromList(AnnotationGroups.Table)

        return  annotation?.let {
            SQLTable(
                name = it.getArgumentValue("name") ?: classDeclaration.simpleName.asString().lowercase()
            )
        } ?: error("Undefined `@Table` with class ${classDeclaration.qualifiedName}")
    }
}