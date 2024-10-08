import java.io.FileInputStream
import java.util.Properties

plugins {
    alias(libs.plugins.androidApplication)
    alias(libs.plugins.jetbrainsKotlinAndroid)
    id("kotlin-kapt")
    id("kotlin-parcelize")
    //firebase sdk
    id("com.google.gms.google-services")
    alias(libs.plugins.googleFirebaseCrashlytics)
}

android {
    namespace = "com.nomorelateness.donotlate"
    compileSdk = 34

    val properties = Properties()
    properties.load(FileInputStream(rootProject.file("local.properties")))

    defaultConfig {
        applicationId = "com.nomorelateness.donotlate"
        minSdk = 31
        targetSdk = 34
        versionCode = 1
        versionName = "1.0"
        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildTypes {
        debug {
            isMinifyEnabled = false
            buildConfigField("String", "API_KEY", properties.getProperty("api_key_debug"))
            resValue("string", "API_KEY", properties.getProperty("api_key_debug"))
        }
        release {
            buildConfigField("String", "API_KEY", properties.getProperty("api_key_release"))
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
            signingConfig = signingConfigs.getByName("debug")
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
        buildConfig = true
    }
}

dependencies {
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.material)
    implementation(libs.androidx.activity)
    implementation(libs.androidx.constraintlayout)
    implementation(libs.google.material)
    implementation(libs.androidx.navigation.fragment)
    implementation(libs.google.material)
    implementation(libs.androidx.navigation.runtime.ktx)
    implementation(libs.firebase.crashlytics)
    testImplementation(libs.junit)
    implementation(libs.androidx.fragment.ktx)
    implementation(libs.androidx.activity.ktx)
    implementation(libs.androidx.fragment)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    implementation(libs.androidx.fragment.ktx.v136)
    implementation(libs.androidx.fragment.ktx.v171)

    //coil
    implementation(libs.coil)

    // firebase - firestore, FCM
    implementation(libs.firebase.firestore)
    implementation(libs.firebase.storage)
    implementation(libs.firebase.messaging)
    implementation(libs.firebase.auth)
    implementation(libs.google.firebase.auth.ktx)

    //firebase bom
    implementation(platform(libs.firebase.bom))
    implementation(libs.firebase.analytics)
    implementation(libs.google.firebase.crashlytics)


    //viewpager2
    implementation(libs.androidx.viewpager2)
    implementation(libs.dotsindicator)

    //recyclerview
    implementation(libs.androidx.recyclerview)

    //retrofit
    implementation(libs.retrofit)
    implementation(libs.converter.gson)
    implementation(libs.okhttp)
    implementation(libs.logging.interceptor)

    //room
    implementation(libs.room.runtime)
    implementation(libs.androidx.room.paging)
    kapt(libs.room.compiler)

    //google map
    implementation(libs.play.services.maps)
    implementation(libs.play.services.location)

    //json
    implementation(libs.gson)

    //google places
    implementation(platform(libs.kotlin.bom))
    implementation(libs.places)
    implementation(libs.volley)

    //프로필 이미지뷰
    implementation(libs.circleimageview)

    //polyline?
    implementation(libs.android.maps.utils)

    //스켈레톤 UI
    implementation("com.facebook.shimmer:shimmer:0.5.0")

    //lottie 애니메이션
    implementation("com.airbnb.android:lottie:5.0.2")

    implementation(libs.androidx.core.ktx.v170)
    implementation(libs.androidx.appcompat.v141)
    implementation(libs.material.v150)
    implementation(libs.androidx.lifecycle.viewmodel.ktx)
    implementation(libs.androidx.lifecycle.livedata.ktx)
    implementation(libs.androidx.fragment.ktx.v141)
    implementation(libs.androidx.viewbinding)

    implementation(libs.gson.v288)

}




