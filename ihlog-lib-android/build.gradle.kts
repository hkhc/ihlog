/*
 * Copyright (c) 2021. Herman Cheung
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 *
 */

plugins {
    id("com.android.library")
    id("kotlin-android")
    id("kotlin-kapt")
    // "io.hkhc.jarbird" must be after "com.android.library"
    // so that libraryVariants is configured before jarbird
    id("io.hkhc.jarbird-android")
    id("org.jetbrains.dokka")
//    id("digital.wup.android-maven-publish") version "3.6.2"
    id("org.jlleitschuh.gradle.ktlint")
    id("io.gitlab.arturbosch.detekt")
    // for build script debugging
    id("org.barfuin.gradle.taskinfo")
}

tasks {
//    dokka {
//        outputFormat = "html"
//        outputDirectory = "$buildDir/dokka"
//        // set true to omit intra-project dependencies
//        disableAutoconfiguration = true
//    }
}

java {
    sourceCompatibility = JavaVersion.VERSION_1_8
    targetCompatibility = JavaVersion.VERSION_1_8
}

android {
    compileSdkVersion(30)

    sourceSets {
        named("main") {
            java.srcDirs("src/main/java", "src/main/kotlin")
        }
        named("release") {
            java.srcDirs("src/release/java", "src/release/kotlin")
        }
    }

    defaultConfig {
        minSdkVersion(21)
        targetSdkVersion(30)
        versionCode = 1
        versionName = "1.0"
        testInstrumentationRunner = "android.support.test.runner.AndroidJUnitRunner"
    }

    buildTypes {
        getByName("release") {
            isMinifyEnabled = false
            proguardFiles(getDefaultProguardFile("proguard-android.txt"), "proguard-rules.pro")
        }
        getByName("debug") {
            isMinifyEnabled = false
        }
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }

    kotlinOptions {
        jvmTarget = "1.8"
    }

    testOptions {
        with(unitTests) {
            isIncludeAndroidResources = true
            isReturnDefaultValues = true
        }
    }
}

android.libraryVariants.configureEach {
    val variantName = name
    val variant = this

    if (variantName == "release") {
        jarbird {
            pub(variantName) {
                useGpg = true
                mavenCentral()
                from(variant)
            }
        }
    }
}

dependencies {

    api(project(":ihlog"))
    implementation(Kotlin.stdlib.jdk8)

    testImplementation(Testing.junit4)
    testImplementation("org.assertj:assertj-core:_")
    testImplementation(Testing.roboElectric)
    testImplementation(Testing.mockK)
}
