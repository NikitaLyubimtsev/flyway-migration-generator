package ru.lubimobile.flyway.mocks

import com.google.devtools.ksp.symbol.KSTypeArgument
import com.google.devtools.ksp.symbol.KSTypeReference
import com.google.devtools.ksp.symbol.Variance
import io.mockk.every
import io.mockk.mockk

class MockKSTypeArgumentBuilder(
    val variance: Variance = Variance.STAR,
    val type: KSTypeReference? = null
) : MockKSAnnotatedBuilder() {

    fun applyTo(mock: KSTypeArgument) = apply {
        super.applyTo(mock)
        every { mock.variance } returns variance
        every { mock.type } returns type
    }

    override fun build(): KSTypeArgument = mockk<KSTypeArgument>().also { applyTo(it) }
}