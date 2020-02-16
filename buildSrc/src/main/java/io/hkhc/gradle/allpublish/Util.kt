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

import org.gradle.api.Project
import org.gradle.kotlin.dsl.configure
import org.gradle.kotlin.dsl.create
import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter

fun Project.allPublish(configuration: AllPublishExtension.() -> Unit): Unit {
    configure(configuration)
}

fun getExtensionName(project: Project, variant: String = "") =
    "allPublish${project.name.capitalize()}${variant.capitalize()}"

fun Project.createAllPluginExtension(variant: String = ""): AllPublishExtension {
    return project.extensions.create(getExtensionName(this, variant))
}

fun Project.allPluginExtension(): AllPublishExtension? {
    return extensions.findByName(getExtensionName(this)) as AllPublishExtension?
}


object Util {

    fun currentZonedDateTime() =
        ZonedDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSSZZ"))

}

