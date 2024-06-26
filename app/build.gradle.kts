import java.io.FileInputStream
import java.util.Properties

plugins {
    alias(libs.plugins.androidApplication)
    alias(libs.plugins.jetbrainsKotlinAndroid)
    id("kotlin-kapt")
    id("kotlin-parcelize")
    //firebase sdk
    id("com.google.gms.google-services")
}

android {
    namespace = "com.example.donotlate"
    compileSdk = 34

    val properties = Properties()
    properties.load(FileInputStream(rootProject.file("local.properties")))

    defaultConfig {
        applicationId = "com.example.donotlate"
        minSdk = 31
        targetSdk = 34
        versionCode = 1
        versionName = "1.0"
        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        buildConfigField("String", "API_KEY", properties.getProperty("api_key"))
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
    implementation("com.google.maps.android:android-maps-utils:2.2.3")


}




