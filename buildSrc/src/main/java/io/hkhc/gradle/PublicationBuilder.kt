/*
 * Copyright (c) 2020. Herman Cheung
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

package io.hkhc.gradle

import com.jfrog.bintray.gradle.BintrayExtension
import org.gradle.api.Project
import org.gradle.api.artifacts.dsl.RepositoryHandler
import org.gradle.api.plugins.ExtensionAware
import org.gradle.api.publish.PublicationContainer
import org.gradle.api.publish.PublishingExtension
import org.gradle.api.publish.maven.MavenPublication
import org.gradle.jvm.tasks.Jar
import org.gradle.kotlin.dsl.closureOf
import org.gradle.kotlin.dsl.create
import org.gradle.kotlin.dsl.get
import org.gradle.plugins.signing.SigningExtension
import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter

fun PublicationContainer.createPublication(
    project: Project,
    pubName: String
    ) {

    val pubConfig = PublishConfig(project)
    val dokkaJar = project.tasks.named("dokkaJar", Jar::class.java)
    val sourcesJar = project.tasks.named("sourcesJar", Jar::class.java)


    create<MavenPublication>(pubName) {

        with(pubConfig) {

            groupId = artifactGroup

            // The default artifactId is project.name
            // artifactId = artifactEyeD
            // version is gotten from an external plugin
            //            version = project.versioning.info.display
            version = artifactVersion
            // This is the main artifact
            from(project.components["java"])
            // We are adding documentation artifact
            artifact(dokkaJar.get())
            // And sources
            artifact(sourcesJar.get())


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

fun RepositoryHandler.createRepository(project: Project) {

    val pubConfig = PublishConfig(project)

    maven {

        with(pubConfig) {

            url = project.uri(
                if (project.version.toString().endsWith("SNAPSHOT"))
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

fun currentZonedDateTime() =
    ZonedDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSSZZ"))

fun Project.publishingConfig(
    project: Project,
    pubName: String
) {

    val ext = (project as ExtensionAware).extensions

    ext.findByType(PublishingExtension::class.java)?.config(project, pubName)
    ext.findByType(BintrayExtension::class.java)?.config(project, pubName)
    ext.findByType(SigningExtension::class.java)?.config(project, pubName)

}

fun PublishingExtension.config(
    project: Project,
    pubName: String
) {
    publications {
        createPublication(project, pubName)
    }
    repositories {
        createRepository(project)
    }

}

fun BintrayExtension.config(
    project: Project,
    pubName: String
    ) {

    val pubConfig = PublishConfig(project)
    val labelList = pubConfig.bintrayLabels?.split(',') ?: emptyList()
    val labelArray = Array(labelList.size) { labelList[it] }

    override = true
    dryRun = false
    publish = true

    user = pubConfig.bintrayUser
    key = pubConfig.bintrayApiKey
    setPublications(pubName)

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
        setLabels(*labelArray)
    }

    // Bintray requires our private key in order to sign archives for us. I don't want to share
    // the key and hence specify the signature files manually and upload them.
    filesSpec(closureOf<com.jfrog.bintray.gradle.tasks.RecordingCopyTask> {
        from("${project.buildDir}/libs").apply {
            include("*.aar.asc")
            include("*.jar.asc")
        }
        from("${project.buildDir}/publications/${pubName}").apply {
            include("pom-default.xml.asc")
            rename("pom-default.xml.asc",
                "${project.name}-${pubConfig.artifactVersion}.pom.asc")
        }
        into("${(pubConfig.artifactGroup as String)
            .replace('.', '/')}/${project.name}/${pubConfig.artifactVersion}")
    })

}

fun SigningExtension.config(
    project: Project,
    pubName: String
) {

    val ext = (project as ExtensionAware).extensions
    val publishingExtension = ext.findByType(PublishingExtension::class.java)

    publishingExtension?.let { sign(it.publications[pubName]) }

}
