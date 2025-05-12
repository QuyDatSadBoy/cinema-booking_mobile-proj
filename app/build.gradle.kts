plugins {
    alias(libs.plugins.android.application)
}

android {
    namespace = "com.example.cinema_booking_mobile"
    compileSdk = 35

    defaultConfig {
        applicationId = "com.example.cinema_booking_mobile"
        minSdk =26
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
}

dependencies {

    implementation(libs.appcompat)
    implementation(libs.material)
    implementation(libs.activity)
    implementation(libs.constraintlayout)
    implementation(libs.annotation)
    implementation(libs.lifecycle.livedata.ktx)
    implementation(libs.lifecycle.viewmodel.ktx)
    testImplementation(libs.junit)
    androidTestImplementation(libs.ext.junit)
    androidTestImplementation(libs.espresso.core)

// Retrofit
    implementation(libs.retrofit)
    implementation(libs.okhttp3.logging.interceptor)
    implementation(libs.retrofit.converter.gson)
    implementation(libs.gson)

// Picasso
    implementation(libs.picasso)

// Firebase
    implementation(platform(libs.firebase.bom))
    implementation(libs.firebase.storage)

// Google
    implementation(libs.play.services.auth)

// RxJava
    implementation(libs.rxjava)
    implementation(libs.rxandroid)

    implementation(libs.circleimageview)

    // OkHttp cho networking
    implementation("com.squareup.okhttp3:okhttp:4.9.1")
    implementation("com.squareup.okhttp3:logging-interceptor:4.9.1")

    // Picasso cho load ảnh
    implementation("com.squareup.picasso:picasso:2.71828")

    implementation ("com.github.bumptech.glide:glide:4.16.0")

}