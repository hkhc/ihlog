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
import io.hkhc.gradle.PublishConfig
import io.hkhc.gradle.createPublication
import io.hkhc.gradle.createRepository
import io.hkhc.gradle.setupBintray
import org.jlleitschuh.gradle.ktlint.reporter.ReporterType

plugins {
    kotlin("jvm")
    id("org.jetbrains.dokka") version "0.10.1"
    id("org.jlleitschuh.gradle.ktlint") version "9.2.1"
    id("io.gitlab.arturbosch.detekt") version "1.5.1"
//    id("fr.coppernic.versioning") version "3.1.2"
    `maven-publish`
    signing
    id("com.jfrog.bintray")

}

val pubConfig = PublishConfig(project)

tasks {
    dokka {
        outputFormat = "html"
        outputDirectory = "$buildDir/dokka"
    }
}

ktlint {
    debug.set(true)
    verbose.set(true)
    android.set(true)
    coloredOutput.set(true)
    reporters {
        setOf(ReporterType.CHECKSTYLE, ReporterType.PLAIN)
    }
}

detekt {
    config = files("default-detekt-config.yml")
}


val sourcesJar by tasks.creating(Jar::class) {
    archiveClassifier.set("sources")
    from(sourceSets.getByName("main").allSource)
}

val dokkaJar by tasks.creating(Jar::class) {
    group = JavaBasePlugin.DOCUMENTATION_GROUP
    description = "Assembles Kotlin docs with Dokka to Jar"
    archiveClassifier.set("javadoc")
    from(tasks.dokka)
    dependsOn(tasks.dokka)
}

artifacts {
    artifacts.add("archives", sourcesJar)
    artifacts.add("archives", dokkaJar)
}

val pubName = "lib"

publishing {
    publications {
        createPublication(project, "lib", pubConfig, this)
    }
    repositories {
        createRepository(project, this, pubConfig)
    }
}

bintray {
    setupBintray(this, pubName, pubConfig)
}

signing {
    sign(publishing.publications[pubName])
}


dependencies {
    implementation(kotlin("stdlib-jdk8", "1.3.61"))
    testImplementation("junit:junit:4.12")
    testImplementation("org.assertj:assertj-core:3.11.1")
    testImplementation("io.mockk:mockk:1.9.3")
}