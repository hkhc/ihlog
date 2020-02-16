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
import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter

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

publishing {
    publications {
        create<MavenPublication>("lib") {

            with(pubConfig) {

                groupId = artifactGroup

                // The default artifactId is project.name
                // artifactId = artifactEyeD
                // version is gotten from an external plugin
    //            version = project.versioning.info.display
                version = artifactVersion
                // This is the main artifact
                from(components["java"])
                // We are adding documentation artifact
                artifact(dokkaJar)
                // And sources
                artifact(sourcesJar)


                // See https://maven.apache.org/pom.html for POM definitions

                pom {
                    name.set(project.name)
                    description.set(pomDescription)
                    url.set(pubConfig.pomUrl)
                    licenses {
                        license {
                            name.set(licenseName)
                            url.set(licenseUrl)
                        }
                    }
                    developers {
                        developer {
                            id.set(developerId)
                            name.set(developerName)
                            email.set(developerEmail)
                        }
                    }
                    scm {
                        connection.set(scmConnection)
                        developerConnection.set(scmConnection)
                        url.set(scmUrl)
                    }
                }

                // TODO dependency versionMapping

            }



        }
    }

    repositories {

        maven {

            with(pubConfig) {

                url = uri(
                    if (version.toString().endsWith("SNAPSHOT"))
                        nexusSnapshotRepositoryUrl!!
                    else
                        nexusReleaseRepositoryUrl!!
                )
                credentials {
                    username = nexusUsername!!
                    password = nexusPassword!!
                }

            }

        }

    }

}

fun currentZonedDateTime() =
    ZonedDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSSZZ"))


bintray {

    override = true
    dryRun = false
    publish = true

    user = pubConfig.bintrayUser
    key = pubConfig.bintrayApiKey
    setPublications("lib")

    pkg.apply {
        repo = "maven"
        name = project.name
        desc = pubConfig.pomDescription!!
        setLicenses(pubConfig.licenseName!!)
        websiteUrl = pubConfig.scmUrl!!
        vcsUrl = pubConfig.scmUrl!!
        githubRepo = pubConfig.scmGithubRepo!!
        issueTrackerUrl = pubConfig.issuesUrl!!
        version.apply {
            name = pubConfig.artifactVersion!!
            desc = pubConfig.pomDescription!!
            released = currentZonedDateTime()
            vcsTag = pubConfig.artifactVersion!!
        }
        setLabels("jar")
    }

    // Bintray requires our private key in order to sign archives for us. I don't want to share
    // the key and hence specify the signature files manually and upload them.
    filesSpec(closureOf<com.jfrog.bintray.gradle.tasks.RecordingCopyTask> {
        from("${project.buildDir}/libs").apply {
            include("*.aar.asc")
            include("*.jar.asc")
        }
        from("${project.buildDir}/publications/${"lib"}").apply {
            include("pom-default.xml.asc")
            rename("pom-default.xml.asc",
                "${project.name}-${pubConfig.artifactVersion}.pom.asc")
        }
        into("${(pubConfig.artifactGroup as String)
            .replace('.', '/')}/${project.name}/${pubConfig.artifactVersion}")
    })


}

signing {
    sign(publishing.publications["lib"])
}


dependencies {
    implementation(kotlin("stdlib-jdk8", "1.3.61"))
    testImplementation("junit:junit:4.12")
    testImplementation("org.assertj:assertj-core:3.11.1")
    testImplementation("io.mockk:mockk:1.9.3")
}