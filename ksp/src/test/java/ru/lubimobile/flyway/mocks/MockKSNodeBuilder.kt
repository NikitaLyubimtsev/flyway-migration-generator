package ru.lubimobile.flyway.mocks

import com.google.devtools.ksp.symbol.KSNode
import com.google.devtools.ksp.symbol.Location
import com.google.devtools.ksp.symbol.NonExistLocation
import com.google.devtools.ksp.symbol.Origin
import io.mockk.every
import io.mockk.mockk

open  class MockKSNodeBuilder {
    var origin: Origin = Origin.KOTLIN
        private set
    var location: Location = NonExistLocation
        private set
    var parent: KSNode? = null
        private set

    open fun withOrigin(value: Origin) = apply { origin = value }
    open fun withLocation(value: Location) = apply { location = value }
    open fun withParent(value: KSNode) = apply { parent = value }

    fun applyTo(mock: KSNode) {
        every { mock.origin } returns origin
        every { mock.location } returns location
        every { mock.parent } returns parent
    }

    open fun build(): KSNode = mockk<KSNode>().also { applyTo(it) }
}
