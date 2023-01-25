@file:Suppress("UnstableApiUsage")

plugins {
    id("org.jetbrains.dokka")
}

tasks.dokkaHtml.configure {
    dokkaSourceSets {
        named("main") {
            noAndroidSdkLink.set(false)
        }
    }
}

tasks.dokkaGfm.configure {
    dokkaSourceSets {
        named("main") {
            noAndroidSdkLink.set(false)
        }

    }
}
