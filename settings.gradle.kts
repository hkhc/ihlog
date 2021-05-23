/*
 * Copyright (c) 2021. Herman Cheung
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

import de.fayard.refreshVersions.bootstrapRefreshVersions

/**
 * The pluginManagement block is need to load plugin from maven local repository.
 * it has to be the first block in settings.gradle.kts
 * When all plugins needed are published to Gradle Plugin Portal, this block can be
 * commented.
 */
pluginManagement {
    repositories {
        mavenLocal()
        jcenter()
        gradlePluginPortal()
    }
}

plugins {
    id("de.fayard.refreshVersions") version "0.10.0"
}

// The default project name is the same as the folder.
// Change the project name here if we need something different

// In project navigator of Android Studio / Intellij, the root/child projects are presented
// as "folderName[projectName]", where folderName is the physical folder name of the project,
// and "projectName" is that of project.name

// if we changed the default name after the projects are established, one may need to update the
// name in intra-project dependencies in other child projects.

// Change root project name

include(":app", ":ihlog-lib", ":ihlog-lib-android",":ihlog-lib-gradle")

rootProject.name = "ihlog-root"

// Change child project name
project(":ihlog-lib").name="ihlog"
project(":ihlog-lib-android").name="ihlog-android"
//project(":ihlog-lib-gradle").name="ihlog-gradle"
