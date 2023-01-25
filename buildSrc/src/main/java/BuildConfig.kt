import org.gradle.api.JavaVersion
import org.gradle.api.Project
import org.jetbrains.kotlin.gradle.plugin.getKotlinPluginVersion

object BuildConfig {
    const val compileSdk = 31
    const val minSdk = 29
    const val maxSdk = 31
    val sourceCompatibility = JavaVersion.VERSION_1_8
    val targetCompatibility = JavaVersion.VERSION_1_8
    val jvmTarget = targetCompatibility.toString()
}

val Project.kotlinVersion get() = getKotlinPluginVersion()
