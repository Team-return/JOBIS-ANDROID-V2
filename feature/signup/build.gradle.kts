import java.util.Properties

@Suppress("DSL_SCOPE_VIOLATION") // TODO: Remove once KTIJ-19369 is fixed
plugins {
    id(libs.plugins.android.library.get().pluginId)
    id(libs.plugins.kotlin.android.get().pluginId)
    id(libs.plugins.kotlin.kapt.get().pluginId)
    id(libs.plugins.kotlinx.serialization.get().pluginId)
    id(libs.plugins.ktlint.gradle.get().pluginId)
}

apply<CommonGradlePlugin>()
apply<ComposeGradlePlugin>()

val properties = Properties()
properties.load(rootProject.file("./local.properties").inputStream())

android {
    namespace = "team.retum.signup"

    buildTypes {
        release {
            buildConfigField(
                type = "String",
                name = "TERMS_URL",
                value = properties.getProperty("TERMS_URL", ""),
            )
        }
        debug {
            buildConfigField(
                type = "String",
                name = "TERMS_URL",
                value = properties.getProperty("TERMS_URL", ""),
            )
        }
    }

    kotlinOptions {
        jvmTarget = ProjectProperties.JVM_TARGET
    }

    buildFeatures {
        buildConfig = true
    }
}

dependencies {
    implementation(libs.kotlin.stdlib)
    implementation(libs.kotlinx.serialization.json)
    implementation(libs.coil.compose)
}
