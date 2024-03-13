plugins {
    `kotlin-dsl`
}

repositories {
    google()
    mavenCentral()
}

dependencies {
    implementation(libs.squareup.javapoet)
    implementation(libs.kotlin.gradle.plugin)
    implementation(libs.android.gradle)
}
