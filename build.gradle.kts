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
}

// (Optional but nice): make `sonarqube` wait for tests + merged coverage
tasks.named("sonarqube") {
    // If you use Kover merged reports (recommended for multi-module),
    // ensure coverage is generated before analysis:
    dependsOn(
        // unit tests across modules
        "testDebugUnitTest", // or just "test" if you prefer
        // merged coverage report
        "koverMergedXmlReport",
    )
}

sonar {
    properties {
        // Basics (set key/name to whatever you use in SonarQube/Cloud)
        property("sonar.projectKey", "your_org_NBRideMultiPlatform")
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
        property("sonar.kotlin.coverage.reportPaths", "${layout.buildDirectory}/reports/kover/merged/xml/report.xml")

        // (Optional fallback for older analyzers)
        // property("sonar.coverage.jacoco.xmlReportPaths", "$buildDir/reports/kover/merged/xml/report.xml")
    }
}
