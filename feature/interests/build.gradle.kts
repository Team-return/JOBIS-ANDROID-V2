@Suppress("DSL_SCOPE_VIOLATION") // TODO: Remove once KTIJ-19369 is fixed
plugins {
    id(libs.plugins.android.library.get().pluginId)
    id(libs.plugins.kotlin.android.get().pluginId)
    id(libs.plugins.kotlin.ksp.get().pluginId)
    id(libs.plugins.hilt.android.get().pluginId)
    id(libs.plugins.ktlint.gradle.get().pluginId)
}

apply<CommonGradlePlugin>()
apply<ComposeGradlePlugin>()

android {
    namespace = "team.retum.jobis.interests"

    kotlinOptions {
        jvmTarget = ProjectProperties.JVM_TARGET
    }
}

dependencies {
    implementation(project(":core:common"))
    implementation(project(":core:domain"))
    implementation(project(":core:network"))

    implementation(libs.kotlinx.collections.immutable)
}
