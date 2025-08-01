package ru.lubimobile.flyway.mocks

import com.google.devtools.ksp.symbol.KSFunction
import com.google.devtools.ksp.symbol.KSType
import com.google.devtools.ksp.symbol.KSTypeParameter
import io.mockk.every
import io.mockk.mockk

class MockKSFunctionBuilder {
    var returnType: KSType? = null
        private set
    var parameterTypes: MutableList<KSType?> = mutableListOf()
        private set
    var typeParameters: MutableList<KSTypeParameter> = mutableListOf()
        private set
    var extensionReceiverType: KSType? = null
        private set
    var isError: Boolean = false
        private set

    fun withReturnType(value: String) = apply { this.returnType = MockKSTypeBuilder(value).build() }
    fun withReturnType(value: Class<*>) = apply { this.returnType = MockKSTypeBuilder(value).build() }

    fun withParametersType(value: MutableList<KSType?>) = apply { this.parameterTypes = value }
    fun withTypeParameters(value: MutableList<KSTypeParameter>) = apply { this.typeParameters = value }

    fun withExtensionReceiverType(value: String) = apply { this.extensionReceiverType = MockKSTypeBuilder(value).build() }
    fun withExtensionReceiverType(value: Class<*>) = apply { this.extensionReceiverType = MockKSTypeBuilder(value).build() }

    fun isError(value: Boolean) = apply { this.isError = value }

    fun applyTo(mock: KSFunction) {
        every { mock.returnType } returns returnType
        every { mock.parameterTypes } returns parameterTypes
        every { mock.typeParameters } returns typeParameters
        every { mock.extensionReceiverType } returns extensionReceiverType
        every { mock.isError } returns isError
    }

    fun build(): KSFunction = mockk<KSFunction>().also { applyTo(it) }
}