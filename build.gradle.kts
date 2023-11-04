buildscript {
    dependencies {
        classpath("com.google.gms:google-services:4.4.0")
    }
}

// Top-level build file where you can add configuration options common to all sub-projects/modules.
plugins {
    kotlin("android") version "1.9.10" apply false
    kotlin("jvm") version "1.9.10" apply false
    id("com.android.application") version "8.0.1" apply false
    id("com.android.library") version "8.0.1" apply false
    id("com.google.dagger.hilt.android") version "2.48.1" apply false
}