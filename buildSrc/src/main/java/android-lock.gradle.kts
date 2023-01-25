@file:Suppress("UnstableApiUsage")

import org.jetbrains.kotlin.gradle.plugin.KotlinPlatformType

plugins {
    id("nebula.dependency-lock")
}

configurations.forEach { configuration: Configuration ->
    if (configuration.name.endsWith("DependenciesMetadata")) {
        if (configuration.name in listOf(
                "apiDependenciesMetadata",
                "apiElements",
                "runtimeElements"
            )
        ) {
            configuration.attributes.attribute(KotlinPlatformType.attribute, KotlinPlatformType.jvm)
        } else {
            configuration.isCanBeResolved = false
        }
    }
}
