package ru.lubimobile.flyway.mocks

import com.google.devtools.ksp.symbol.KSReferenceElement
import com.google.devtools.ksp.symbol.KSTypeArgument
import io.mockk.every
import io.mockk.mockk

open class MockKSReferenceElementBuilder : MockKSNodeBuilder() {

    var typeArguments: List<KSTypeArgument> = emptyList()
        private set

    open fun withTypeArguments(value: List<KSTypeArgument>) = apply { typeArguments = value }

    open fun applyTo(mock: KSReferenceElement) = apply {
        super.applyTo(mock)
        every { mock.typeArguments } returns typeArguments
    }

    override fun build(): KSReferenceElement = mockk<KSReferenceElement>().also { applyTo(it) }
}