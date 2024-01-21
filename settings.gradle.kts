pluginManagement {
    repositories {
        google()
        mavenCentral()
        gradlePluginPortal()
    }
}
dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        google()
        mavenCentral()
        maven("https://jitpack.io")
    }
}

rootProject.name = "JobisAndroidV2"
include(":app")
include(":feature")
include(":core")
include(":core:domain")
include(":core:data")
include(":core:network")
include(":core:common")
include(":feature:landing")
