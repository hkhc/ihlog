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
import org.gradle.BuildAdapter
import org.gradle.BuildResult
import org.gradle.api.Project
import org.gradle.api.ProjectEvaluationListener
import org.gradle.api.ProjectState
import org.gradle.api.initialization.Settings
import org.gradle.api.invocation.Gradle
import org.gradle.api.publish.Publication
import org.gradle.api.publish.PublishingExtension
import org.gradle.kotlin.dsl.configure
import org.gradle.plugins.signing.SigningExtension

class ProjectEvaluationBuildListener(
    var sourceProject: Project,
    var pluginExtension: AllPublishExtension
): BuildAdapter(), ProjectEvaluationListener {

    private val exts = mutableListOf<AllPublishExtension>()

    override fun afterEvaluate(project: Project, state: ProjectState) {



//        if (project.name!=sourceProject.name) return
//
        System.out.println("AllPublishPlugin afterEvaluate ${project.name} ${state} ${this} ${Thread.currentThread().name}")
        val allPublishExt = project.extensions.findByType(AllPublishExtension::class.java)!!
        System.out.println("AllPublishPlugin afterEvaluate allpublish pubName ${allPublishExt?.publicationName==null}")
        val bintray = project.extensions.findByType(BintrayExtension::class.java)
        if (bintray!=null)
            System.out.println("AllPublishPlugin afterEvaluate bintray repoName ${bintray.publications}")
        else {
            project.pluginManager.apply("com.jfrog.bintray")
            System.out.println("AllPublishPlugin afterEvaluate bintray null")
        }
        val signingExt = project.extensions.findByType(SigningExtension::class.java)
        if (signingExt==null) {
            project.pluginManager.apply("org.gradle.signing")
        }

        if (bintray?.publications?.size==0) {

            val publishConfig = PublishConfig(project)

            // TODO Check if pluginExtension.publicationName is null

            val publicationFactory = PublicationFactory(project, pluginExtension, publishConfig)

            var publication: Publication? = null

            with(project) {
                configure<PublishingExtension> {
                    publications {
                        publication = publicationFactory.createMavenPublication(this)
                    }
                    repositories {
                        publicationFactory.createMavenRepository(this)
                    }

                }

                configure<SigningExtension> {
                    sign(publication)
                }

                publicationFactory.setupBintray(this)
                System.out.println("AllPublishPlugin finished setup bintray extension")

            }

            with(project.tasks) {


                named("bintrayUpload").get()
                    .dependsOn(named("publishToMavenLocal"))
                    .dependsOn(named("sign${allPublishExt.publicationName.get().capitalize()}Publication"))

                named("_bintrayRecordingCopy").get()
                    .dependsOn(named("sign${allPublishExt.publicationName.get().capitalize()}Publication"))

                named("allpublish").get()
                    .dependsOn(named("publish"))

                if (!publishConfig.artifactVersion!!.endsWith("SNAPSHOT")) {

                    named("allpublish").get()
                        .dependsOn(named("bintrayUpload"))

                }

            }

        }

        System.out.println("AllPublishPlugin afterEvaluate after bintray publications ${bintray?.publications}")


//        System.out.println("AllPublishPlugin afterEvaluate ${pluginExtension}")
//
//        val publishConfig = PublishConfig(project)
//
//        // TODO Check if pluginExtension.publicationName is null
//
//        if (pluginExtension.publicationName==null) return
//
//        val publicationFactory = PublicationFactory(project, pluginExtension, publishConfig)
//
//        var publication: Publication? = null
//
//        with(project) {
//            configure<PublishingExtension> {
//                publications {
//                    publication = publicationFactory.createMavenPublication(this)
//                }
//                repositories {
//                    publicationFactory.createMavenRepository(this)
//                }
//
//            }
//
//            configure<SigningExtension> {
//                sign(publication)
//            }
//
//            publicationFactory.setupBintray(this)
//
//        }
//
//
//


    }

    override fun beforeEvaluate(project: Project) {
        System.out.println("AllPublishPlugin beforeEvaluate")
    }

    override fun projectsEvaluated(gradle: Gradle) {

        System.out.println("AllPublishPlugin projectsEvaluated")

        System.out.println("AllPublishPlugin projectsEvaluated ${sourceProject.name} ${this} ${Thread.currentThread().name}")
        val bintray = sourceProject.extensions.findByType(BintrayExtension::class.java)
        if (bintray!=null)
            System.out.println("AllPublishPlugin projectsEvaluated bintray repoName ${bintray.pkg.repo}")
        else
            System.out.println("AllPublishPlugin projectsEvaluated bintray null")

        val allPublishExt = sourceProject.extensions.findByType(AllPublishExtension::class.java)!!
        System.out.println("AllPublishPlugin projectsEvaluated allpublish pubName ${allPublishExt?.publicationName.get()}")

        if (bintray?.publications?.size==0) {

            val publishConfig = PublishConfig(sourceProject)

            // TODO Check if pluginExtension.publicationName is null

            val publicationFactory = PublicationFactory(sourceProject, pluginExtension, publishConfig)

            var publication: Publication? = null

            with(sourceProject) {
                configure<PublishingExtension> {
                    publications {
                        publication = publicationFactory.createMavenPublication(this)
                    }
                    repositories {
                        publicationFactory.createMavenRepository(this)
                    }

                }

                configure<SigningExtension> {
                    sign(publication)
                }

                publicationFactory.setupBintray(this)
                System.out.println("AllPublishPlugin finished setup bintray extension")

            }

            with(sourceProject.tasks) {


                named("bintrayUpload").get()
                    .dependsOn(named("publishToMavenLocal"))
                    .dependsOn(named("sign${allPublishExt.publicationName.get().capitalize()}Publication"))

                named("_bintrayRecordingCopy").get()
                    .dependsOn(named("sign${allPublishExt.publicationName.get().capitalize()}Publication"))

                named("allpublish").get()
                    .dependsOn(named("publish"))

                if (!publishConfig.artifactVersion!!.endsWith("SNAPSHOT")) {

                    named("allpublish").get()
                        .dependsOn(named("bintrayUpload"))

                }

            }

            System.out.println("AllPublishPlugin projectEvaluated after bintray publications ${bintray?.publications}")

        }


//        with(sourceProject.tasks) {
//
//
//            named("bintrayUpload").get()
//                .dependsOn(named("publishToMavenLocal"))
//                .dependsOn(named("sign${pluginExtension.publicationName.get().capitalize()}Publication"))
//
//            named("_bintrayRecordingCopy").get()
//                .dependsOn(named("sign${pluginExtension.publicationName.get().capitalize()}Publication"))
//
//            named("allpublish").get()
//                .dependsOn(named("publish"))
//
//            if (!publishConfig.artifactVersion!!.endsWith("SNAPSHOT")) {
//
//                named("allpublish").get()
//                    .dependsOn(named("bintrayUpload"))
//
//            }
//
//        }

    }

    override fun settingsEvaluated(settings: Settings) {
        System.out.println("AllPublishPlugin settingsEvaluated")
    }

    override fun buildFinished(result: BuildResult) {
        System.out.println("AllPublishPlugin buildFinished")
    }

    override fun projectsLoaded(gradle: Gradle) {
        System.out.println("AllPublishPlugin projectsLoaded")
    }

    override fun buildStarted(gradle: Gradle) {
        super.buildStarted(gradle)
    }
}