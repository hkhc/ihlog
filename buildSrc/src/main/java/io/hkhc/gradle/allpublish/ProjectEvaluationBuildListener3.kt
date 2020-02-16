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

import io.hkhc.gradle.PublishConfig
import org.gradle.BuildAdapter
import org.gradle.BuildResult
import org.gradle.api.Project
import org.gradle.api.ProjectEvaluationListener
import org.gradle.api.ProjectState
import org.gradle.api.initialization.Settings
import org.gradle.api.invocation.Gradle

class ProjectEvaluationBuildListener3(
    var targetProject: Project,
    var publishConfig: PublishConfig
): BuildAdapter(), ProjectEvaluationListener {

    private var hasSetupTask = false

    override fun afterEvaluate(project: Project, state: ProjectState) {

        System.out.println("ALL_PUBLISH(${targetProject.name}) afterEvaluate ${project.name} ${state.executed}")


    }

    override fun beforeEvaluate(project: Project) {

        System.out.println("ALL_PUBLISH(${targetProject.name}) beforeEvaluate ${project.name} ")

    }

    override fun settingsEvaluated(settings: Settings) {
        System.out.println("ALL_PUBLISH(${targetProject.name}) settingsEvaluated")
    }

    override fun buildFinished(result: BuildResult) {
        System.out.println("ALL_PUBLISH(${targetProject.name}) buildFinished ${result.action}")
    }

    override fun projectsLoaded(gradle: Gradle) {
        System.out.println("ALL_PUBLISH(${targetProject.name}) projectsLoaded")
    }

    override fun buildStarted(gradle: Gradle) {
        System.out.println("ALL_PUBLISH(${targetProject.name}) buildStarted")
    }

    override fun projectsEvaluated(gradle: Gradle) {
        System.out.println("ALL_PUBLISH(${targetProject.name}) projectsEvaluated")

        if (hasPublicationName()) {
            System.out.println("ALL_PUBLISH(${targetProject.name}) setupTasks")
            setupTasks()
        }

    }

    private fun getExtension() =
        targetProject.extensions.findByType(AllPublishExtension::class.java)


    private fun hasPublicationName(): Boolean {
        val extension = targetProject.extensions.findByType(AllPublishExtension::class.java)
        return extension
            ?.run { publicationName.isPresent() }
            ?: false
    }

    private fun getPublicationName(): String {
        val extension = targetProject.extensions.findByType(AllPublishExtension::class.java)
        return extension
            ?.run { publicationName.get() }
            ?: "untitledPublication"
    }


    private fun setupTasks() {

        if (hasSetupTask) return

        with(targetProject.tasks) {

            this.all {
                System.out.println("ALL_PUBLISH tasks ${name}")
            }

            val publicationName = getPublicationName().capitalize()

            System.out.println("ALL_PUBLISH(${targetProject.name}) setupTasks1 ${publicationName}")

//            named("publish${publicationName}PublicationToMavenLocal").get()
//                .dependsOn("allPublish${publicationName}DocumentJar")
//                .dependsOn("allPublish${publicationName}SourcesJar")
//
            named("bintrayUpload").get()
                .dependsOn("publish${publicationName}PublicationToMavenLocal")
                .dependsOn("sign${publicationName}Publication")
//
//            named("_bintrayRecordingCopy").get()
//                .dependsOn("sign${publicationName}Publication")
//
//            named("allpublish").get()
//                .dependsOn("publish")
//
//            if (!publishConfig.artifactVersion!!.endsWith("SNAPSHOT")) {
//
//                named("allpublish").get()
//                    .dependsOn("bintrayUpload")
//
//            }

        }

        hasSetupTask = true


    }

}

