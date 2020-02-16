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

import org.gradle.api.Action
import org.gradle.api.Project
import org.gradle.api.publish.maven.MavenPom

class PomBuilder(var project: Project) {

    fun getProperty(propertyName: String): String {
        val value = project.property("${project.name}.$propertyName")
        if (value==null)
            throw Exception(
                "Property $propertyName is required by module ${project.name} and it is not found")
        else
            return value as String
    }

    fun build(): Action<MavenPom> =

        Action {

            name.set(project.rootProject.name)
            description.set(getProperty("description"))
            url.set("url")
            licenses {
                license {
                    name.set(getProperty("license.name"))
                    url.set("license.url")
                    distribution.set("license.dist")
                }
            }
            developers {
                developer {
                    id.set("developer.id")
                    name.set("developer.name")
                }
            }
            scm {
                url.set("scm.url")
                connection.set("scm.connection")
            }

        }


}

fun Project.buildPom() = PomBuilder(this).build()

