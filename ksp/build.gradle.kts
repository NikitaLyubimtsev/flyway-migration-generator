plugins {
    id("java")
    alias(libs.plugins.kotlin.jvm)
    alias(libs.plugins.ksp)
    `java-library`
    `maven-publish`
}

group = "ru.lubimobile"
version = "0.0.1-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {
    implementation(project(":api"))
    implementation(libs.symbol.processing.api)
    implementation(libs.kotlin.stdlib)

    testImplementation(platform("org.junit:junit-bom:5.10.0"))
    testImplementation("org.junit.jupiter:junit-jupiter")
    testImplementation(libs.kotlin.test)
}



tasks.test {
    useJUnitPlatform()
}