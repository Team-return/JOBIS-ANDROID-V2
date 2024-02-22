@Suppress("DSL_SCOPE_VIOLATION") // TODO: Remove once KTIJ-19369 is fixed
plugins {
    id(libs.plugins.android.library.get().pluginId)
    id(libs.plugins.kotlin.android.get().pluginId)
    id(libs.plugins.kotlin.kapt.get().pluginId)
}

apply<CommonGradlePlugin>()

android {
    namespace = "team.retum.common"
    buildFeatures {
        compose = true
    }
    composeOptions {
        kotlinCompilerExtensionVersion = ProjectProperties.COMPOSE_COMPILER_EXTENSION
    }
}

dependencies {

    implementation(project(":core:design-system"))

    implementation(libs.androidx.lifecycle.viewmodel)
    implementation(libs.androidx.compose.foundation)
    implementation(libs.androidx.compose.material3)
}
