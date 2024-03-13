buildscript {
    repositories {
        google()
        mavenCentral()
    }
    dependencies {
        classpath(libs.kotlin.serialization)
    }
}

plugins {
    alias(libs.plugins.hilt.android) apply false
    alias(libs.plugins.ktlint.gradle) apply false
    alias(libs.plugins.google.service) apply false
}
