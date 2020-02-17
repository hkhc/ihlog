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

//import io.hkhc.gradle.allpublish.allPublish
//import io.hkhc.gradle.allpublish.mavenPublication
//import io.hkhc.gradle.allpublish.mavenRepository
//import io.hkhc.gradle.allpublish.setup
import org.jetbrains.dokka.gradle.DokkaTask

plugins {
    id("com.android.library")
    id("kotlin-android")
    id("kotlin-android-extensions")
    id("kotlin-kapt")
    id("org.jetbrains.dokka-android") version "0.9.17"
    id("digital.wup.android-maven-publish") version "3.6.2"
//    id("io.hkhc.gradle.allpublish")
    `maven-publish`
    signing
    id("com.jfrog.bintray")

}

val artifactId : String by project

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

    if (variantName=="Release") {

        System.out.println("android setup publishing for ${variantName}")

        tasks.withType<DokkaTask> {
            outputFormat = "html"
            outputDirectory = "$buildDir/javadoc"
        }

        tasks.register<DokkaTask>("dokka$variantName") {
            group = JavaBasePlugin.DOCUMENTATION_GROUP
            description = "Generate KDoc for build $variantName"
            if (outputDirectory.isEmpty())
                outputDirectory = artifactId
            else
                outputDirectory = "$outputDirectory/${artifactId}"

        }

        // TODO enable default of each of them
//        allPublish {
//            documentTask.set(tasks.named<DokkaTask>("dokka$variantName"))
//            sourceSet.set(files(javaCompileProvider.get().source))
//            publicationName.set("lib${variantName}")
//            publishComponent.set(components["android"])
//            label.set("aar")
//            System.out.println("ALL_PUBLISH extension setup")
//        }

//        publishing {
//            publications {
//                mavenPublication(project)
//            }
//            repositories {
//                mavenRepository(project)
//            }
//        }
//
//        signing {
//            sign(publishing.publications.mavenPublication(project))
//        }
//
//        bintray {
//            setup()
//        }

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
