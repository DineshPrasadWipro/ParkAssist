plugins {
    id("com.android.library")
    id("kotlin-android")
    id("android-version")
    id("android-lock")
    id("android-doc")
}

android {
    compileSdk = BuildConfig.compileSdk

    defaultConfig {
        minSdk = BuildConfig.minSdk
        targetSdk = BuildConfig.maxSdk
        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        consumerProguardFiles("consumer-rules.pro")
    }

    androidResources {
        noCompress("dex", "so")
    }

    buildTypes {
        debug {
            isTestCoverageEnabled = true
            isPseudoLocalesEnabled = true
        }
        release {
            isTestCoverageEnabled = false
            isPseudoLocalesEnabled = false

            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro"
            )
        }
    }

    compileOptions {
        sourceCompatibility = BuildConfig.sourceCompatibility
        targetCompatibility = BuildConfig.targetCompatibility
    }

    kotlinOptions {
        jvmTarget = BuildConfig.jvmTarget
    }
}

dependencies {
    implementation("org.jetbrains.kotlin:kotlin-stdlib:$kotlinVersion")
}

