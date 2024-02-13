@Suppress("DSL_SCOPE_VIOLATION") // TODO: Remove once KTIJ-19369 is fixed
plugins {
    id(libs.plugins.android.library.get().pluginId)
    id(libs.plugins.kotlin.android.get().pluginId)
    id(libs.plugins.kotlin.kapt.get().pluginId)
}

apply<CommonGradlePlugin>()

android {
    namespace = "team.retum.design_system"

    buildFeatures {
        compose = true
    }
    composeOptions {
        kotlinCompilerExtensionVersion = ProjectProperties.COMPOSE_COMPILER_EXTENSION
    }
}

dependencies {
    implementation(libs.androidx.compose.foundation)
    implementation(libs.androidx.compose.material3)
    implementation(libs.androidx.compose.ui)
    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.com.google.android.material)

    androidTestApi(libs.androidx.compose.ui.test.junit4)
}
