plugins {
    id(libs.plugins.android.library.get().pluginId)
    id(libs.plugins.kotlin.android.get().pluginId)
    id(libs.plugins.kotlin.ksp.get().pluginId)
}

apply<CommonGradlePlugin>()
apply<ComposeGradlePlugin>()

android {
    namespace = "team.retum.employment"
}

dependencies {

}
