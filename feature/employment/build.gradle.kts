plugins {
    id(libs.plugins.android.library.get().pluginId)
    id(libs.plugins.kotlin.android.get().pluginId)
    id(libs.plugins.hilt.android.get().pluginId)
    id(libs.plugins.kotlin.ksp.get().pluginId)
    id(libs.plugins.ktlint.gradle.get().pluginId)
}

apply<CommonGradlePlugin>()
apply<ComposeGradlePlugin>()

android {
    namespace = "team.retum.employment"

    kotlinOptions {
        jvmTarget = ProjectProperties.JVM_TARGET
    }
}

dependencies {
    implementation(libs.kotlinx.collections.immutable)
    implementation(libs.coil.compose)
    implementation(libs.androidx.compose.ui.tooling.preview)
}
