@file:Suppress("UnstableApiUsage")

import org.jetbrains.kotlin.gradle.plugin.getKotlinPluginVersion

plugins {
    id("com.android.application")
    id("kotlin-android")
    id("maven-publish")
    id("android-version")
    id("android-lock")
    id("android-doc")
    id("artifactory")
}

android {
    compileSdk = BuildConfig.compileSdk

    defaultConfig {
        minSdk = BuildConfig.minSdk
        maxSdk = BuildConfig.maxSdk
        versionCode = (1 + androidGitVersion.code())
        versionName = androidGitVersion.name()
        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    androidResources {
        aaptOptions.additionalParameters(
            "-I",
            "$buildDir/intermediates/tmp/package-framework.apk"
        )
        noCompress("dex", "so")
    }

    buildTypes {
        debug {
            isPseudoLocalesEnabled = true
            isTestCoverageEnabled = true
            isDebuggable = true
            isMinifyEnabled = false
            isShrinkResources = false
        }
        release {
            isPseudoLocalesEnabled = false
            isTestCoverageEnabled = false
            isDebuggable = false
            isMinifyEnabled = true
            isShrinkResources = true

            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro"
            )
        }
    }

    compileOptions {
        sourceCompatibility = BuildConfig.sourceCompatibility
        targetCompatibility = BuildConfig.sourceCompatibility
    }

    kotlinOptions {
        jvmTarget = BuildConfig.jvmTarget
    }
}

configurations.create("framework")

dependencies {
    implementation("org.jetbrains.kotlin:kotlin-stdlib:$kotlinVersion")
    "framework"("com.renault.car.ui:package-framework:0.0.+")
}

tasks.register<Copy>("downloadFramework") {
    // force download on every call
    outputs.upToDateWhen { false }
    from(configurations["framework"])
    into("$buildDir/intermediates/tmp/")
    rename("(.*)package-framework(.*)", "package-framework.apk")
}

tasks.whenTaskAdded {
    // Download package-framework.apk even if not used in debug
    // W/A due to aaptOptions set at configuration time while build type (debug or release) is unknown
    if (this.name.contains("processDebugResources") ||
        this.name.contains("processReleaseResources")
    ) {
        this.dependsOn(tasks.getByName("downloadFramework"))
    }
}

afterEvaluate {
    publishing {
        publications {
            // https://developer.android.com/studio/build/maven-publish-plugin
            create<MavenPublication>("bundle") {
                groupId = "${project.group}"
                artifactId = project.name
                version = androidGitVersion.name()
                from(components.getByName("release_aab"))
                pom.licenses {
                    license {
                        name.set("PROPRIETARY")
                        distribution.set("repo")
                        comments.set(file(rootProject.file("LICENSE")).readText())
                    }
                }
            }
            create<MavenPublication>("release") {
                groupId = "${project.group}"
                artifactId = "${project.name}-release"
                version = androidGitVersion.name()
                from(components.getByName("release_apk"))
                pom.licenses {
                    license {
                        name.set("PROPRIETARY")
                        distribution.set("repo")
                        comments.set(file(rootProject.file("LICENSE")).readText())
                    }
                }
            }
            create<MavenPublication>("debug") {
                groupId = "${project.group}"
                artifactId = "${project.name}-debug"
                version = androidGitVersion.name()
                from(components.getByName("debug_apk"))
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
            publications(publishing.publications["bundle"])
            publications(publishing.publications["debug"])
            publications(publishing.publications["release"])
        }
    }
}
