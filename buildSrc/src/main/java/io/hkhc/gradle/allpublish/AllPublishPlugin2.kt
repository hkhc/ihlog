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
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.plugins.BasePluginConvention

@Suppress("unused")
class AllPublishPlugin2: Plugin<Project> {

    lateinit var publishConfig: PublishConfig
    lateinit var factory: PublicationFactory2


    override fun apply(project: Project) {

        System.out.println("ALL_PUBLISH apply ${project.name}")

        project.gradle.addListener(DependencyListener())

        with(project) {
            publishConfig = PublishConfig(this)
            var task = project.tasks.create("allpublish")

            project.createAllPluginExtension()
            System.out.println("create AllPublish extension for ${project.name} ")
            factory = PublicationFactory2(project)

        }

        publishConfig.artifactId?.run {

            with(project) {

                convention.getPlugin(BasePluginConvention::class.java).archivesBaseName = publishConfig.artifactId!!
                version = publishConfig.artifactVersion!!

                gradle.addProjectEvaluationListener(ProjectEvaluationBuildListener3(project, publishConfig))

            }
        }

    }
}