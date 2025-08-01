package ru.lubimobile.flyway.mocks

import com.google.devtools.ksp.symbol.KSAnnotation
import com.google.devtools.ksp.symbol.KSDeclaration
import com.google.devtools.ksp.symbol.KSFile
import com.google.devtools.ksp.symbol.KSName
import com.google.devtools.ksp.symbol.KSTypeParameter
import com.google.devtools.ksp.symbol.Origin
import io.mockk.every
import io.mockk.mockk

open class MockKSDeclarationBuilder(
    val simpleName: KSName,
    val packageName: KSName,
    val qualifiedName: KSName? = null
) : MockKSAnnotatedBuilder() {

    var typeParameters: MutableList<KSTypeParameter> = mutableListOf()
        private set
    var parentDeclaration: KSDeclaration? = null
        private set
    var containingFile: KSFile? = null
        private set
    var docString: String? = null
        private set

    constructor(clazz: Class<*>) : this(
        simpleName = MockKSNameBuilder(clazz.simpleName).build(),
        packageName = MockKSNameBuilder(clazz.`package`?.name ?: "").build(),
        qualifiedName = MockKSNameBuilder(clazz).build()
    )

    constructor(name: String) : this(
        simpleName = MockKSNameBuilder(name.substringAfterLast(".")).build(),
        packageName = MockKSNameBuilder(name.substringBeforeLast(".")).build(),
        qualifiedName = MockKSNameBuilder(name).build()
    )

    override fun withAnnotations(value: MutableList<KSAnnotation>) = apply { super.withAnnotations(value) }
    override fun addAnnotation(clazz: Class<*>) = apply { super.addAnnotation(clazz) }
    override fun addAnnotation(value: String) = apply { super.addAnnotation(value) }
    override fun withOrigin(value: Origin) = apply { super.withOrigin(value) }

    open fun withParentDeclaration(value: KSDeclaration?) = apply { this.parentDeclaration = value }
    open fun withContainingFile(value: KSFile?) = apply { this.containingFile = value }
    open fun withTypeParameters(value: MutableList<KSTypeParameter>) = apply { this.typeParameters = value }
    open fun withDocString(value: String?) = apply { this.docString = value }

    open fun applyTo(mock: KSDeclaration) = apply {
        super.applyTo(mock)
        every { mock.simpleName } returns simpleName
        every { mock.qualifiedName } returns qualifiedName
        every { mock.packageName } returns packageName
        every { mock.parentDeclaration } returns parentDeclaration
        every { mock.containingFile } returns containingFile
        every { mock.typeParameters } returns typeParameters
        every { mock.docString } returns docString
    }

    override fun build(): KSDeclaration = mockk<KSDeclaration>().also { applyTo(it) }
}