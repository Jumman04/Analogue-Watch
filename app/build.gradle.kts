plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.jetbrains.kotlin.android)
}

android {
    namespace = "com.jummania.analogue_watch"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.jummania.analogue_watch"
        minSdk = 21
        targetSdk = 34
        versionCode = 4
        versionName = "4.0"

        resValue("string", "versionName", versionName.toString())
        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildTypes {
        release {
            isMinifyEnabled = true
            isShrinkResources = true
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
    kotlinOptions {
        jvmTarget = "1.8"
    }

    buildFeatures {
        viewBinding = true
    }
}

dependencies {
    implementation(libs.androidx.core.ktx)
    implementation(libs.material)
    implementation(libs.neumorphism)
    implementation(libs.androidx.preference)
    implementation (libs.android.colorpickerpreference)
    implementation(project(":AnalogClock"))
}