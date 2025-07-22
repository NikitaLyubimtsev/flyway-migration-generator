package ru.lubimobile.flyway.infrastructure

enum class AnnotationName(val shortName: String, val fullName: String) {
    TABLE("Table","jakarta.persistence.Table"),
    COLUMN("Column", "jakarta.persistence.Column"),
    COLUMN_NAME("ColumnName", "ru.lubimobile.annotation.ColumnName"),
    ID("Id", "jakarta.persistence.Id"),
    GENERATED_VALUE("GeneratedValue", "jakarta.persistence.GeneratedValue")

}