package ru.lubimobile.flyway.mocks

import com.google.devtools.ksp.symbol.KSReferenceElement
import com.google.devtools.ksp.symbol.KSType
import com.google.devtools.ksp.symbol.KSTypeReference
import io.mockk.every
import io.mockk.mockk

class MockKSTypeReferenceBuilder private constructor (
    val resolvedType: KSType
) : MockKSAnnotatedBuilder() {
    private var element: KSReferenceElement? = null

    constructor(clazz: Class<*>) : this(
        resolvedType = MockKSTypeBuilder(clazz).build()
    )

    constructor(name: String) : this(
        resolvedType = MockKSTypeBuilder(name).build()
    )

    fun withReferenceElement(value: KSReferenceElement?) = apply { this.element = value }

    fun applyTo(mock: KSTypeReference) = apply {
        super.applyTo(mock)
        every { mock.element } returns element
        every { mock.resolve() } returns resolvedType
    }

    override fun build(): KSTypeReference = mockk<KSTypeReference>().also { applyTo(it) }
}