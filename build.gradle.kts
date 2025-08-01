allprojects {
    group = project.property("group").toString()
    version = project.property("version").toString()
}

tasks.register("cleanPublish") {
    group = "publishing"
    description = "Cleans the build and publishes ksp and api modules"

    dependsOn(":clean")
    dependsOn(":ksp:publish")
    dependsOn(":api:publish")
}