@Suppress("DSL_SCOPE_VIOLATION") // TODO: Remove once KTIJ-19369 is fixed
plugins {
    id(libs.plugins.androidLibrary.get().pluginId)
    id(libs.plugins.kotlin.android.get().pluginId)
}

apply<CommonGradlePlugin>()

android {
    namespace = "team.retum.domain"
}
