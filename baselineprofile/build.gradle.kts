import com.android.build.api.dsl.ManagedVirtualDevice

plugins {
    id(libs.plugins.android.test.get().pluginId)
    id(libs.plugins.kotlin.android.get().pluginId)
    id(libs.plugins.baselineprofile.get().pluginId)
}

android {
    namespace = "team.retum.jobis.baselineprofile"
    compileSdk = 34

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_18
        targetCompatibility = JavaVersion.VERSION_18
    }

    kotlinOptions {
        jvmTarget = "18"
    }

    defaultConfig {
        minSdk = 28
        targetSdk = 34

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    targetProjectPath = ":app"

    testOptions.managedDevices.devices {
        create<ManagedVirtualDevice>("pixel6api33") {
            device = "Pixel 6"
            apiLevel = 33
            systemImageSource = "aosp"
        }
    }

}

// This is the configuration block for the Baseline Profile plugin.
// You can specify to run the generators on a managed devices or connected devices.
baselineProfile {
    managedDevices += "pixel6api33"
    useConnectedDevices = true
}

dependencies {
    implementation(libs.androidx.junit)
    implementation(libs.androidx.espresso.core)
    implementation(libs.androidx.uiautomator)
    implementation(libs.androidx.benchmark.macro.junit4)
}
