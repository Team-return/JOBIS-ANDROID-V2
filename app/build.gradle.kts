import com.github.triplet.gradle.androidpublisher.ReleaseStatus

plugins {
    id(libs.plugins.android.application.get().pluginId)
    id(libs.plugins.kotlin.android.get().pluginId)
    id(libs.plugins.hilt.android.get().pluginId)
    id(libs.plugins.kotlin.kapt.get().pluginId)
    id(libs.plugins.ktlint.gradle.get().pluginId)
    id(libs.plugins.google.service.get().pluginId)
    id(libs.plugins.firebase.crashlytics.get().pluginId)
    id(libs.plugins.triplet.play.get().pluginId)
}

android {
    namespace = "team.retum.jobisandroidv2"
    compileSdk = ProjectProperties.COMPILE_SDK

    defaultConfig {
        applicationId = "team.retum.jobisandroidv2"
        minSdk = ProjectProperties.MIN_SDK
        targetSdk = ProjectProperties.TARGET_SDK
        versionCode = ProjectProperties.VERSION_CODE
        versionName = ProjectProperties.VERSION_NAME

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        vectorDrawables {
            useSupportLibrary = true
        }
    }

    signingConfigs {
        create("release") {
            storeFile = file("/home/runner/work/JOBIS-ANDROID-V2/JOBIS-ANDROID-V2/keystore/jobis_v2_key.jks")
            storePassword = System.getenv("SIGNING_STORE_PASSWORD")
            keyAlias = System.getenv("SIGNING_KEY_ALIAS")
            keyPassword = System.getenv("SIGNING_KEY_PASSWORD")
        }
    }

    hilt {
        enableAggregatingTask = true
    }

    buildTypes {
        release {
            signingConfig = signingConfigs.getByName("release")
            isMinifyEnabled = true
            isShrinkResources = true
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro",
            )
        }
        debug {
            splits.abi.isEnable = false
            aaptOptions.cruncherEnabled = false
            splits.density.isEnable = false
            aaptOptions.cruncherEnabled = false
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_18
        targetCompatibility = JavaVersion.VERSION_18
    }
    kotlinOptions {
        jvmTarget = ProjectProperties.JVM_TARGET
    }
    buildFeatures {
        compose = true
    }
    composeOptions {
        kotlinCompilerExtensionVersion = ProjectProperties.COMPOSE_COMPILER_EXTENSION
    }
    packaging {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
    }
}

play {
    serviceAccountCredentials.set(file("src/main/play/google-cloud-platform.json"))
    defaultToAppBundles.set(true)
    releaseStatus.set(ReleaseStatus.IN_PROGRESS)
    track.set("production")
}

tasks.register("release") {
    dependsOn(tasks["clean"])
    dependsOn(tasks["bundleRelease"])
    mustRunAfter(tasks["clean"])
}

dependencies {

    implementation(project(":core:common"))
    implementation(project(":core:design-system"))
    implementation(project((":feature:splash")))
    implementation(project(":feature:landing"))
    implementation(project(":feature:signin"))
    implementation(project(":feature:signup"))
    implementation(project(":feature:home"))
    implementation(project(":feature:notification"))
    implementation(project(":feature:recruitment"))
    implementation(project(":feature:bookmark"))
    implementation(project(":feature:bug"))
    implementation(project(":feature:mypage"))
    implementation(project(":feature:interests"))
    implementation(project(":feature:change-password"))
    implementation(project(":feature:verify-email"))
    implementation(project(":feature:notice"))
    implementation(project(":feature:company"))
    implementation(project(":feature:review"))
    implementation(project(":feature:application"))

    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.navigation.compose)

    implementation(platform(libs.firebase.bom))
    implementation(libs.firebase.messaging)
    implementation(libs.firebase.crashlytics)
    implementation(libs.firebase.analytics)

    implementation(libs.hilt.android)
    kapt(libs.hilt.android.compiler)

    api(libs.androidx.compose.ui)
    api(libs.androidx.compose.ui.graphics)
    api(libs.androidx.compose.ui.tooling.preview)
    api(libs.androidx.compose.material3)
    api(libs.androidx.compose.material)
    api(libs.com.google.android.play)

    testApi(libs.junit)

    androidTestApi(libs.androidx.junit)
    androidTestApi(libs.androidx.espresso.core)
    androidTestApi(libs.androidx.compose.ui.test.junit4)
    androidTestImplementation(platform(libs.androidx.compose.bom))

    debugApi(libs.androidx.compose.ui.tooling)
    debugApi(libs.androidx.compose.ui.test.manifest)

    implementation(libs.anrwatchdog)
}

kapt {
    javacOptions {
        option("-Adagger.fastInit=ENABLED")
        option("-Adagger.hilt.android.internal.disableAndroidSuperclassValidation=true")
    }
}
