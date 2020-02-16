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

import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.plugins.BasePluginConvention
import org.gradle.kotlin.dsl.create

@Suppress("unused")
class AllPublishPlugin: Plugin<Project> {

    var pluginExtension: AllPublishExtension? = null
    lateinit var publishConfig: PublishConfig

    var listener: ProjectEvaluationBuildListener? = null

    override fun apply(project: Project) {

        System.out.println("Applying AllPublishPlugin ${project.name} ${this}")

        with(project) {

            with(pluginManager) {
                apply("org.gradle.maven-publish")
            }

            publishConfig = PublishConfig(this)
            var task = project.tasks.create("allPublish")

            var pluginExtension: AllPublishExtension = project.extensions.create("allPublish")
            System.out.println("create AllPublish extension for ${project.name} ")

            with(project) {
                convention.getPlugin(BasePluginConvention::class.java).archivesBaseName = publishConfig.artifactId!!
                version = publishConfig.artifactVersion!!
            }

            listener = ProjectEvaluationBuildListener(project, pluginExtension).also {
                gradle.addProjectEvaluationListener(it)
                gradle.addBuildListener(it)
            }

            pluginManager.apply("com.jfrog.bintray")

            tasks.register("allpublish") {}

        }


    }

}