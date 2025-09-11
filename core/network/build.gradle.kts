plugins {
    alias(libs.plugins.kotlinMultiplatform)
    alias(libs.plugins.androidKotlinMultiplatformLibrary)
    alias(libs.plugins.kotlinxSerialization)
}

kotlin {
    androidLibrary {
        namespace = "com.dovoh.android_mvi.core.network"
        compileSdk = 35
        minSdk = 24
    }

    val xcfName = "core:networkKit"

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
                implementation(libs.ktor.core)
                implementation(libs.ktor.logging)
                implementation(libs.ktor.content)
                implementation(libs.ktor.auth)
                implementation(libs.ktor.serialization.kotlinx.json)
                implementation(project(":core:logging"))
                implementation(project(":core:auth"))
                implementation(project(":core:common"))
            }
        }

        commonTest {
            dependencies {
                implementation(libs.kotlin.test)
            }
        }

        androidMain {
            dependencies {
                implementation(libs.ktor.okhttp)
            }
        }

        iosMain {
            dependencies {
                implementation(libs.ktor.darwin)
            }
        }
    }

}
