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

ktlint {
    version.set("1.2.1")
    android.set(true)
    outputToConsole.set(true)
    ignoreFailures.set(false)
    enableExperimentalRules.set(true)
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
    }
    reports {
        verify {
            rule {
                minBound(10)
            }
        }
        variant("custom") {
            filters {
                includes {
                    packages(
                        "org.dovoh.android_mvi.feature.login",
                        "org.dovoh.android_mvi.feature.register",
                    )
                }
                excludes{
                    packages("com.dovoh.android_mvi.core.common",
                        "com.dovoh.android_mvi.core.logging",
                        "com.dovoh.android_mvi.core.mvi",
                        "com.dovoh.android_mvi.core.navigation",
                        "com.dovoh.android_mvi.core.network")
                }
            }
        }
    }
}

sonar {
    properties {
        // --- Identity / server ---
        property("sonar.organization", "mccostic")
        property("sonar.projectKey", "mccostic_NBRideMultiPlatform")
        property("sonar.projectName", "NBRideMultiPlatform")
        property("sonar.host.url", System.getenv("SONAR_HOST_URL") ?: "https://sonarcloud.io")
        // Auth comes from env SONAR_TOKEN; do NOT set sonar.login here.
        property("sonar.token", System.getenv("SONAR_TOKEN") ?: "")

        // --- Make source/test sets disjoint (KMP layouts) ---
        property(
            "sonar.sources",
            listOf(
                "**/src/main/**",
                "**/src/commonMain/**",
                "**/src/androidMain/**",
                "**/src/iosMain/**",
            ).joinToString(","),
        )
        property(
            "sonar.tests",
            listOf(
                "**/src/commonTest/**",
            ).joinToString(","),
        )

        // Exclude build outputs & gradle internals (safe)
        property("sonar.exclusions", "**/build/**,**/.gradle/**,**/*.kts,**/di/**")

        // Coverage (Kover XML). We glob so multi-module paths are picked up.
        property(
            "sonar.kotlin.coverage.reportPaths",
            fileTree(project.rootDir) {
                include("**/build/reports/kover/**/report.xml")
                include("**/build/reports/kover/xml/report.xml")
            }.files.joinToString(",") { it.absolutePath },
        )

        // If your analyzer expects JaCoCo XML key (fallback):
        property(
            "sonar.coverage.jacoco.xmlReportPaths",
            fileTree(project.rootDir) {
                include("**/build/reports/kover/**/report.xml")
                include("**/build/reports/kover/xml/report.xml")
            }.files.joinToString(",") { it.absolutePath },
        )

        property(
            "sonar.androidLint.reportPaths",
            fileTree(project.rootDir) {
                // AGP puts them under module/build/reports/lint/…
                include("**/build/reports/lint/lint-results*.xml")
                // Older/alternate naming
                include("**/build/reports/lint-results*.xml")
            }.files.joinToString(",") { it.absolutePath },
        )
    }
}
