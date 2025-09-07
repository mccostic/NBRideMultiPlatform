plugins {
    alias(libs.plugins.kotlinMultiplatform)
    alias(libs.plugins.androidKotlinMultiplatformLibrary)
}

kotlin {
   androidLibrary {
        namespace = "com.dovoh.android_mvi.core.mvi"
        compileSdk = 35
        minSdk = 24

        withHostTestBuilder {
        }
    }

    val xcfName = "core:mviKit"

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
                implementation(libs.navigation.compose)
                implementation(libs.androidx.lifecycle.runtimeCompose)
                implementation(project(":core:common"))
                implementation(project(":core:network"))
            }
        }

        commonTest {
            dependencies {
                implementation(libs.kotlin.test)
            }
        }

        androidMain {
            dependencies {
                implementation(libs.lifecycle.viewmodel)
                implementation(libs.coroutines.core)
            }
        }


        iosMain {
            dependencies {

            }
        }
    }

}