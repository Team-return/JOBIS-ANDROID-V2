buildscript {
    repositories {
        google()
        mavenCentral()
    }
    dependencies {
        classpath(libs.kotlin.serialization)
        classpath(libs.play.publisher)
    }
}

plugins {
    alias(libs.plugins.hilt.android) apply false
    alias(libs.plugins.ktlint.gradle) apply false
    alias(libs.plugins.google.service) apply false
    alias(libs.plugins.firebase.crashlytics) apply false
}
