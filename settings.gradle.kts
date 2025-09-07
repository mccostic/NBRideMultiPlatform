rootProject.name = "NBRideMultiPlatform"
enableFeaturePreview("TYPESAFE_PROJECT_ACCESSORS")

pluginManagement {
    repositories {
        google {
            mavenContent {
                includeGroupAndSubgroups("androidx")
                includeGroupAndSubgroups("com.android")
                includeGroupAndSubgroups("com.google")
            }
        }
        mavenCentral()
        gradlePluginPortal()
    }
}

dependencyResolutionManagement {
    repositories {
        google {
            mavenContent {
                includeGroupAndSubgroups("androidx")
                includeGroupAndSubgroups("com.android")
                includeGroupAndSubgroups("com.google")
            }
        }
        mavenCentral()
    }
}
includeBuild("build-logic")
include(":composeApp")
include(":di")
include(":core:auth")
include(":core:common")
include(":core:mvi")
include(":core:network")
include(":core:network:config")
include(":feature:login")
include(":feature:register")
include(":core:logging")
include(":core:navigation")
include(":feature:home")
