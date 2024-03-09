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
include(":core:design-system")
include(":core:domain")
include(":core:data")
include(":core:network")
include(":core:common")
include(":feature:landing")
include(":feature:signin")
include(":feature:signup")
include(":feature:home")
include(":feature:notification")
include(":feature:bookmark")
include(":feature:recruitment")
include(":feature:bug")
include(":feature:mypage")
include(":feature:interests")
include(":feature:change-password")
include(":feature:verify-email")
include(":feature:notice")
include(":feature:company")
include(":core:local")
include(":feature:review")
include(":feature:splash")
