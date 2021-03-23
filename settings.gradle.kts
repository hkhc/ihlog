include(":ihlog-lib-gradle")
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

include("app", ":ihlog-lib", ":ihlog-lib-android")

// The default project name is the same as the folder.
// Change the project name here if we need something different

// In project navigator of Android Studio / Intellij, the root/child projects are presented
// as "folderName[projectName]", where folderName is the physical folder name of the project,
// and "projectName" is that of project.name

// if we changed the default name after the projects are established, one may need to update the
// name in intra-project dependencies in other child projects.

// Change root project name

rootProject.name = "ihlog-root"

// Change child project name
project(":ihlog-lib").name="ihlog"
project(":ihlog-lib-android").name="ihlog-android"
//project(":ihlog-lib-gradle").name="ihlog-gradle"
