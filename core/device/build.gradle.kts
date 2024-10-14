plugins {
    id(libs.plugins.android.library.get().pluginId)
    id(libs.plugins.kotlin.android.get().pluginId)
    id(libs.plugins.kotlin.ksp.get().pluginId)
    id(libs.plugins.ktlint.gradle.get().pluginId)
}

apply<CommonGradlePlugin>()

dependencies {
    implementation(project(":core:design-system"))
    implementation(project(":core:domain"))
    implementation(project(":core:common"))

    implementation(platform(libs.firebase.bom))
    implementation(libs.firebase.analytics.ktx)
    implementation(libs.firebase.messaging)
}

android {
    namespace = "team.retum.device"

    kotlinOptions {
        jvmTarget = ProjectProperties.JVM_TARGET
    }
}
