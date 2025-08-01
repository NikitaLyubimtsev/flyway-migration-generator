plugins {
    id("java")
    `java-library`
    `maven-publish`
}

repositories {
    mavenCentral()
}

dependencies {
//    api(libs.jakarta.persistence.api)
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
        register<MavenPublication>("api-publication") {
            from(components["java"])

            groupId = project.group.toString()
            artifactId = "flyway-migration-generator-api"
            version = project.version.toString()

            pom {
                name.set("Flyway Migration Generator API")
                description.set("Provides an API for interacting with the Flyway migration generator KSP")
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