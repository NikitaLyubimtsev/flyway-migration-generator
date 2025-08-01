package ru.lubimobile.flyway.mocks

import com.google.devtools.ksp.symbol.KSAnnotation
import com.google.devtools.ksp.symbol.KSPropertyDeclaration
import com.google.devtools.ksp.symbol.KSPropertyGetter
import com.google.devtools.ksp.symbol.KSPropertySetter
import com.google.devtools.ksp.symbol.KSType
import com.google.devtools.ksp.symbol.KSTypeReference
import io.mockk.every
import io.mockk.mockk

class MockKSPropertyDeclarationBuilder private constructor(
    name: String,
    val type: KSTypeReference
) : MockKSDeclarationBuilder(name) {

    var getter: KSPropertyGetter? = null
        private set
    var setter: KSPropertySetter? = null
        private set

    var extensionReceiver: KSTypeReference? = null
        private set
    var isMutable: Boolean = true
        private set
    var hasBackingField: Boolean = false
        private set
    var isDelegated: Boolean = false
        private set
    var override: KSPropertyDeclaration? = null
        private set
    var memberTypeResolver: ((KSType) -> KSType)? = null
        private set

    constructor(name: String, typeClazz: Class<*>) : this(
        name = name,
        type = MockKSTypeReferenceBuilder(typeClazz).build()
    )

    constructor(name: String, typeName: String) : this(
        name = name,
        type = MockKSTypeReferenceBuilder(typeName).build()
    )

    override fun withAnnotations(value: MutableList<KSAnnotation>) = apply { super.withAnnotations(value) }
    override fun addAnnotation(value: KSAnnotation) = apply { super.addAnnotation(value) }
    override fun addAnnotation(clazz: Class<*>) = apply { super.addAnnotation(clazz) }
    override fun addAnnotation(value: String) = apply { super.addAnnotation(value) }

    fun withGetter(value: KSPropertyGetter) = apply { this.getter = value }
    fun withSetter(value: KSPropertySetter) = apply { this.setter = value }
    fun withExtensionReceiver(clazz: Class<*>) = apply { this.extensionReceiver = MockKSTypeReferenceBuilder(clazz).build() }
    fun withExtensionReceiver(name: String) = apply { this.extensionReceiver = MockKSTypeReferenceBuilder(name).build() }
    fun isMutable(value: Boolean) = apply { this.isMutable = value }
    fun hasBackingField(value: Boolean) = apply { this.hasBackingField = value }
    fun isDelegated(value: Boolean) = apply { this.isDelegated = value }
    fun withOverride(value: KSPropertyDeclaration) = apply { this.override = value }

    fun applyTo(mock: KSPropertyDeclaration) = apply {
        super.applyTo(mock)
        every { mock.getter } returns getter
        every { mock.setter } returns setter
        every { mock.type } returns type
        every { mock.extensionReceiver } returns extensionReceiver
        every { mock.isMutable } returns isMutable
        every { mock.hasBackingField } returns hasBackingField
        every { mock.isDelegated() } returns isDelegated
        every { mock.findOverridee() } returns override
        every { mock.asMemberOf(any()) } answers {
            memberTypeResolver?.invoke(firstArg()) ?: mock.type.resolve()
        }
    }

    override fun build(): KSPropertyDeclaration = mockk<KSPropertyDeclaration>().also { applyTo(it) }
}