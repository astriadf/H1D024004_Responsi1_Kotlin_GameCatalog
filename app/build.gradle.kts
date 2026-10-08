import java.util.Properties

plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.compose)
}

android {
    namespace = "com.pemmob.astriadf.gamecatalog"
    compileSdk {
        version = release(37)
    }

    defaultConfig {
        applicationId = "com.pemmob.astriadf.gamecatalog"
        minSdk = 24
        targetSdk = 37
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"

        // Mengambil API Key RAWG dari local.properties
        val localProperties = Properties()
        val localPropertiesFile = rootProject.file("local.properties")

        if (localPropertiesFile.exists()) {
            localPropertiesFile.inputStream().use {
                localProperties.load(it)
            }
        }

        val rawgApiKey = localProperties.getProperty("RAWG_API_KEY") ?: ""

        // Membuat BuildConfig.RAWG_API_KEY
        buildConfigField(
            "String",
            "RAWG_API_KEY",
            "\"$rawgApiKey\""
        )
    }

    buildTypes {
        release {
            optimization {
                enable = false
            }
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
    buildFeatures {
        compose = true
        buildConfig = true
    }
}

dependencies {
    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.activity.compose)
    implementation(libs.androidx.compose.material3)
    implementation(libs.androidx.compose.ui)
    implementation(libs.androidx.compose.ui.graphics)
    implementation(libs.androidx.compose.ui.tooling.preview)
    implementation("androidx.compose.material:material-icons-extended")
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation("androidx.lifecycle:lifecycle-runtime-compose:2.11.0")
    implementation("androidx.navigation:navigation-compose:2.10.2") // Untuk berpindah Home Screen ke Detail Screen
    implementation("androidx.lifecycle:lifecycle-viewmodel-compose:2.11.0") // Untuk menerapkan arsitektur MVVM
    implementation("com.squareup.retrofit2:retrofit:3.0.0") // Untuk mengambil data dari REST API RAWG
    implementation("com.squareup.retrofit2:converter-gson:3.0.0") // Mengubah JSON dari RAWG menjadi object Kotlin
    implementation("com.squareup.okhttp3:logging-interceptor:5.5.0") // Membantu melihat request/response API saat proses development
    testImplementation(libs.junit)
    androidTestImplementation(platform(libs.androidx.compose.bom))
    androidTestImplementation(libs.androidx.compose.ui.test.junit4)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(libs.androidx.junit)
    debugImplementation(libs.androidx.compose.ui.test.manifest)
    debugImplementation(libs.androidx.compose.ui.tooling)
}