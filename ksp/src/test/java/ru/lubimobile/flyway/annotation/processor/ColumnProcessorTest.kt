package ru.lubimobile.flyway.annotation.processor

import com.google.devtools.ksp.symbol.KSAnnotated
import com.google.devtools.ksp.symbol.KSAnnotation
import com.google.devtools.ksp.symbol.KSDeclaration
import com.google.devtools.ksp.symbol.KSPropertyDeclaration
import com.google.devtools.ksp.symbol.KSType
import com.google.devtools.ksp.symbol.KSValueArgument
import com.google.devtools.ksp.symbol.Nullability
import com.google.devtools.ksp.symbol.Origin
import io.mockk.every
import io.mockk.mockk
import mu.KLogger
import mu.KotlinLogging
import org.junit.jupiter.api.Test
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.Arguments
import org.junit.jupiter.params.provider.MethodSource
import ru.lubimobile.annotation.Column
import ru.lubimobile.flyway.infrastructure.AnnotationGroups
import ru.lubimobile.flyway.infrastructure.SqlField
import ru.lubimobile.flyway.infrastructure.findAnnotationFromList
import ru.lubimobile.flyway.mocks.MockAnnotationFactory
import ru.lubimobile.flyway.mocks.MockKSAnnotatedBuilder
import ru.lubimobile.flyway.mocks.MockKSAnnotationBuilder
import ru.lubimobile.flyway.mocks.MockKSDeclarationBuilder
import ru.lubimobile.flyway.mocks.MockKSNodeBuilder
import ru.lubimobile.flyway.mocks.MockKSPropertyDeclarationBuilder
import java.util.stream.Stream
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertTrue

class ColumnProcessorTest {

    private val logger: KLogger = KotlinLogging.logger {}
    private val annotationQualifierName = "ru.lubimobile.annotation.Column"


    @Test
    fun `find annotation Column`() {
        val mockProperty: KSPropertyDeclaration = MockKSPropertyDeclarationBuilder(name = "firstName", String::class.java)
            .addAnnotation(Column::class.java)
            .build()

        val result: KSAnnotation = requireNotNull(
            mockProperty.findAnnotationFromList(AnnotationGroups.Column)
        ) { "Annotation $annotationQualifierName not found" }

        val actual = result.annotationType.resolve().declaration.qualifiedName?.asString()

        assertEquals("ru.lubimobile.annotation.Column", actual)
    }

    @Test
    fun `process column with name`() {
        val mockProperty: KSPropertyDeclaration = MockKSPropertyDeclarationBuilder("firstName", String::class.java)
            .addAnnotation(MockAnnotationFactory.columnAnnotation(name = "first_name"))
            .build()

        val actual = ColumnProcessor.process(mockProperty)

        logger.info { "Actual $actual" }

        assertEquals("first_name", actual.name)
    }

    @Test
    fun `process column with nullable`() {
        val mockProperty: KSPropertyDeclaration = MockKSPropertyDeclarationBuilder("firstName", String::class.java)
            .addAnnotation(MockAnnotationFactory.columnAnnotation(nullable = false))
            .build()

        val actual = ColumnProcessor.process(mockProperty)

        assertFalse(actual.nullable)
    }


//    @ParameterizedTest
//    @MethodSource("provideColumnSetting")
//    fun `process column`(propertyName: String, propertyType: KSType, name: String = "", nullable: Boolean = false, unique: Boolean = false, defaultValue: String = "", length: Int = 0, precision: Int = 0, scale: Int = 0, expected: SqlField) {
//        val mockProperty: KSPropertyDeclaration = mockPropertyWithColumnAnnotation(propertyName, propertyType, name, nullable, unique, defaultValue, length, precision, scale)
//
//        val actual = ColumnProcessor.process(mockProperty)
//
//        assertSQLField(expected, actual)
//    }
//
//    private fun mockPropertyWithColumnAnnotation(propertyName: String, propertyType: KSType, name: String = "", nullable: Boolean = false, unique: Boolean = false, defaultValue: String = "", length: Int = 0, precision: Int = 0, scale: Int = 0): KSPropertyDeclaration = mockProperty(
//        propertyName, propertyType, listOf(
//            mockColumnAnnotation(name, nullable, unique, defaultValue, length, precision, scale)
//        )
//    )
//
//    private fun assertSQLField(expected: SqlField, actual: SqlField) {
//        logger.info { "Actual SQLField: $actual" }
//        assertEquals(expected.name, actual.name, "Name")
//        assertEquals(expected.type, actual.type, "Type")
//        assertEquals(expected.unique, actual.unique, "Unique")
//        assertEquals(expected.nullable, actual.nullable, "Nullable")
//        assertEquals(expected, actual, "SQL result is not expected")
//    }
//
//    companion object {
//
//        val stringNullableKSType: KSType =
//
//        @JvmStatic
//        fun provideColumnSetting("email", ): Stream<Arguments> = Stream.of(
//
//        )
//    }

    fun mockColumnAnnotation(name: String = "", nullable: Boolean, unique: Boolean, defaultValue: String, length: Int, precision: Int, scale: Int): KSAnnotation = mockk<KSAnnotation>().apply {
        every { this@apply.annotationType.resolve().declaration.qualifiedName?.asString() } returns annotationQualifierName
        every { this@apply.arguments } returns listOf(
            mockAnnotationArg("name", name),
            mockAnnotationArg("unique", unique),
            mockAnnotationArg("nullable", nullable),
            mockAnnotationArg("defaultValue", defaultValue),
            mockAnnotationArg("length", length),
            mockAnnotationArg("precision", precision),
            mockAnnotationArg("scale", scale),
        )
    }

    fun mockProperty(propertyName: String, propertyType: KSType, annotations: List<KSAnnotation> = emptyList()): KSPropertyDeclaration = mockk<KSPropertyDeclaration>().apply {
        every { this@apply.simpleName.asString() } returns propertyName
        every { this@apply.type.resolve() } returns propertyType
        every { this@apply.annotations } returns annotations.asSequence()
    }

    fun mockAnnotationArg(name: String, value: Any?): KSValueArgument = mockk<KSValueArgument>().apply {
        every { this@apply.name?.asString() } returns name
        every { this@apply.value } returns value
    }

    fun mockKSType(qfTypeName: String, nullability: Nullability = Nullability.NULLABLE): KSType = mockk<KSType>().apply {
        every { this@apply.declaration.qualifiedName?.asString() } returns qfTypeName
        every { this@apply.nullability } returns nullability
    }
}