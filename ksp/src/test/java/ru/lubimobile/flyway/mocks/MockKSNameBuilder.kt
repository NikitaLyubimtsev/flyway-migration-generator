package ru.lubimobile.flyway.mocks

import com.google.devtools.ksp.symbol.KSName
import io.mockk.every
import io.mockk.mockk

/**
 * Builder Mockk with `KSName`
 *
 * Example usage:
 * ```
 * val ksName = KSNameBuilder(MyClass::class.java).build()
 *
 * println(ksName.asString()) // com.example.MyClass
 * println(ksName.getQualifier()) // com.example
 * println(ksName.getShortName()) // MyClass
 *
 * val ksName = KSNameBuilder("com.example.MyClass").build()
 *
 * println(ksName.asString()) // com.example.MyClass
 * println(ksName.getQualifier()) // com.example
 * println(ksName.getShortName()) // MyClass
 * ```
 *
 * @see KSName
 */
class MockKSNameBuilder private constructor (
    val qualifier: String,
    val shortName: String
) {

    constructor(clazz: Class<*>) : this(
        qualifier = clazz.name,
        shortName = clazz.simpleName
    )

    constructor(name: String) : this(
        qualifier = name,
        shortName = name.substringAfterLast(".")
    )

    init {
        require(qualifier.isNotBlank()) { "Name is Blanc" }
    }

    fun applyTo(mock: KSName) {
        every { mock.asString() } returns qualifier
        every { mock.getQualifier() } returns qualifier
        every { mock.getShortName() } returns shortName
    }

    /**
     * Builds and returns a mocked [KSName] using the configured values.
     *
     * @return A [KSName] mock with behavior defined by this builder.
     */
    fun build(): KSName = mockk<KSName>().also { applyTo(it) }
}