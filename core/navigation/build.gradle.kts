plugins {
    alias(libs.plugins.kotlinMultiplatform)
    alias(libs.plugins.androidKotlinMultiplatformLibrary)
    alias(libs.plugins.kotlinxSerialization)
}

kotlin {
    androidLibrary {
        namespace = "com.dovoh.android_mvi.core.navigation"
        compileSdk = 35
        minSdk = 24
    }
    val xcfName = "core:navigationKit"
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
                implementation(libs.ktor.serialization.kotlinx.json)
                api(libs.material.icons.extended)
                api(libs.material.icons.core)
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
