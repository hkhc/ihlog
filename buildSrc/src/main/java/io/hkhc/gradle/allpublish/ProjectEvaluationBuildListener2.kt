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
import org.gradle.api.Project
import org.gradle.api.ProjectEvaluationListener
import org.gradle.api.ProjectState
import org.gradle.api.invocation.Gradle

class ProjectEvaluationBuildListener2
    : BuildAdapter(), ProjectEvaluationListener {

    val extMap = mutableMapOf<Project, MutableList<AllPublishExtension>>()

    override fun afterEvaluate(project: Project, state: ProjectState) {

        System.out.println("pluginExtension afterEvaluate ${project.name}")

        val pluginExtension = project.allPluginExtension() ?: return

        System.out.println("pluginExtension afterEvaluate ${project.name} ext present")

        if (extMap[project]==null) {
            extMap.put(project, mutableListOf(pluginExtension))
        }
        else {
            extMap[project]?.add(pluginExtension)
        }

    }

    override fun projectsEvaluated(gradle: Gradle) {

        for(extEntry in extMap) {

            val project = extEntry.key
            val publishConfig = PublishConfig(extEntry.key)
            val pluginExtensions = extEntry.value

            for(ext in pluginExtensions) {

                if (!ext.publicationName.isPresent()) return

                val publicationName = ext.publicationName.get().capitalize()
                System.out.println("pluginExtension projectsEvaluated ${project.name} publicationName present ${publicationName}")
                val signTask = project.tasks.named("sign${publicationName}Publication")

//                for(dtask in signTask.get().dependsOn) {
//                    System.out.println("${signTask.get().name} depends on ${dtask::class}")
//                }

                System.out.println("pluginExtension projectsEvaluated publicationName ${ext.publicationName.get()}")

                with(project.tasks) {

                    named("bintrayUpload").get()
                        .dependsOn(named("publishToMavenLocal"))
                        .dependsOn(signTask)

                    named("_bintrayRecordingCopy").get()
                        .dependsOn(signTask)

                    named("allpublish").get()
                        .dependsOn(named("publish"))

                    if (!publishConfig.artifactVersion!!.endsWith("SNAPSHOT")) {

                        named("allpublish").get()
                            .dependsOn(named("bintrayUpload"))

                    }

                }

            }


        }

    }

    override fun beforeEvaluate(project: Project) {
    }
}