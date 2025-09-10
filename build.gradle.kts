plugins {
    // this is necessary to avoid the plugins to be loaded multiple times
    // in each subproject's classloader
    alias(libs.plugins.androidApplication) apply false
    alias(libs.plugins.androidLibrary) apply false
    alias(libs.plugins.composeMultiplatform) apply false
    alias(libs.plugins.composeCompiler) apply false
    alias(libs.plugins.kotlinMultiplatform) apply false
    alias(libs.plugins.androidKotlinMultiplatformLibrary) apply false
    alias(libs.plugins.kotlinxSerialization) apply false
    alias(libs.plugins.kover)
    id("org.jlleitschuh.gradle.ktlint") version "12.1.1" apply true
    id("com.google.gms.google-services") version "4.4.3" apply false
    alias(libs.plugins.sonarqube)
}

// 👇 Add this block at root-level (after plugins{}), not inside a subproject
ktlint {
    version.set("1.2.1")
    android.set(true)
    outputToConsole.set(true)
    ignoreFailures.set(false) // fail build if violations
    enableExperimentalRules.set(true) // opt into experimental rules
    filter {
        exclude("**/generated/**")
        include("**/src/**/*.kt")
    }
}
kover {
    currentProject {
        createVariant("custom") {
            // use coverage if it's JVM module
            add("jvm", optional = true)
            // use coverage for debug build variant if it's Android module
            add("debug", optional = true)
        }
    }

    dependencies {
        kover(project(":feature:login"))
        kover(project(":feature:register"))
       // kover(project(":core:auth"))
        // all subprojects
    }
    reports {
        variant("custom") {
            filters {
                includes {
                    packages(
                        "org.dovoh.android_mvi.feature.login",
                        "org.dovoh.android_mvi.feature.register")
                }
            }
        }
    }
}

/*kover {
    currentProject {
        createVariant("custom") {
            // use coverage if it's JVM module
            add("jvm", optional = true)
            // use coverage for debug build variant if it's Android module
            add("debug", optional = true)
        }
    }

    reports {
        filters {
            excludes {
                annotatedBy("androidx.compose.runtime.Composable")
               *//* packages(
                    "org.dovoh.android_mvi.core.auth",
                    "org.dovoh.android_mvi.core.auth",
                    "org.dovoh.android_mvi.core.common",
                    "org.dovoh.android_mvi.core.network",
                    "org.dovoh.android_mvi.di",
                    "org.dovoh.android_mvi.feature.login",
                    "org.dovoh.android_mvi.feature.register",
                    "org.example.project.nbride")*//*

            }
        }
    }
}*/
/*kover {
    reports {
        filters {
            excludes {
                // examples:
                packages(
                    "com.dovoh.android_mvi.di",
                    "com.dovoh.android_mvi.generated",
                )
                annotatedBy("androidx.compose.runtime.Composable")
            }
        }
    }
}*/



sonar {
    properties {
        // Basics (set key/name to whatever you use in SonarQube/Cloud)
        property("sonar.projectKey", "mccostic_NBRideMultiPlatform")
        property("sonar.projectName", "NBRideMultiPlatform")

        // Sonar server + auth token from CI env (don’t hardcode locally)
        property("sonar.host.url", System.getenv("SONAR_HOST_URL") ?: "https://sonarcloud.io")
        property("sonar.login", System.getenv("SONAR_TOKEN") ?: "")

        // Sources & tests (covers KMP layout)
        property("sonar.sources", ".")
        property(
            "sonar.tests",
            listOf(
                "**/src/**/test/**",
                "**/src/**/androidTest/**",
                "**/src/**/commonTest/**",
            ).joinToString(","),
        )

        // Exclusions (build outputs, generated, etc.)
        property("sonar.exclusions", "**/build/**, **/.gradle/**, **/*.kts")

        // Kover merged XML report path (created by :koverMergedXmlReport)
        //property("sonar.kotlin.coverage.reportPaths", "${layout.buildDirectory}/reports/kover/merged/xml/report.xml")

        // (Optional fallback for older analyzers)
        property("sonar.coverage.jacoco.xmlReportPaths", "${layout.buildDirectory}/reports/kover/report.xml")
    }
}
