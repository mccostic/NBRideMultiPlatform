plugins {
    alias(libs.plugins.kmpFeature)
}

kotlin {
    sourceSets {
        val commonMain by getting {
            dependencies {

            }
        }
        /*val androidMain by getting {
            dependencies {

            }
        }
        val iosMain by getting {
            dependencies {

            }
        }*/
    }
}


android {
    buildFeatures.buildConfig = true
    packaging {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
            merges += "META-INF/LICENSE.md"
            merges += "META-INF/LICENSE-notice.md"
        }
    }
}

