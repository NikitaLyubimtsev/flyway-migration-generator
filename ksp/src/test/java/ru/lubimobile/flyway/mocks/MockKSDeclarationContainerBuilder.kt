package ru.lubimobile.flyway.mocks

import com.google.devtools.ksp.symbol.KSDeclaration
import com.google.devtools.ksp.symbol.KSDeclarationContainer
import io.mockk.every
import io.mockk.mockk

open class MockKSDeclarationContainerBuilder : MockKSNodeBuilder() {

    var declarations: MutableList<KSDeclaration> = mutableListOf()
        private set

    open fun addDeclaration(value: KSDeclaration) = apply { declarations.add(value) }
    open fun withDeclarations(value: MutableList<KSDeclaration>) = apply { this.declarations = value }

    open fun applyTo(mock: KSDeclarationContainer) = apply {
        super.applyTo(mock)
        every { mock.declarations } returns declarations.asSequence()
    }

    override fun build(): KSDeclarationContainer = mockk<KSDeclarationContainer>().also { applyTo(it) }
}