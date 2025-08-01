plugins {
    id("java")
    alias(libs.plugins.kotlin.jvm)
    alias(libs.plugins.ksp)
    alias(libs.plugins.kotlin.serialization)
    `java-library`
    `maven-publish`
}

repositories {
    mavenCentral()
}

val testAgent by configurations.creating

dependencies {
    implementation(project(":api"))
    implementation(libs.symbol.processing.api)
    implementation(libs.kotlin.stdlib)
    implementation(libs.kotlinx.datetime)
    implementation(libs.kotlinx.serialization.json)
    implementation(libs.kotlin.logging.jvm)

    runtimeOnly(libs.slf4j.simple)

    testImplementation(libs.mockk)
    testImplementation(libs.junit.jupiter)
    testImplementation(libs.junit.jupiter.params)
    testImplementation(libs.kotlin.test)

    testAgent(libs.byte.buddy.agent)
}

publishing {
    repositories {
        maven {
            name = "GitHubPackages"
            url = uri("https://maven.pkg.github.com/NikitaLyubimtsev/flyway-migration-generator")
            credentials {
                username = System.getenv("GITHUB_ACTOR")
                password = System.getenv("GITHUB_TOKEN")
            }
        }
    }
    publications {
        register<MavenPublication>("ksp-publication") {
            from(components["java"])

            groupId = project.group.toString()
            artifactId = "flyway-migration-generator-ksp"
            version = project.version.toString()

            pom {
                name.set("Flyway Migration Generator")
                description.set("Automatic generation flyway migration from Kotlin and Java Entity, marking annotation Table")
                url.set("https://https://github.com/NikitaLyubimtsev/flyway-migration-generator")

                licenses {
                    license {
                        name.set("Apache-2.0 license")
                        url.set("http://www.apache.org/licenses/")
                    }
                }
                developers {
                    developer {
                        id.set("nikitalyubimtsev")
                        name.set("Nikita Lyubimtsev")
                        email.set("lyubimtsevn.a@yandex.ru")
                    }
                }
                scm {
                    connection.set("scm:git:git://github.com/NikitaLyubimtsev/flyway-migration-generator.git")
                    developerConnection.set("scm:git:ssh://github.com:NikitaLyubimtsev/flyway-migration-generator.git")
                    url.set("https://github.com/NikitaLyubimtsev/flyway-migration-generator")
                }
            }
        }
    }
}

tasks.test {
    useJUnitPlatform()
}

testing {
    suites {
        @Suppress("UnstableApiUsage") val test by getting(JvmTestSuite::class) {
            useJUnitJupiter("5.10.1")
            dependencies {
                implementation(libs.mockk)
            }
            targets.configureEach {
                testTask.configure {
                    jvmArgs(testAgent.files.map { "-javaagent:${it.absolutePath}" }) // comment this out to see warning
                }
            }
            targets {
                all {
                    testTask.configure {
                        jvmArgs("-Xshare:off")
                    }
                }
            }
        }
    }
}