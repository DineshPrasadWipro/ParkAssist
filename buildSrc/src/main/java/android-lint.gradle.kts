plugins {
    id("io.gitlab.arturbosch.detekt")
    id("com.diffplug.spotless")
}

spotless {
    isEnforceCheck = true

    java {
        target("**/*.java")
        googleJavaFormat().aosp()
        trimTrailingWhitespace()
        indentWithSpaces()
        endWithNewline()
    }
    kotlin {
        target("**/*.kt")

        ktlint().userData(mapOf(
            "indent_size" to "4",
            "continuation_indent_size" to "4")
        )
        trimTrailingWhitespace()
        indentWithSpaces()
        endWithNewline()
    }
}

detekt {
    baseline = file("$projectDir/detekt.xml")
    buildUponDefaultConfig = true
}

tasks.withType<io.gitlab.arturbosch.detekt.Detekt>().configureEach {
    // Target version of the generated JVM bytecode. It is used for type resolution.
    this.jvmTarget = "1.8"
}
