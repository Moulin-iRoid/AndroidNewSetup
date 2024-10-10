plugins {
    id("kotlin-kapt")
    alias(libs.plugins.android.application)
    alias(libs.plugins.jetbrains.kotlin.android)
    id("kotlin-parcelize")
}

android {
    namespace = "com.example.androidnewsetup"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.example.androidnewsetup"
        minSdk = 26
        targetSdk = 34
        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildTypes {
        release {
            isDebuggable = false
            isMinifyEnabled = false
            proguardFiles(getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro")
            buildConfigField("boolean", "EnableAnim", "true")
        }
        debug {
            isDebuggable = true
            buildConfigField("boolean", "EnableAnim", "true")
        }
    }

    flavorDimensions += listOf("default")
    productFlavors {
        create("local") {
            versionCode = 1
            versionName = "1.0.1"
            buildConfigField("String", "BASE_URL", "\"https://dev.iroidsolutions.com:4007/api/v1/\"") //please put your respective base url
        }
        create("production") {
            versionCode = 1
            versionName = "1.0.1"
            buildConfigField("String", "BASE_URL", "\"https://dev.iroidsolutions.com:4007/api/v1/\"") //please put your respective base url
        }
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }
    kotlinOptions {
        jvmTarget = "17"
    }
    buildFeatures {
        buildConfig = true
        viewBinding = true
        dataBinding = true
    }
}

dependencies {

    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.material)
    implementation(libs.androidx.activity)
    implementation(libs.androidx.constraintlayout)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)

    implementation(libs.androidx.lifecycle.livedata.ktx)
    implementation(libs.androidx.lifecycle.viewmodel.ktx)
    implementation(libs.androidx.paging.common.ktx)
    implementation(libs.androidx.paging.runtime.ktx)

    implementation(libs.androidx.activity.ktx)
    implementation(libs.androidx.fragment.ktx)

    //Navigation
    implementation(libs.androidx.navigation.fragment.ktx)
    implementation(libs.androidx.navigation.ui.ktx)

    //Crash and Analytics
    implementation(libs.firebase.crashlytics.ktx)
    implementation(libs.firebase.analytics.ktx)

    //Retrofit
    implementation(libs.retrofit)
    implementation(libs.converter.gson)
    implementation(libs.logging.interceptor)
    implementation(libs.retrofit.gson)

    //Prefs
    implementation(libs.preference)
    implementation(libs.permission)

    //Coroutines
    implementation(libs.retrofit2.kotlin.coroutines.adapter)
    implementation(libs.kotlinx.coroutines.android)

    //RxJava
    implementation(libs.rxjava)
    implementation(libs.rxandroid)

    //Sdp Ssp
    implementation(libs.sdp.android)
    implementation(libs.ssp.android)

    //Recyclerview
    implementation(libs.androidx.recyclerview)

    //Glide
    implementation(libs.glide)

    //coil for image loading
    implementation(libs.coil)

    //swipe refresh layout
    implementation(libs.androidx.swiperefreshlayout)

    //testfairy
    implementation("com.testfairy:testfairy-android-sdk:1.+@aar")

    //Cookie bar
    implementation(libs.cookiebar2)

    //Loader
    implementation(libs.circularprogressview)
}