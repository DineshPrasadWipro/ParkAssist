@file:Suppress("LocalVariableName")

plugins {
    id("android-module")
    id("maven-publish")
    id("artifactory")
}

val androidSourcesJar = tasks.register<Jar>("androidSourcesJar") {
    archiveClassifier.set("source")
    from(android.sourceSets.getByName("main").java.srcDirs)
}

afterEvaluate {
    publishing {
        publications {
            // https://developer.android.com/studio/build/maven-publish-plugin
            create<MavenPublication>("release") {
                groupId = "${project.group}"
                artifactId = project.name
                version = androidGitVersion.name()
                from(components.getByName("release"))
                artifact(androidSourcesJar)
                pom.licenses {
                    license {
                        name.set("PROPRIETARY")
                        distribution.set("repo")
                        comments.set(file(rootProject.file("LICENSE")).readText())
                    }
                }
            }
        }
    }

    tasks {
        artifactoryPublish {
            publications(publishing.publications["release"])
        }
    }
}
