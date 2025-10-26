@Suppress("DSL_SCOPE_VIOLATION") // TODO: Remove once KTIJ-19369 is fixed
plugins {
    id(libs.plugins.android.library.get().pluginId)
    id(libs.plugins.kotlin.android.get().pluginId)
    id(libs.plugins.kotlin.ksp.get().pluginId)
    id(libs.plugins.hilt.android.get().pluginId)
    id(libs.plugins.ktlint.gradle.get().pluginId)
    id(libs.plugins.kotlinx.serialization.get().pluginId)
}

apply<CommonGradlePlugin>()
apply<ComposeGradlePlugin>()

android {
    namespace = "team.retum.post_review"

    kotlinOptions {
        jvmTarget = ProjectProperties.JVM_TARGET
    }
}

dependencies {
    implementation(libs.kotlinx.collections.immutable)
    implementation(libs.kotlinx.serialization.json)
}
