package ru.lubimobile.flyway.mocks

import com.google.devtools.ksp.symbol.KSAnnotation

object MockAnnotationFactory {

    fun columnAnnotation(name: String = "", nullable: Boolean = true, unique: Boolean = false, defaultValue: String = "", length: Int = 0, precision: Int = 0, scale: Int = 0): KSAnnotation =
        MockKSAnnotationBuilder(ru.lubimobile.annotation.Column::class.java)
            .addArgument("name", name)
            .addArgument("nullable", nullable)
            .addArgument("unique", unique)
            .addArgument("defaultValue", defaultValue)
            .addArgument("length", length)
            .addArgument("precision", precision)
            .addArgument("scale", scale)
        .build()


}