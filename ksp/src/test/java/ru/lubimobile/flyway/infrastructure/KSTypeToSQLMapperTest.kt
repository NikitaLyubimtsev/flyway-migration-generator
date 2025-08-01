package ru.lubimobile.flyway.infrastructure

import com.google.devtools.ksp.symbol.ClassKind
import com.google.devtools.ksp.symbol.KSClassDeclaration
import com.google.devtools.ksp.symbol.KSType
import io.mockk.every
import io.mockk.junit5.MockKExtension
import io.mockk.mockk
import org.junit.jupiter.api.extension.ExtendWith
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.Arguments
import org.junit.jupiter.params.provider.MethodSource
import java.util.stream.Stream
import kotlin.test.assertEquals

@ExtendWith(MockKExtension::class)
class KSTypeToSQLMapperTest {

    companion object {

        @JvmStatic
        fun provideTypeMappingCases(): Stream<Arguments> = Stream.of(
            Arguments.of("kotlin.String", KSTypeToSQLMapper.VARCHAR),
            Arguments.of("kotlin.Short", KSTypeToSQLMapper.SMALLINT),
            Arguments.of("kotlin.Int",  KSTypeToSQLMapper.INTEGER),
            Arguments.of("kotlin.Long", KSTypeToSQLMapper.BIGINT),
            Arguments.of("kotlin.Double",KSTypeToSQLMapper.DOUBLE_PRECISION),
            Arguments.of("kotlin.Float", KSTypeToSQLMapper.REAL),
            Arguments.of("kotlin.Boolean", KSTypeToSQLMapper.BOOLEAN),
            Arguments.of("kotlin.datetime.LocalDateTime", KSTypeToSQLMapper.TIMESTAMP),
            Arguments.of("kotlin.datetime.LocalDate", KSTypeToSQLMapper.DATE),
            Arguments.of("kotlin.time.Instant", KSTypeToSQLMapper.TIMESTAMPTZ),
            Arguments.of("kotlin.datetime.Instant", KSTypeToSQLMapper.TIMESTAMPTZ),
            Arguments.of("kotlin.uuid.Uuid", KSTypeToSQLMapper.UUID),
            Arguments.of("kotlin.ByteArray", KSTypeToSQLMapper.BYTEA),

            Arguments.of("java.lang.String", KSTypeToSQLMapper.VARCHAR),
            Arguments.of("java.lang.Short", KSTypeToSQLMapper.SMALLINT),
            Arguments.of("java.lang.Integer", KSTypeToSQLMapper.INTEGER),
            Arguments.of("java.lang.Long", KSTypeToSQLMapper.BIGINT),
            Arguments.of("java.lang.Double", KSTypeToSQLMapper.DOUBLE_PRECISION),
            Arguments.of("java.lang.Float", KSTypeToSQLMapper.REAL),
            Arguments.of("java.lang.Boolean", KSTypeToSQLMapper.BOOLEAN),

            Arguments.of("short", KSTypeToSQLMapper.SMALLINT),
            Arguments.of("int", KSTypeToSQLMapper.INTEGER),
            Arguments.of("long", KSTypeToSQLMapper.BIGINT),
            Arguments.of("double", KSTypeToSQLMapper.DOUBLE_PRECISION),
            Arguments.of("float", KSTypeToSQLMapper.REAL),
            Arguments.of("boolean", KSTypeToSQLMapper.BOOLEAN),

            Arguments.of("java.time.LocalDateTime", KSTypeToSQLMapper.TIMESTAMP),
            Arguments.of("java.time.LocalDate", KSTypeToSQLMapper.DATE),
            Arguments.of("java.time.Instant", KSTypeToSQLMapper.TIMESTAMPTZ),

            Arguments.of("java.unit.Date", KSTypeToSQLMapper.TIMESTAMP),

            Arguments.of("java.util.UUID", KSTypeToSQLMapper.UUID),

            Arguments.of("java.sql.Blob", KSTypeToSQLMapper.BYTEA)
        )
    }

    @ParameterizedTest
    @MethodSource("provideTypeMappingCases")
    fun `map KSType to current SQL type`(typeName: String, expectedSql: String) {
        val ksType = mockk<KSType>()
        val declaration = mockk<KSClassDeclaration>()

        every { ksType.declaration } returns declaration
        every { declaration.qualifiedName?.asString() } returns typeName
        every { declaration.classKind } returns ClassKind.CLASS

        val result = KSTypeToSQLMapper.map(ksType)
        assertEquals(expectedSql, result)
    }
}