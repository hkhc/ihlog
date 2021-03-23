/*
 * Copyright (c) 2019. Herman Cheung
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

buildscript {
    repositories {
        // needed by dokka
        jcenter()
    }
    dependencies {
    }
}

plugins {
    id("com.android.library")
    id("kotlin-android")
    id("kotlin-android-extensions")
    id("kotlin-kapt")
    // "io.hkhc.jarbird" must be after "com.android.library"
    // so that libraryVariants is configured before jarbird
    id("io.hkhc.jarbird")
    id("org.jetbrains.dokka")
//    id("digital.wup.android-maven-publish") version "3.6.2"
    id("org.jlleitschuh.gradle.ktlint") version "9.2.1"
    id("io.gitlab.arturbosch.detekt") version "1.5.1"
    // for build script debugging
    id("com.dorongold.task-tree") version "1.5"
}

repositories {
    jcenter()
}

tasks {
//    dokka {
//        outputFormat = "html"
//        outputDirectory = "$buildDir/dokka"
//        // set true to omit intra-project dependencies
//        disableAutoconfiguration = true
//    }
}


android {
    compileSdkVersion(28)

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
        targetSdkVersion(28)
        versionCode = 1
        versionName = "1.0"
        testInstrumentationRunner = "android.support.test.runner.AndroidJUnitRunner"
    }

    buildTypes {
        getByName("release") {
            isMinifyEnabled = true
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
                from(components[variantName])
                docSourceSets = variant
            }
        }
    }
}

dependencies {

//    dokkaHtmlPlugin("org.jetbrains.dokka:kotlin-as-java-plugin:1.4.10.2")
    api(project(":ihlog"))
    implementation(kotlin("stdlib-jdk8", "1.3.71"))

    testImplementation("junit:junit:4.13.2")
    testImplementation("org.assertj:assertj-core:3.16.1")
    testImplementation("org.robolectric:robolectric:4.3.1")
    testImplementation("io.mockk:mockk:1.10.2")
}
