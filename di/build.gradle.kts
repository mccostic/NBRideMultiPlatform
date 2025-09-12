plugins {
    alias(libs.plugins.kotlinMultiplatform)
    alias(libs.plugins.androidKotlinMultiplatformLibrary)
    alias(libs.plugins.buildKonfig)
}

kotlin {
    androidLibrary {
        namespace = "com.dovoh.android_mvi.di"
        compileSdk = 35
        minSdk = 24
    }
    val xcfName = "diKit"

    iosX64 {
        binaries.framework {
            baseName = xcfName
        }
    }

    iosArm64 {
        binaries.framework {
            baseName = xcfName
        }
    }

    iosSimulatorArm64 {
        binaries.framework {
            baseName = xcfName
        }
    }
    sourceSets {
        commonMain {
            dependencies {
                implementation(libs.kotlin.stdlib)
                implementation(libs.koin.core)
                implementation(libs.ktor.core)
                implementation(libs.lifecycle.viewmodel)
                implementation(project(":core:common"))
                implementation(project(":core:network"))
                implementation(project(":core:auth"))
                implementation(project(":core:mvi"))
                implementation(project(":feature:login"))
                implementation(project(":feature:register"))
            }
        }

        commonTest {
            dependencies {
                implementation(libs.kotlin.test)
            }
        }

        androidMain {
            dependencies {

            }
        }

        iosMain {
            dependencies {

            }
        }
    }

}
buildkonfig {
    packageName = "com.dovoh.android_mvi.di.core"

    defaultConfigs {
        val baseUrl: String = project.findProperty("BASE_URL") as? String
            ?: "https://dummyjson.com"

        buildConfigField(
            com.codingfeline.buildkonfig.compiler.FieldSpec.Type.STRING,
            "BASE_URL",
            baseUrl
        )
    }
}
