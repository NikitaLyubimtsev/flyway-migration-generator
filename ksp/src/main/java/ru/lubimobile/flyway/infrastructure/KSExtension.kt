package ru.lubimobile.flyway.infrastructure

import com.google.devtools.ksp.symbol.KSAnnotation
import com.google.devtools.ksp.symbol.KSClassDeclaration
import com.google.devtools.ksp.symbol.KSPropertyDeclaration
import com.google.devtools.ksp.symbol.KSValueArgument
import com.google.devtools.ksp.symbol.Modifier

fun KSPropertyDeclaration.isPublic(): Boolean {
    return this.modifiers.contains(Modifier.PUBLIC)
}

fun KSPropertyDeclaration.hasAnnotationShort(annotation: AnnotationName): Boolean = this.annotations.any {
    it.annotationType.resolve().declaration.simpleName.asString() == annotation.shortName
}

fun KSPropertyDeclaration.getAnnotation(annotation: AnnotationName): KSAnnotation? = this.annotations.firstOrNull {
    it.shortName.asString() == annotation.shortName
}

fun KSClassDeclaration.getAnnotationShort(annotation: AnnotationName): KSAnnotation? = this.annotations.firstOrNull {
    it.shortName.asString() == annotation.shortName
}

inline fun <reified T> List<KSValueArgument>.getValue(name: String): T? = this
    .firstOrNull { it.name?.asString() == name }
    ?.value as? T