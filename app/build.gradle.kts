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

    defaultConfig {
        applicationId = "com.example.donotlate"
        minSdk = 24
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
    buildFeatures {
        viewBinding = true
    }

}

dependencies {

    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.material)
    implementation(libs.androidx.activity)
    implementation(libs.androidx.constraintlayout)
    implementation(libs.google.material)

    // firebase - firestore, FCM
    implementation(libs.firebase.firestore)
    implementation(libs.firebase.storage)
    implementation(libs.firebase.messaging)
    implementation(libs.firebase.auth)
    implementation(libs.androidx.fragment.ktx)
    implementation(libs.androidx.activity.ktx)

    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)

    //viewpager2
    implementation(libs.androidx.viewpager2)
    implementation("com.tbuonomo:dotsindicator:5.0")

    //recyclerview
    implementation(libs.androidx.recyclerview)

    //fragment
    implementation(libs.androidx.fragment)

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
    implementation ("com.google.android.gms:play-services-maps:18.1.0")
    implementation ("com.google.android.gms:play-services-location:21.0.1")

    //chip
    implementation("com.google.android.material:material:1.4.0")

    //json
    implementation("com.google.code.gson:gson:2.8.9")

    //google places
    implementation(platform("org.jetbrains.kotlin:kotlin-bom:1.9.0"))
    implementation ("com.google.android.libraries.places:places:3.3.0")
    implementation(libs.volley)

    //firebase bom
    implementation(platform("com.google.firebase:firebase-bom:33.1.0"))
    implementation("com.google.firebase:firebase-analytics")

    implementation("com.google.firebase:firebase-auth-ktx")

    implementation ("androidx.fragment:fragment-ktx:1.3.6")

}
