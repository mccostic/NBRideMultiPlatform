plugins {
    alias(libs.plugins.kmpFeature)
}

kotlin {
    sourceSets {
        commonMain {
        }
        androidMain {
            dependencies {
                implementation(libs.google.maps.compose)
                implementation(libs.google.play.services.maps)
            }
        }
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
