package ru.lubimobile.flyway.mocks

import com.google.devtools.ksp.symbol.ClassKind
import com.google.devtools.ksp.symbol.KSClassDeclaration
import com.google.devtools.ksp.symbol.KSFunctionDeclaration
import com.google.devtools.ksp.symbol.KSPropertyDeclaration
import com.google.devtools.ksp.symbol.KSTypeReference
import io.mockk.every
import io.mockk.mockk

class MockKSClassDeclarationBuilder : MockKSDeclarationBuilder {
    var classKind: ClassKind = ClassKind.CLASS
        private set
    var primaryConstructor: KSFunctionDeclaration? = null
        private set
    var superTypes: MutableList<KSTypeReference> = mutableListOf()
        private set
    var isCompanionObject: Boolean = false
        private set

    private var sealedSubclasses: MutableList<KSClassDeclaration> = mutableListOf()
    private var allFunctions: MutableList<KSFunctionDeclaration> = mutableListOf()
    private var allProperties: MutableList<KSPropertyDeclaration> = mutableListOf()
//    private var asTypeBuilder: ((List<KSTypeArgument>) -> KSType)? = null
    //private var starProjectedType: KSType

    constructor(clazz: Class<*>) : super(clazz)
    constructor(name: String) : super(name)

    fun withClassKind(value: ClassKind) = apply { this.classKind = value }
    fun withPrimaryConstructor(value: KSFunctionDeclaration?) = apply { this.primaryConstructor = value }
    fun withSuperTypes(value: MutableList<KSTypeReference>) = apply { this.superTypes = value }
    fun isCompanionObject(value: Boolean) = apply { this.isCompanionObject = value }
    fun withSealedSubclasses(value: MutableList<KSClassDeclaration>) = apply { this.sealedSubclasses = value }
    fun allFunctions(value: MutableList<KSFunctionDeclaration>) = apply { this.allFunctions = value }
    fun allProperties(value: MutableList<KSPropertyDeclaration>) = apply { this.allProperties = value }
//    fun withTypeBuilder(block: (List<KSTypeArgument>) -> KSType) = apply { this.asTypeBuilder = block }
//    fun withStarProjectedType(value: KSType) = apply { this.starProjectedType = value }


    fun applyTo(mock: KSClassDeclaration) = apply {
        super.applyTo(mock)
        every { mock.classKind } returns classKind
        every { mock.primaryConstructor } returns primaryConstructor
        every { mock.superTypes } returns superTypes.asSequence()
        every { mock.isCompanionObject } returns isCompanionObject
        every { mock.getSealedSubclasses() } returns sealedSubclasses.asSequence()
        every { mock.getAllFunctions() } returns allFunctions.asSequence()
        every { mock.getAllProperties() } returns allProperties.asSequence()
//        every { mock.asType(any()) } answers {
//            asTypeBuilder?.invoke(firstArg()) ?: mock.declarations
//        }
//        every { mock.asStarProjectedType() } returns starProjectedType
    }

    override fun build(): KSClassDeclaration = mockk<KSClassDeclaration>().also { applyTo(it) }
}