plugins {
    id("com.android.application")
    id("org.jetbrains.kotlin.android")
    id("com.google.devtools.ksp")
}

android {
    namespace = "com.example.vinillos_app_misw"
    compileSdk = 35

    dataBinding {
        enable = true
    }

    defaultConfig {
        applicationId = "com.example.vinillos_app_misw"
        minSdk = 21
        targetSdk = 34
        versionCode = 1
        versionName = "1.0"
        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildTypes {
        release {
            isMinifyEnabled = false
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
}

dependencies {
    implementation ("androidx.room:room-ktx:2.6.1")
    ksp ("androidx.room:room-compiler:2.6.1")
    androidTestImplementation ("androidx.room:room-testing:2.6.1")
    implementation("androidx.core:core-ktx:1.15.0")
    implementation("androidx.appcompat:appcompat:1.7.0")
    implementation("com.google.android.material:material:1.12.0")
    implementation( "com.android.volley:volley:1.2.1")
    implementation("androidx.constraintlayout:constraintlayout:2.2.0")
    implementation("com.caverock:androidsvg:1.4")
    implementation( "com.google.code.gson:gson:2.10.1")
    implementation ("org.jetbrains.kotlinx:kotlinx-coroutines-android:1.9.0")
    implementation("androidx.activity:activity-ktx:1.9.3")
    implementation("androidx.lifecycle:lifecycle-viewmodel-ktx:2.8.7")
    testImplementation("junit:junit:4.13.2")
    testImplementation ("io.mockk:mockk:1.12.0")
    testImplementation ("org.jetbrains.kotlinx:kotlinx-coroutines-test:1.7.1")
    testImplementation( "androidx.arch.core:core-testing:2.1.0")
    debugImplementation ("androidx.test:runner:1.6.1")
    debugImplementation ("androidx.test:core:1.6.1")
    debugImplementation("androidx.test.ext:junit:1.2.1")
    debugImplementation("androidx.test.espresso:espresso-core:3.6.1")
    debugImplementation("androidx.compose.ui:ui-test-manifest:1.7.5")
    debugImplementation("androidx.test:rules:1.6.1")
}