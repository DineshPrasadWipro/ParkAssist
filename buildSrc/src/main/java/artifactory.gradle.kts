@file:Suppress("LocalVariableName")

plugins {
    id("com.jfrog.artifactory")
}

artifactory {
    val artifactory_url: String? by rootProject.extra
    val artifactory_repository: String? by rootProject.extra
    val artifactory_user: String? by rootProject.extra
    val artifactory_password: String? by rootProject.extra

    publish {
        setContextUrl("$artifactory_url")

        repository {
            setRepoKey("$artifactory_repository") // The repository key to publish to
            setUsername("$artifactory_user") // The publisher user name
            setPassword("$artifactory_password") // The publisher password
            setMavenCompatible(true)
        }
    }
}
