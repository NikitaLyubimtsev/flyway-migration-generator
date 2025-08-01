package ru.lubimobile.flyway.mocks

import com.google.devtools.ksp.symbol.AnnotationUseSiteTarget
import com.google.devtools.ksp.symbol.KSAnnotation
import com.google.devtools.ksp.symbol.KSName
import com.google.devtools.ksp.symbol.KSTypeReference
import com.google.devtools.ksp.symbol.KSValueArgument
import io.mockk.every
import io.mockk.mockk

class MockKSAnnotationBuilder private constructor(
    val annotationType: KSTypeReference,
    val shortName: KSName
): MockKSNodeBuilder() {

    var arguments: MutableList<KSValueArgument> = mutableListOf()
        private set
    var defaultArguments: MutableList<KSValueArgument> = mutableListOf()
        private set
    var useSiteTarget: AnnotationUseSiteTarget? = null
        private set

    constructor(clazz: Class<*>) : this(
        shortName = MockKSNameBuilder(clazz.simpleName).build(),
        annotationType = MockKSTypeReferenceBuilder(clazz).build()
    ) {
        require(clazz.isAnnotation) { "Not eat Instance Annotation class $clazz" }
    }

    constructor(name: String): this(
        shortName = MockKSNameBuilder(name.substringAfterLast(".")).build(),
        annotationType = MockKSTypeReferenceBuilder(name).build()
    ) {
        require(name.isNotBlank()) { "annotationName must not be blank" }
    }

    fun withArguments(value: List<KSValueArgument>) = apply { arguments = value.toMutableList() }
    fun addArgument(name: String, value: Any) = apply { arguments.add(MockKSValueArgumentBuilder(name, value).build()) }
    fun withDefaultArguments(value: List<KSValueArgument>) = apply { this.defaultArguments = value.toMutableList() }
    fun userSiteTarget(value: AnnotationUseSiteTarget) = apply { this.useSiteTarget = value }

    fun applyTo(mock: KSAnnotation) = apply {
        super.applyTo(mock)

        every { mock.annotationType } returns annotationType
        every { mock.arguments } returns arguments
        every { mock.defaultArguments } returns defaultArguments
        every { mock.shortName } returns shortName
        every { mock.useSiteTarget } returns useSiteTarget
    }

    override fun build(): KSAnnotation = mockk<KSAnnotation>().also { applyTo(it) }
}