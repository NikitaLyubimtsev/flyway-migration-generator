package ru.lubimobile.flyway.mocks

import com.google.devtools.ksp.symbol.KSName
import com.google.devtools.ksp.symbol.KSValueArgument
import io.mockk.every
import io.mockk.mockk

class MockKSValueArgumentBuilder private constructor(
    val name: KSName,
    val value: Any?
) : MockKSAnnotatedBuilder() {
    var isSpread: Boolean = false
        private set

    constructor(name: String, value: Any?) : this(
        name = MockKSNameBuilder(name).build(),
        value = value
    )

    fun isSpread(value: Boolean) = apply { this.isSpread = value }

    fun applyTo(mock: KSValueArgument) {
        super.applyTo(mock)
        every { mock.name } returns name
        every { mock.value } returns value
        every { mock.isSpread } returns isSpread
    }

    override fun build(): KSValueArgument = mockk<KSValueArgument>().also { applyTo(it) }
}