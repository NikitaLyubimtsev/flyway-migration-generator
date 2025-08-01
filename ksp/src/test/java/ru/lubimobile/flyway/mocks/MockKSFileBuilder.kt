package ru.lubimobile.flyway.mocks

import com.google.devtools.ksp.symbol.KSFile
import com.google.devtools.ksp.symbol.KSName
import io.mockk.every
import io.mockk.mockk

open class MockKSFileBuilder private constructor(
    var packageName: KSName,
    var fileName: String
) : MockKSAnnotatedBuilder() {
    var filePath: String = ""
        private set

    constructor(fileName: String, packageName: String) : this(
        fileName = fileName,
        packageName = MockKSNameBuilder(packageName).build()
    )

    open fun withFilePath(value: String) = apply { this.filePath = value }

    open fun applyTo(mock: KSFile) = apply {
        super.applyTo(mock)
        every { mock.packageName } returns packageName
        every { mock.fileName } returns fileName
        every { mock.filePath } returns filePath
    }

    override fun build(): KSFile = mockk<KSFile>().also { applyTo(it) }
}