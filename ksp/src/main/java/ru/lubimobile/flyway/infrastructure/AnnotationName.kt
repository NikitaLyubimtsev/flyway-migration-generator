package ru.lubimobile.flyway.infrastructure

data class AnnotationName(val qualifiedName: String, val shortName: String) {
    companion object {
        inline fun <reified T : Annotation> of(): AnnotationName = AnnotationName(T::class.java.name, T::class.java.simpleName)
    }
}