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

package io.hkhc.gradle.allpublish

import com.jfrog.bintray.gradle.BintrayExtension
import com.jfrog.bintray.gradle.tasks.RecordingCopyTask
import io.hkhc.gradle.PublishConfig
import org.gradle.api.Project
import org.gradle.api.artifacts.dsl.RepositoryHandler
import org.gradle.api.plugins.JavaBasePlugin
import org.gradle.api.publish.Publication
import org.gradle.api.publish.PublicationContainer
import org.gradle.api.publish.maven.MavenPublication
import org.gradle.api.tasks.bundling.Jar
import org.gradle.kotlin.dsl.closureOf
import org.gradle.kotlin.dsl.configure
import org.gradle.kotlin.dsl.create
import java.net.URI
import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter


class PublicationFactory(
    var project: Project,
    var pluginExtension: AllPublishExtension,
    var publishConfig: PublishConfig
) {

    fun createDocJarTask() =
        project.tasks.create(
            "allPublish${pluginExtension.publicationName.get().capitalize()}DocumentJar",
            Jar::class) {
            group = JavaBasePlugin.DOCUMENTATION_GROUP
            description = "Assembles document for publishing"
            archiveClassifier.set("javadoc")
            from(pluginExtension.documentTask.get())
            dependsOn(pluginExtension.documentTask.get())
        }

    fun createSourceJarTask() =
        project.tasks.create(
            "allPublish${pluginExtension.publicationName.get().capitalize()}SourcesJar",
            Jar::class) {
            archiveClassifier.set("sources")
            from(pluginExtension.sourceSet)
        }

    fun createMavenPublication(container: PublicationContainer): Publication {

        System.out.println("AllPublishPlugin Creating publication component ${pluginExtension.publishComponent.get()}")

        return container.create<MavenPublication>(pluginExtension.publicationName.get()) {
            this.groupId = publishConfig.artifactGroup!!
            artifactId = publishConfig.artifactEyeD!!
            version = publishConfig.artifactVersion!!

            // This is the main artifact
            from(pluginExtension.publishComponent.get())
            // We are adding documentation artifact
            //            artifact(tasks.named("allPublishDocumentJar", Jar::class))
            artifact(createDocJarTask())
            // And sources
            artifact(createSourceJarTask())

            pom {
                name.set(publishConfig.artifactEyeD!!)
                description.set(publishConfig.pomDescription!!)
                url.set(publishConfig.pomUrl!!)
                licenses {
                    license {
                        name.set(publishConfig.licenseName!!)
                        url.set(publishConfig.licenseUrl!!)
                        distribution.set(publishConfig.licenseDist!!)
                    }
                }
                developers {
                    developer {
                        id.set(publishConfig.developerId!!)
                        name.set(publishConfig.developerName!!)
                    }
                }
                scm {
                    url.set(publishConfig.scmUrl!!)
                    connection.set(publishConfig.scmConnection!!)
                }
            }
        }
    }

    fun createMavenRepository(repositoryHandler: RepositoryHandler) =
        repositoryHandler.maven {
            url = URI(publishConfig.nexusSnapshotRepositoryUrl!!)
            credentials {
                username = publishConfig.nexusUsername!!
                password = publishConfig.nexusPassword!!
            }
        }

    private fun currentZonedDateTime() =
        ZonedDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSSZZ"))


    fun setupBintray(project: Project) {

        val publicationName = pluginExtension.publicationName.get()

        System.out.println("AllPublishPlugin Creating Bintray publicationName ${publicationName}")
        System.out.println("AllPublishPlugin Creating Bintray label ${pluginExtension.label.get()}")

        project.configure<BintrayExtension> {
            with (publishConfig) {
                user = System.getenv("BINTRAY_USER")
                key = System.getenv("BINTRAY_KEY")
                setPublications(publicationName)
                filesSpec(closureOf<RecordingCopyTask> {
                    from("${project.buildDir}/libs").apply {
                        include("*.aar.asc")
                        include("*.jar.asc")
                    }
                    from("${project.buildDir}/publications/${publicationName}").apply {
                        include("pom-default.xml.asc")
                        rename("pom-default.xml.asc",
                            "$artifactId-$artifactVersion.pom.asc")
                    }
                    into("${(artifactGroup as String)
                        .replace('.', '/')}/$artifactId/$artifactVersion")
                })

                override = true
                dryRun = false
                pkg.apply {
                    repo = "maven"
                    name = artifactEyeD!!
                    desc = pomDescription!!
                    setLicenses(licenseName!!)
                    websiteUrl = scmUrl!!
                    vcsUrl = scmUrl!!
                    githubRepo = scmGithubRepo!!
                    issueTrackerUrl = issuesUrl!!
                    version.apply {
                        name = artifactVersion!!
                        desc = pomDescription!!
                        released = currentZonedDateTime()
                        vcsTag = artifactVersion!!
                    }
                    setLabels(pluginExtension.label.get())
                }
            }
        }

    }

}