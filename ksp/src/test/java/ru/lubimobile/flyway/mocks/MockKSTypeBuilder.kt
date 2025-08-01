package ru.lubimobile.flyway.mocks

import com.google.devtools.ksp.symbol.KSAnnotation
import com.google.devtools.ksp.symbol.KSDeclaration
import com.google.devtools.ksp.symbol.KSType
import com.google.devtools.ksp.symbol.KSTypeArgument
import com.google.devtools.ksp.symbol.Nullability
import io.mockk.every
import io.mockk.mockk

class MockKSTypeBuilder private constructor(
    val declaration: KSDeclaration
) {

    var nullable: Nullability = Nullability.NULLABLE
        private set
    var arguments: MutableList<KSTypeArgument> = mutableListOf()
        private set
    var annotations: MutableList<KSAnnotation> = mutableListOf()
        private set
    var isMarkedNullable: Boolean = false
        private set
    var isError: Boolean = false
        private set
    var isFunctionType: Boolean = false
        private set
    var isSuspendFunctionType: Boolean = false
        private set

    private var assignableChecker: ((KSType) -> Boolean)? = null
    private var replacementBuilder: ((List<KSTypeArgument>) -> KSType)? = null
    private var starProjectionBuilder: (() -> KSType)? = null
    private var makeNullableBuilder: (() -> KSType)? = null
    private var makeNotNullableBuilder: (() -> KSType)? = null
    private var mutabilityFlexible: Boolean = false
    private var covarianceFlexible: Boolean = false

    constructor(clazz: Class<*>) : this(
        declaration = MockKSDeclarationBuilder(clazz).build()
    )

    constructor(name: String) : this(
        declaration = MockKSDeclarationBuilder(name).build()
    )

    fun withNullable(value: Nullability) = apply { this.nullable = value }
    fun withArguments(value: List<KSTypeArgument>) = apply { this.arguments = value.toMutableList() }
    fun addArgument(value: KSTypeArgument) = apply { this.arguments + value }
    fun withAnnotations(value: List<KSAnnotation>) = apply { this.annotations = value.toMutableList() }
    fun addAnnotations(value: KSAnnotation) = apply { this.annotations + value }

    fun isMarkedNullable(value: Boolean) = apply { this.isMarkedNullable = value }
    fun isError(value: Boolean) = apply { this.isError = value }
    fun isFunctionType(value: Boolean) = apply { this.isFunctionType = value }
    fun isSuspendFunctionType(value: Boolean) = apply { this.isSuspendFunctionType = value }

    fun withAssignableChecker(block: (KSType) -> Boolean) = apply { assignableChecker = block }
    fun isMutabilityFlexible(value: Boolean) = apply { mutabilityFlexible = value }
    fun isCovarianceFlexible(value: Boolean) = apply { covarianceFlexible = value }
    fun withReplacementBuilder(block: (List<KSTypeArgument>) -> KSType) = apply { replacementBuilder = block }
    fun withStarProjection(block: () -> KSType) = apply { starProjectionBuilder = block }
    fun withMakeNullable(block: () -> KSType) = apply { makeNullableBuilder = block }
    fun withMakeNotNullable(block: () -> KSType) = apply { makeNotNullableBuilder = block }



    fun applyTo(mock: KSType) {
        every { mock.declaration } returns declaration
        every { mock.nullability } returns nullable
        every { mock.arguments } returns arguments.toList()
        every { mock.annotations } returns annotations.asSequence()
        every { mock.isMarkedNullable } returns isMarkedNullable
        every { mock.isError } returns isError
        every { mock.isFunctionType } returns isFunctionType
        every { mock.isSuspendFunctionType } returns isSuspendFunctionType

        every { mock.isAssignableFrom(any()) } answers { assignableChecker?.invoke(firstArg()) ?: false }
        every { mock.isMutabilityFlexible() } returns mutabilityFlexible
        every { mock.isCovarianceFlexible() } returns covarianceFlexible
        every { mock.replace(any()) } answers { replacementBuilder?.invoke(firstArg()) ?: mock }
        every { mock.starProjection() } answers { starProjectionBuilder?.invoke() ?: mock }
        every { mock.makeNullable() } answers { makeNullableBuilder?.invoke() ?: mock }
        every { mock.makeNotNullable() } answers { makeNotNullableBuilder?.invoke() ?: mock }
    }

    fun build(): KSType = mockk<KSType>().also { applyTo(it) }
}
