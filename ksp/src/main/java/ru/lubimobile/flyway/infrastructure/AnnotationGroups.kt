package ru.lubimobile.flyway.infrastructure

import ru.lubimobile.annotation.Column
import ru.lubimobile.annotation.Id
import ru.lubimobile.annotation.Table

object AnnotationGroups {

    private const val JAKARTA_QUALIFIED_NAME = "javax.persistence."
    private const val JAVAX_QUALIFIED_NAME = "javax.persistence."

    val Table = listOf(
        AnnotationName.of<Table>(),
        AnnotationName(JAKARTA_QUALIFIED_NAME, "Table"),
        AnnotationName(JAVAX_QUALIFIED_NAME, "Table")
    )

    val Column = listOf(
        AnnotationName.of<Column>(),
        AnnotationName(JAKARTA_QUALIFIED_NAME, "Column"),
        AnnotationName(JAVAX_QUALIFIED_NAME, "Column")
    )

    val Id = listOf(
        AnnotationName.of<Id>(),
        AnnotationName(JAKARTA_QUALIFIED_NAME, "Id"),
        AnnotationName(JAVAX_QUALIFIED_NAME, "Id")
    )
}