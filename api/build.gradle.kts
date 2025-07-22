plugins {
    id("java")
    `java-library`
}

group = "ru.lubimobile"
version = "0.0.1-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {
    api(libs.jakarta.persistence.api)
}