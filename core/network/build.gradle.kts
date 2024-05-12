import java.util.Properties

@Suppress("DSL_SCOPE_VIOLATION") // TODO: Remove once KTIJ-19369 is fixed
plugins {
    id(libs.plugins.android.library.get().pluginId)
    id(libs.plugins.kotlin.android.get().pluginId)
    id(libs.plugins.kotlin.kapt.get().pluginId)
}

apply<CommonGradlePlugin>()

val properties = Properties()
properties.load(rootProject.file("./local.properties").inputStream())

android {
    namespace = "team.retum.network"

    buildFeatures {
        buildConfig = true
    }

    buildTypes {
        release {
            buildConfigField(
                type = "String",
                name = "BASE_URL",
                value = properties.getProperty("BASE_URL_PROD", "\"\""),
            )
        }
        debug {
            buildConfigField(
                type = "String",
                name = "BASE_URL",
                value = properties.getProperty("BASE_URL_DEV", "\"\""),
            )
        }
    }
    kotlinOptions {
        jvmTarget = ProjectProperties.JVM_TARGET
    }
}

dependencies {

    implementation(project(":core:common"))
    implementation(project(":core:local"))

    implementation(libs.squareup.retrofit)
    implementation(libs.converter.moshi)
    implementation(libs.moshi.kotlin)
    implementation(libs.logging.interceptor)
}
