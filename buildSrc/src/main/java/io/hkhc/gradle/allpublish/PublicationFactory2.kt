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
import org.gradle.api.Project
import org.gradle.api.artifacts.dsl.RepositoryHandler
import org.gradle.api.plugins.JavaBasePlugin
import org.gradle.api.publish.Publication
import org.gradle.api.publish.PublicationContainer
import org.gradle.api.publish.maven.MavenPublication
import org.gradle.api.tasks.bundling.Jar
import org.gradle.kotlin.dsl.closureOf
import org.gradle.kotlin.dsl.create
import java.net.URI
import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter

fun PublicationContainer.mavenPublication(project: Project) =
    project.plugins
        .getPlugin(AllPublishPlugin2::class.java)
        .factory
        .createMavenPublication(this)

fun RepositoryHandler.mavenRepository(project: Project) =
    project.plugins
        .getPlugin(AllPublishPlugin2::class.java)
        .factory
        .createMavenRepository(this)

fun BintrayExtension.setup() =
    project.plugins
        .getPlugin(AllPublishPlugin2::class.java)
        .factory
        .setupBintray(this)


class PublicationFactory2(
    var project: Project
) {

    val extension = project.extensions.findByType(AllPublishExtension::class.java)

    val publishConfig = PublishConfig(project)

    fun createMavenPublication(
        container: PublicationContainer
    ): Publication {

        if (extension!=null) {
            with (extension?.publicationName?.get()) {
                System.out.println("ALL_PUBLISH find or create publication ${this}")
                return (container.findByName(this) ?:
                    container.create<MavenPublication>(this) { setup() }).apply {
                    System.out.println("ALL_PUBLISH publication result ${this}")
                }
            }
        }
        else {
            return container.create("untitledPub") as MavenPublication
        }


    }

    // TODO clean up !! references and provide default values
    private fun MavenPublication.setup() {

        if (extension==null) {
            System.out.println("allpublish extension is not found")
            // TODO show warning
            return
        }

        groupId = publishConfig.artifactGroup!!
        artifactId = publishConfig.artifactEyeD!!
        version = publishConfig.artifactVersion!!

        System.out.println("ALL_PUBLISH setup publication $name $groupId:$artifactId:$version")

        // This is the main artifact
        from(extension.publishComponent.get())
        // We are adding documentation artifact
        //            artifact(tasks.named("allPublishDocumentJar", Jar::class))
        artifact(createDocJarTask(extension))
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

    fun createMavenRepository(repositoryHandler: RepositoryHandler) =
        repositoryHandler.maven {
            if (publishConfig.artifactVersion!!.endsWith("SNAPSHOT"))
                url = URI(publishConfig.nexusSnapshotRepositoryUrl!!)
            else
                url = URI(publishConfig.nexusReleaseRepositoryUrl!!)
            credentials {
                username = publishConfig.nexusUsername!!
                password = publishConfig.nexusPassword!!
            }
        }

    fun setupBintray(bintray: BintrayExtension) {

        if (extension==null) {
            // TODO show warning
            return
        }

        val publicationName = extension.publicationName.get()

        System.out.println("AllPublishPlugin Creating Bintray publicationName ${publicationName}")
        System.out.println("AllPublishPlugin Creating Bintray label ${extension.label.get()}")

        with(bintray) {
            with(publishConfig) {
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
                pkg(closureOf<BintrayExtension.PackageConfig> {
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
                        gpg.sign = false
                        mavenCentralSync.sync = false
                    }
                    setLabels(extension.label.get())
                })
            }
        }

    }

    private fun currentZonedDateTime() =
        ZonedDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSSZZ"))

    private fun getPackageName() =
        extension
            ?.run {extension.publicationName.get() }
            ?: "untitledPublication"

    private fun createDocJarTask(extension: AllPublishExtension): Jar {

        return project.tasks.create(
            "allPublish${getPackageName().capitalize()}DocumentJar",
            Jar::class
        ) {
            val documentTask = extension.documentTask.get()
            documentTask.run {
                group = JavaBasePlugin.DOCUMENTATION_GROUP
                description = "Assembles document for publishing"
                archiveClassifier.set("javadoc")
                from(this)
                dependsOn(this)
                System.out.println("ALL_PUBLISH after createing docjar task")
            }
        }
    }

    private fun createSourceJarTask(): Jar {

        return project.tasks.create(
            "allPublish${getPackageName().capitalize()}SourcesJar",
            Jar::class
        ) {
            this@PublicationFactory2.extension?.apply {
                archiveClassifier.set("sources")
                from(sourceSet)
            }
        }

    }


}