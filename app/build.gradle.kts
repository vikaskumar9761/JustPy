import org.apache.tools.ant.util.JavaEnvUtils.VERSION_1_8
import org.gradle.api.JavaVersion


plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
}

android {
    namespace = "com.justpe.justpy1"
    compileSdk = 35

    buildFeatures{
        viewBinding=true
        dataBinding=true
    }

    sourceSets {
        getByName("main") {
            res {
                srcDirs(

                    "src\\main\\res\\layout\\hotels",

                    )
            }
        }
    }


    defaultConfig {
        applicationId = "com.justpe.justpy1"
        minSdk = 24
        targetSdk = 35
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
            sourceCompatibility = JavaVersion.VERSION_11
            targetCompatibility = JavaVersion.VERSION_11
        }


    kotlinOptions {
        jvmTarget = "11"
    }
}

dependencies {

    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.material)
    implementation(libs.androidx.activity)
    implementation(libs.androidx.constraintlayout)
    implementation(libs.androidx.navigation.fragment.ktx)
    implementation(libs.androidx.navigation.ui.ktx)
    implementation(libs.androidx.room.ktx)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)

    val koin_version="2.1.6";
//    val retrofitVersion = "2.9.0"
//    val okhttpVersion = "4.9.0"
    val glideVersion = "4.16.0"
    implementation ("io.insert-koin:koin-core:$koin_version")
    implementation ("io.insert-koin:koin-core-ext:$koin_version")
    implementation ("io.insert-koin:koin-androidx-scope:$koin_version")
    implementation ("io.insert-koin:koin-androidx-viewmodel:$koin_version")
    implementation("androidx.security:security-crypto:1.0.0")
    implementation ("com.airbnb.android:lottie:3.4.0")

    implementation ("com.squareup.retrofit2:retrofit:2.9.0")
    implementation ("com.squareup.retrofit2:converter-gson:2.9.0")
    implementation ("com.squareup.okhttp3:logging-interceptor:4.9.3")

//    implementation ("com.squareup.retrofit2:retrofit:$retrofitVersion")
//    implementation ("com.squareup.retrofit2:converter-gson:$retrofitVersion")
//    implementation ("com.squareup.okhttp3:logging-interceptor:$okhttpVersion")
    
    implementation ("com.jakewharton.retrofit:retrofit2-kotlin-coroutines-adapter:0.9.2")
    implementation ("org.jetbrains.kotlinx:kotlinx-coroutines-android:1.6.1")
    implementation ("androidx.legacy:legacy-support-v4:1.0.0")
    implementation ("com.google.android.material:material:1.1.0-alpha09")

  //  implementation("com.github.ozcanalasalvar:otpview:2.0.1")
    implementation ("com.google.android.play:integrity:1.3.0")

 //   implementation ("phonepe.intentsdk.android.release:IntentSDK:2.4.3")

    implementation ("com.airbnb.android:lottie:3.4.0")
    implementation ("com.github.bumptech.glide:glide:$glideVersion")
    annotationProcessor ("com.github.bumptech.glide:compiler:$glideVersion")
    implementation(platform("com.google.firebase:firebase-bom:33.1.2"))

    implementation("com.google.android.play:app-update:2.1.0")

    implementation("com.google.android.flexbox:flexbox:3.0.0")
    // For Kotlin users also import the Kotlin extensions library for Play In-App Update:
    implementation("com.google.android.play:app-update-ktx:2.1.0")

    implementation(platform("com.google.firebase:firebase-bom:33.1.2"))

    // Add the dependencies for the App Check libraries
    // When using the BoM, you don't specify versions in Firebase library dependencies
    implementation("com.google.firebase:firebase-appcheck-debug")

    // Add the dependencies for the Crashlytics and Analytics libraries
    // When using the BoM, you don't specify versions in Firebase library dependencies
    implementation("com.google.firebase:firebase-crashlytics")
    implementation("com.google.firebase:firebase-analytics")
    implementation(platform("com.google.firebase:firebase-bom:33.0.0"))
    implementation("com.google.firebase:firebase-analytics")
    implementation ("com.squareup.retrofit2:converter-scalars:2.5.0")
    implementation ("com.google.android.gms:play-services-auth:19.0.0")
    implementation ("com.google.android.gms:play-services-auth-api-phone:17.5.0")
//add slider
    implementation ("com.github.denzcoskun:ImageSlideshow:0.1.2")
}