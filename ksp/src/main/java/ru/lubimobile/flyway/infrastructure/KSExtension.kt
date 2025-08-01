package ru.lubimobile.flyway.infrastructure

import com.google.devtools.ksp.symbol.KSAnnotated
import com.google.devtools.ksp.symbol.KSAnnotation

fun KSAnnotated.findAnnotationFromList(candidates: List<AnnotationName>): KSAnnotation? {
    val matched: List<KSAnnotation> = this.annotations.filter { ksAnnotation ->
        val qName: String? = ksAnnotation.annotationType.resolve().declaration.qualifiedName?.asString()
        candidates.any { it.qualifiedName == qName }
    }.toList()

    return when {
        matched.isEmpty() -> null
        matched.size == 1 ->  matched.first()
        else -> error(
            "Conflict: multiple annotations found from the candidate list ${
                matched.joinToString(", ") { it.annotationType.resolve().declaration.qualifiedName?.asString().orEmpty() }
            }"
        )
    }
}

inline fun <reified T> KSAnnotation.getArgumentValue(name: String): T? = this.arguments
    .firstOrNull { it.name?.asString() == name }
    ?.value as? T