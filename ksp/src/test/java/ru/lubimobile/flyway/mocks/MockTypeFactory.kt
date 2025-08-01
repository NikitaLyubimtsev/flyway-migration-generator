package ru.lubimobile.flyway.mocks

import com.google.devtools.ksp.symbol.KSType

object MockTypeFactory {

    val kotlinString: KSType = MockKSTypeBuilder(String::class.java).build()
}