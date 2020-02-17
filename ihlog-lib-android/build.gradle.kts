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

import io.hkhc.gradle.publishingConfig
import org.jetbrains.dokka.gradle.DokkaTask

plugins {
    id("com.android.library")
    id("kotlin-android")
    id("kotlin-android-extensions")
    id("kotlin-kapt")
    id("org.jetbrains.dokka") version "0.10.1"
    id("digital.wup.android-maven-publish") version "3.6.2"
    id("org.jlleitschuh.gradle.ktlint") version "9.2.1"
    id("io.gitlab.arturbosch.detekt") version "1.5.1"
//    id("io.hkhc.gradle.allpublish")
    `maven-publish`
    signing
    id("com.jfrog.bintray")
    id("com.dorongold.task-tree") version "1.5"
}

android {
    compileSdkVersion(28)

    defaultConfig {
        minSdkVersion(21)
        targetSdkVersion(28)
        versionCode = 1
        versionName = "1.0"
        testInstrumentationRunner = "android.support.test.runner.AndroidJUnitRunner"
    }

    buildTypes {
        getByName("release") {
            isMinifyEnabled = false
            proguardFiles(getDefaultProguardFile("proguard-android.txt"), "proguard-rules.pro")
        }
    }

    testOptions {
        with(unitTests) {
            isIncludeAndroidResources = true
            isReturnDefaultValues = true
        }
    }

}

android.libraryVariants.configureEach {
    val variantName = name.capitalize()

    System.out.println("android publishing variant ${variantName}")

    var dokka = tasks.register("dokka$variantName", DokkaTask::class) {
        group = JavaBasePlugin.DOCUMENTATION_GROUP
        description = "Generate Kotlin docs with Dokka for variant $variantName"
        outputFormat = "html"
        outputDirectory = "$buildDir/dokka"
    }

    if (variantName=="Release") {

        System.out.println("android setup publishing for ${variantName}")

        tasks.register("dokkaJar$variantName", Jar::class) {
            group = JavaBasePlugin.DOCUMENTATION_GROUP
            description = "Assembles Kotlin docs with Dokka to Jar for variant $variantName"

            from(dokka)
            dependsOn(dokka)
        }

        tasks.register("sourcesJar${variantName}", Jar::class) {
            description = "Create archive of source code for the binary for variant $variantName"
            from(files(javaCompileProvider.get().source))
        }

        project.publishingConfig("lib", variantName, "android")

    }

}


dependencies {
    implementation(fileTree(mapOf("dir" to "libs", "include" to listOf("*.jar"))))
    implementation(project(":ihlog"))
    implementation(kotlin("stdlib-jdk8", "1.3.21"))
    testImplementation("junit:junit:4.12")
    testImplementation("org.assertj:assertj-core:3.11.1")
    testImplementation("org.robolectric:robolectric:4.1")
//        exclude group: 'commons-logging', module: 'commons-logging'
//        exclude group: 'org.apache.httpcomponents', module: 'httpclient'
//    }
    testImplementation("io.mockk:mockk:1.9.1")
}
