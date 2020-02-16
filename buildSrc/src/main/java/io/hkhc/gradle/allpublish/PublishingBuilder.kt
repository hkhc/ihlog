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
import org.gradle.api.publish.PublishingExtension
import org.gradle.kotlin.dsl.closureOf
import org.gradle.kotlin.dsl.get
import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter


class PublishingBuilder(val project: Project) {

    val publishConfig = PublishConfig(project)
    val pluginExtension = project.extensions["allPublish${project.name.capitalize()}"] as AllPublishExtension

    fun build(ext: PublishingExtension) {

        val publicationFactory = PublicationFactory(project, pluginExtension, publishConfig)

        with(ext) {
            publications {
                publicationFactory.createMavenPublication(this)
            }
            repositories {
                publicationFactory.createMavenRepository(this)
            }
        }

    }

    private fun currentZonedDateTime() =
        ZonedDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSSZZ"))

    fun buildBintray(ext: BintrayExtension) {

        with(ext) {
            with(publishConfig) {
                user = System.getenv("BINTRAY_USER")
                key = System.getenv("BINTRAY_KEY")
                setPublications(pluginExtension.publicationName.get())
                filesSpec(closureOf<RecordingCopyTask> {
                    from("${project.buildDir}/libs").apply {
                        include("*.aar.asc")
                        include("*.jar.asc")
                    }
                    from("${project.buildDir}/publications/${pluginExtension.publicationName.get()}").apply {
                        include("pom-default.xml.asc")
                        rename(
                            "pom-default.xml.asc",
                            "$artifactId-$artifactVersion.pom.asc"
                        )
                    }
                    into(
                        "${(artifactGroup as String)
                            .replace('.', '/')}/$artifactId/$artifactVersion"
                    )
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