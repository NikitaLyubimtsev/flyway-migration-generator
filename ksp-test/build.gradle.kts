plugins {
    id("java")
    alias(libs.plugins.kotlin.jvm)
    alias(libs.plugins.ksp)
}

group = "ru.lubimobile"
version = "unspecified"

repositories {
    mavenCentral()
}

dependencies {
    ksp(project(":ksp"))
    implementation(project(":api"))

    testImplementation(platform("org.junit:junit-bom:5.10.0"))
    testImplementation("org.junit.jupiter:junit-jupiter")
    testImplementation(libs.kotlin.test)
}

ksp {
    arg("run_flyway_generator", "true")
    arg("migration_version", "V1.3_init")
    arg("output_dir", "$projectDir/src/main/resources/flyway")
}

tasks.register("generateFlywayMigration") {
    group = "flyway"
    description = "Runs KSP processor to generate Flyway migration"

    dependsOn("kspKotlin")
}