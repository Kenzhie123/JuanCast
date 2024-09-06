plugins {
    id("com.android.application")
    id("com.google.gms.google-services")
    id("org.jetbrains.kotlin.android")
}

android {
    namespace = "com.JuanCast.myapplication"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.JuanCast.myapplication"
        minSdk = 24
        targetSdk = 34
        versionCode = 40
        versionName = "1.0"
        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildFeatures {
        viewBinding = true
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

    packagingOptions {
        exclude("META-INF/NOTICE.md")
        exclude("META-INF/LICENSE.md")
        exclude("META-INF/DEPENDENCIES")
        exclude("META-INF/ASL2.0")
        exclude("META-INF/LGPL2.1")
    }
}


dependencies {

        implementation ("com.google.android.gms:play-services-ads:23.2.0")


        implementation("androidx.appcompat:appcompat:1.3.1")
        implementation("com.google.android.material:material:1.4.0")
        implementation("androidx.constraintlayout:constraintlayout:2.1.0")
        implementation("com.google.firebase:firebase-auth:21.0.1")
        implementation("com.google.firebase:firebase-firestore:23.0.3")
        implementation("com.google.firebase:firebase-database:20.0.3")
        implementation("com.google.firebase:firebase-storage:20.0.0")
    implementation(libs.androidx.activity)
    implementation(libs.androidx.lifecycle.livedata.ktx)
    implementation(libs.androidx.lifecycle.viewmodel.ktx)
    testImplementation("junit:junit:4.13.2")
        androidTestImplementation("androidx.test.ext:junit:1.1.3")
        androidTestImplementation("androidx.test.espresso:espresso-core:3.4.0")

        implementation("com.firebaseui:firebase-ui-firestore:7.2.0")
        implementation("com.firebaseui:firebase-ui-database:8.0.0")
        implementation("com.github.bumptech.glide:glide:4.12.0")
        annotationProcessor("com.github.bumptech.glide:compiler:4.12.0")
        implementation("com.google.android.gms:play-services-auth:20.5.0")
        implementation("com.etebarian:meow-bottom-navigation:1.2.0")
        implementation("com.github.denzcoskun:ImageSlideshow:0.1.0")
        implementation("androidx.recyclerview:recyclerview:1.2.1")


        implementation("de.hdodenhof:circleimageview:3.1.0")
        implementation ("androidx.media:media:1.5.0")

    implementation ("com.google.android.material:material:1.6.0")
    implementation ("androidx.navigation:navigation-fragment-ktx:2.6.0")


    implementation ("androidx.swiperefreshlayout:swiperefreshlayout:1.2.0-alpha01")


    implementation ("com.sun.mail:android-mail:1.6.6")
    implementation ("com.sun.mail:android-activation:1.6.7")

    val billing_version = "7.0.0"
    implementation("com.android.billingclient:billing-ktx:$billing_version")


    // Firebase BOM (Bill of Materials)
    implementation(enforcedPlatform("com.google.firebase:firebase-bom:32.0.0"))

    // WorkManager
    implementation ("androidx.work:work-runtime:2.8.0")









}

