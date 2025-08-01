package ru.lubimobile.flyway.builder

import org.junit.jupiter.api.Test
import ru.lubimobile.flyway.infrastructure.SqlField

class AnnotationTest {

    @Test
    fun `column_default_test`() {
        val fields: List<SqlField> = listOf(
            SqlField(
                name = "firstName",
                type = "VARCHAR"
            )
        )
    }
}