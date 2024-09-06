pluginManagement {
    repositories {
        google {
            content {
                includeGroupByRegex("com\\.android.*")
                includeGroupByRegex("com\\.google.*")
                includeGroupByRegex("androidx.*")
            }


        }
        google()
        mavenCentral()
        gradlePluginPortal()
        jcenter()
        maven {
            url = uri ("https://jitpack.io")
        }



    }


}





// Top-level build file where you can add configuration options common to all sub-projects/modules.




dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        google()
        mavenCentral()
        jcenter()
        maven {
            url = uri ("https://jitpack.io")
        }



    }
}






rootProject.name = "Juans"
include(":myapplication")

