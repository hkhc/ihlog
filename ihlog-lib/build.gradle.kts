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
import io.hkhc.gradle.allpublish.PublishConfig
import org.jlleitschuh.gradle.ktlint.reporter.ReporterType

plugins {
    kotlin("jvm")
    id("org.jetbrains.dokka") version "0.10.1"
    id("org.jlleitschuh.gradle.ktlint") version "9.2.1"
    id("io.gitlab.arturbosch.detekt") version "1.5.1"
//    id("fr.coppernic.versioning") version "3.1.2"
    `maven-publish`
    signing
//    id("com.jfrog.bintray")

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

publishing {
    publications {
        create<MavenPublication>("lib") {

            groupId = pubConfig.artifactGroup

            // The default artifactId is project.name
            // artifactId = artifactEyeD
            // version is gotten from an external plugin
//            version = project.versioning.info.display
            version = pubConfig.artifactVersion
            // This is the main artifact
            from(components["java"])
            // We are adding documentation artifact
            artifact(dokkaJar)
            // And sources
            artifact(sourcesJar)


            // See https://maven.apache.org/pom.html for POM definitions

            pom {
                name.set(project.name)
                description.set(pubConfig.pomDescription)
                url.set(pubConfig.pomUrl)
                licenses {
                    license {
                        name.set(pubConfig.licenseName)
                        url.set(pubConfig.licenseUrl)
                    }
                }
                developers {
                    developer {
                        id.set(pubConfig.developerId)
                        name.set(pubConfig.developerName)
                        email.set(pubConfig.developerEmail)
                    }
                }
                scm {
                    connection.set(pubConfig.scmConnection)
                    developerConnection.set(pubConfig.scmConnection)
                    url.set(pubConfig.scmUrl)
                }
            }

            // TODO dependency versionMapping



        }
    }

    repositories {

        maven {
            url = uri(
                if (version.toString().endsWith("SNAPSHOT"))
                    pubConfig.nexusSnapshotRepositoryUrl!!
                else
                    pubConfig.nexusReleaseRepositoryUrl!!
            )
            credentials {
                username = pubConfig.nexusUsername!!
                password = pubConfig.nexusPassword!!
            }
        }

    }

}


//// TODO enable default of each of them
//allPublish {
//    documentTask.set(tasks.named<DokkaTask>("dokka"))
//    sourceSet.set(sourceSets.getByName("main").allSource)
//    publicationName.set("lib")
//    publishComponent.set(components["java"])
//    label.set("jar")
//    System.out.println("ALL_PUBLISH(${project.name} finished allPublish")
//}

signing {
    sign(publishing.publications["lib"])
}

//bintray {
//    setup()
//}


dependencies {
    implementation(kotlin("stdlib-jdk8", "1.3.61"))
    testImplementation("junit:junit:4.12")
    testImplementation("org.assertj:assertj-core:3.11.1")
    testImplementation("io.mockk:mockk:1.9.3")
}