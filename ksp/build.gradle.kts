plugins {
    id("java")
    alias(libs.plugins.kotlin.jvm)
    alias(libs.plugins.ksp)
    `java-library`
    `maven-publish`
}

//group = "ru.lubimobile"
//version = project.findProperty("version").toString()

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

publishing {
    repositories {
        maven {
            name = "GitHubPackages"
            url = uri("https://maven.pkg.github.com/NikitaLyubimtsev/flyway-migration-generator")
            credentials {
                username = project.findProperty("gpr.key") as String? ?: System.getenv("USER")
                password = project.findProperty("gpr.key") as String? ?: System.getenv("GPR_TOKEN")
            }
        }
    }
    publications {
        register<MavenPublication>("grp") {
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