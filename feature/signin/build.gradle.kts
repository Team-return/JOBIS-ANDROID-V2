import java.util.Properties

@Suppress("DSL_SCOPE_VIOLATION") // TODO: Remove once KTIJ-19369 is fixed
plugins {
    id(libs.plugins.android.library.get().pluginId)
    id(libs.plugins.kotlin.android.get().pluginId)
    id(libs.plugins.hilt.android.get().pluginId)
    id(libs.plugins.kotlin.kapt.get().pluginId)
}

apply<CommonGradlePlugin>()
apply<ComposeGradlePlugin>()

val properties = Properties()
properties.load(rootProject.file("./local.properties").inputStream())

android {
    namespace = "team.retum.signin"

    sourceSets["main"].manifest.srcFile("src/debug/java/AndroidManifest.xml")

    buildTypes {
        debug {
            isMinifyEnabled = false
            buildConfigField(
                type = "String",
                name = "TEST_EMAIL",
                value = properties.getProperty("TEST_EMAIL", "\"test\""),
            )
            buildConfigField(
                type = "String",
                name = "TEST_PASSWORD",
                value = properties.getProperty("TEST_PASSWORD", "\"test\"")
            )
        }
    }

    buildFeatures {
        buildConfig = true
    }
}
