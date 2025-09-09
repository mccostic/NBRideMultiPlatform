plugins {
    alias(libs.plugins.kotlinMultiplatform)
    alias(libs.plugins.androidKotlinMultiplatformLibrary)
}

kotlin {

    androidLibrary {
        namespace = "com.dovoh.android_mvi.core.auth"
        compileSdk = 35
        minSdk = 24

        withHostTestBuilder {
        }
    }

    val xcfName = "core:authKit"

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
                implementation(libs.coroutines.core)
            }
        }

        commonTest {
            dependencies {
                implementation(libs.kotlin.test) // assertions @Test
                implementation(libs.kotlin.test.annotations.common) // optional
                implementation(libs.coroutines.test)                // runTest, TestScope, etc.
                implementation(libs.turbine)                        // Flow testing (optional for later)
                implementation(libs.koin.test)                      // if you test DI (optional)
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
