package ru.lubimobile.flyway.mocks

import com.google.devtools.ksp.symbol.KSAnnotated
import com.google.devtools.ksp.symbol.KSAnnotation
import io.mockk.every
import io.mockk.mockk

open class MockKSAnnotatedBuilder : MockKSNodeBuilder() {
    var annotations: MutableList<KSAnnotation> = mutableListOf()
        private set

    open fun withAnnotations(value: MutableList<KSAnnotation>) = apply { annotations = value }

    open fun addAnnotation(value: KSAnnotation) = apply { annotations.add(value) }
    open fun addAnnotation(value: String) = apply { annotations.add(MockKSAnnotationBuilder(value).build()) }
    open fun addAnnotation(clazz: Class<*>) = apply { annotations.add(MockKSAnnotationBuilder(clazz).build()) }

    open fun applyTo(mock: KSAnnotated) = apply {
        super.applyTo(mock)
        every { mock.annotations } returns annotations.asSequence()
    }

    override fun build(): KSAnnotated = mockk<KSAnnotated>().also { applyTo(it) }
}