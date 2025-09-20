package com.dovoh.convention

import com.android.build.api.dsl.LibraryExtension
import io.gitlab.arturbosch.detekt.Detekt
import io.gitlab.arturbosch.detekt.extensions.DetektExtension
import org.gradle.api.JavaVersion
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.artifacts.VersionCatalogsExtension
import org.gradle.kotlin.dsl.configure
import org.gradle.kotlin.dsl.getByType
import org.jetbrains.kotlin.gradle.dsl.JvmTarget
import org.jetbrains.kotlin.gradle.dsl.KotlinMultiplatformExtension

class KmpFeatureConventionPlugin : Plugin<Project> {

    override fun apply(target: Project) = with(target) {
        val libs = extensions.getByType<VersionCatalogsExtension>().named("libs")
        // Required plugins
        pluginManager.apply("org.jetbrains.kotlin.multiplatform")
        pluginManager.apply("com.android.library")
        pluginManager.apply("org.jetbrains.kotlin.plugin.serialization")
        pluginManager.apply("org.jetbrains.compose")
        pluginManager.apply("org.jetbrains.kotlin.plugin.compose")
        pluginManager.apply("org.jetbrains.kotlinx.kover")
        pluginManager.apply(libs.findPlugin("detekt").get().get().pluginId)

        // ---------------- Android config ----------------
        extensions.configure<LibraryExtension> {
            // e.g. :feature:login  -> com.dovoh.android_mvi.feature.login
            namespace = "com.dovoh.android_mvi" + project.path.replace(':', '.')
                .removePrefix(".") // just in case

            compileSdk = libs.findVersion("android-compileSdk").get().requiredVersion.toInt()

            defaultConfig {
                minSdk = libs.findVersion("android-minSdk").get().requiredVersion.toInt()
                consumerProguardFiles("consumer-rules.pro")
            }

            compileOptions {
                sourceCompatibility = JavaVersion.VERSION_17
                targetCompatibility = JavaVersion.VERSION_17
            }
        }

        // ---------------- KMP targets + source sets ----------------
        extensions.configure<KotlinMultiplatformExtension> {
            androidTarget {
                compilerOptions {
                    jvmTarget.set(JvmTarget.JVM_17)
                }
            }
            // use JDK 17 toolchain
            jvmToolchain(17)

            val xcfName = project.path.removePrefix(":").replace(':', '_') + "Kit"
            iosX64 { binaries.framework { baseName = xcfName } }
            iosArm64 { binaries.framework { baseName = xcfName } }
            iosSimulatorArm64 { binaries.framework { baseName = xcfName } }

            // ---- commonMain deps
            sourceSets.named("commonMain").configure {
                dependencies {
                    // Compose MPP

                    implementation(libs.findLibrary("compose-runtime").get())
                    implementation(libs.findLibrary("compose-foundation").get())
                    implementation(libs.findLibrary("compose-material3").get())
                    implementation(libs.findLibrary("navigation-compose").get())           // DI / coroutines
                    implementation(libs.findLibrary("koin-compose-viewmodel").get())
                    implementation(libs.findLibrary("koin-core").get())
                    implementation(libs.findLibrary("coroutines-core").get())

                    // Ktor core + JSON
                    implementation(libs.findLibrary("ktor-core").get())
                    implementation(libs.findLibrary("ktor-serialization-kotlinx-json").get())

                    // shared core modules
                    implementation(project(":core:common"))
                    implementation(project(":core:logging"))
                    api(project(":core:mvi"))
                    implementation(project(":core:network"))
                    implementation(project(":core:auth"))
                    implementation(project(":core:navigation"))
                    implementation(project(":core:designsystem"))
                }
            }

            // ---- commonTest deps
            sourceSets.named("commonTest").configure {
                dependencies {
                    implementation(libs.findLibrary("kotlin-test").get())
                    implementation(libs.findLibrary("coroutines-test").get())
                    implementation(libs.findLibrary("koin-test").get())
                    implementation(libs.findLibrary("turbine").get())

                    implementation(libs.findLibrary("kotlin-test").get())
                    implementation(libs.findLibrary("kotlin-test-annotations-common").get())

                    // Choose ONE: Koin test OR MockK common
                    // implementation(libs.findLibrary("koinTest").get())
                }
            }
        }

        dependencies.add("detektPlugins", libs.findLibrary("detekt-formatting").get())

        extensions.configure<DetektExtension>("detekt") {
            buildUponDefaultConfig = true
            parallel = true
            config.setFrom(files(rootProject.file("config/detekt/detekt.yml")))
            baseline = rootProject.file("config/detekt/baseline.xml").takeIf { it.exists() }

            // If you set sources, use setFrom instead of direct assign:
            source.setFrom(
                files(
                    "src/commonMain/kotlin",
                    "src/commonTest/kotlin",
                    "src/androidMain/kotlin",
                    "src/main/kotlin",
                    "src/test/kotlin"
                )
            )

            basePath = rootProject.projectDir.absolutePath
        }

        tasks.withType(Detekt::class.java).configureEach {
            // Task-level inputs (ok to use setSource here)
            setSource(files(projectDir))
            include("**/*.kt", "**/*.kts")
            exclude("**/build/**", "**/.gradle/**", "**/generated/**")

            reports {
                xml.required.set(true)
                html.required.set(true)
                txt.required.set(false)
                sarif.required.set(false)
            }
        }
    }
}
