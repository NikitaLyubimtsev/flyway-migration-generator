package ru.lubimobile.flyway.mocks

import com.google.devtools.ksp.symbol.FunctionKind
import com.google.devtools.ksp.symbol.KSDeclaration
import com.google.devtools.ksp.symbol.KSFunction
import com.google.devtools.ksp.symbol.KSFunctionDeclaration
import com.google.devtools.ksp.symbol.KSType
import com.google.devtools.ksp.symbol.KSTypeReference
import com.google.devtools.ksp.symbol.KSValueParameter
import io.mockk.every
import io.mockk.mockk

class MockKSFunctionDeclarationBuilder(
    name: String
): MockKSDeclarationBuilder(name) {
    private var functionKind: FunctionKind = FunctionKind.MEMBER
    private var isAbstract: Boolean = false
    private var extensionReceiver: KSTypeReference? = null
    private var returnType: KSTypeReference? = null
    private var parameters: List<KSValueParameter> = emptyList()
    private var override: KSDeclaration? = null
    private var asMemberOfBuilder: ((KSType) -> KSFunction)? = null

    fun withFunctionKind(value: FunctionKind) = apply { this.functionKind = value }
    fun isAbstract(value: Boolean) = apply { this.isAbstract = value }
    fun withExtensionReceiver(value: KSTypeReference?) = apply { this.extensionReceiver = value }
    fun withReturnType(value: KSTypeReference?) = apply { this.returnType = value }
    fun withParameters(value: List<KSValueParameter>) = apply { this.parameters = value }
    fun withOverride(value: KSDeclaration?) = apply { this.override = value }
    fun asMemberOf(block: (KSType) -> KSFunction) = apply { asMemberOfBuilder = block }

    fun applyTo(mock: KSFunctionDeclaration) = apply {
        super.applyTo(mock)
        every { mock.functionKind } returns functionKind
        every { mock.isAbstract } returns isAbstract
        every { mock.extensionReceiver } returns extensionReceiver
        every { mock.returnType } returns returnType
        every { mock.parameters } returns parameters
        every { mock.findOverridee() } returns override
        every { mock.asMemberOf(any()) } answers { asMemberOfBuilder?.invoke(firstArg()) ?: MockKSFunctionBuilder().build() }
    }

    override fun build(): KSFunctionDeclaration = mockk<KSFunctionDeclaration>().also { applyTo(it) }
}