import org.jetbrains.compose.desktop.application.dsl.TargetFormat
import org.jetbrains.kotlin.gradle.dsl.JvmTarget

plugins {
    alias(libs.plugins.kotlinMultiplatform)
    alias(libs.plugins.androidApplication)
    alias(libs.plugins.composeMultiplatform)
    alias(libs.plugins.composeCompiler)
    alias(libs.plugins.kover)
    alias(libs.plugins.firebaseAppDistribution)
    id("com.google.gms.google-services")
}

kotlin {
    androidTarget {
        compilerOptions {
            jvmTarget.set(JvmTarget.JVM_17)
        }
    }
    
    listOf(
        iosArm64(),
        iosSimulatorArm64()
    ).forEach { iosTarget ->
        iosTarget.binaries.framework {
            baseName = "ComposeApp"
            isStatic = true
        }
    }
    
    sourceSets {
        androidMain.dependencies {
            implementation(compose.preview)
            implementation(libs.androidx.activity.compose)
            implementation(libs.ktor.okhttp)
            implementation(project.dependencies.platform("com.google.firebase:firebase-bom:34.2.0"))
        }
        commonMain.dependencies {
            implementation(compose.runtime)
            implementation(compose.foundation)
            implementation(compose.material3)
            implementation(compose.ui)
            implementation(compose.components.resources)
            implementation(compose.components.uiToolingPreview)
            implementation(libs.androidx.lifecycle.viewmodelCompose)
            implementation(libs.androidx.lifecycle.runtimeCompose)
            implementation(libs.koin.compose)
            implementation(libs.koin.androidx.compose)
            implementation(libs.navigation.compose)
            implementation(libs.koin.compose.viewmodel)
            implementation(project(":core:logging"))
            implementation(project(":di"))
            implementation(project(":feature:login"))
            implementation(project(":feature:register"))
            implementation(project(":feature:home"))
            implementation(project(":core:common"))
            implementation(project(":core:navigation"))
        }
        commonTest.dependencies {
            implementation(libs.kotlin.test)
        }
        iosMain.dependencies {
            implementation(libs.ktor.darwin)
        }
    }
}

android {
    namespace = "org.example.project.nbride"
    compileSdk = libs.versions.android.compileSdk.get().toInt()

    defaultConfig {
        applicationId = "org.example.project.nbride"
        minSdk = libs.versions.android.minSdk.get().toInt()
        targetSdk = libs.versions.android.targetSdk.get().toInt()
        val versionNameBase = (project.findProperty("versionNameBase") as String?) ?: "1.2.3.0"
        val buildNumber = (project.findProperty("buildNumber") as String?)
            ?: System.getenv("BUILD_NUMBER")
            ?: "0"

        versionCode = buildNumber.takeLast(9).toIntOrNull() ?: 1
        versionName = "$versionNameBase ($buildNumber)"
    }
    packaging {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
    }

    val useCiSigning = providers.environmentVariable("ANDROID_KEYSTORE_PASSWORD").isPresent &&
            providers.environmentVariable("ANDROID_KEY_ALIAS").isPresent &&
            providers.environmentVariable("ANDROID_KEY_PASSWORD").isPresent &&
            file("${rootDir}/ci/ci-keystore.jks").exists()

    signingConfigs {
        if (useCiSigning) {
            create("ci") {
                storeFile = file("${rootDir}/ci/ci-keystore.jks")
                storePassword = System.getenv("ANDROID_KEYSTORE_PASSWORD")
                keyAlias = System.getenv("ANDROID_KEY_ALIAS")
                keyPassword = System.getenv("ANDROID_KEY_PASSWORD")
            }
        }
    }

    buildTypes {
        getByName("debug") {
            // debug signing as usual
        }

        maybeCreate("beta")
        getByName("beta") {
            initWith(getByName("debug"))
            isMinifyEnabled = false
            matchingFallbacks += listOf("debug")
            if (useCiSigning) {
                signingConfig = signingConfigs.getByName("ci")
            } else {
                // No CI signing available -> keep debug keystore
                println("Using DEBUG signing for beta (CI keystore not provided).")
            }
        }

        getByName("release") {
            isMinifyEnabled = false
        }
    }


    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }
}
firebaseAppDistribution {

}

// Optional: per-variant tweaks (example for 'beta')
tasks.matching { it.name == "appDistributionUploadBeta" }.configureEach {
    // nothing needed; exists so you can attach logic if you want
}

dependencies {
    debugImplementation(compose.uiTooling)
}
kover {
    reports {
        filters {
            excludes {
                // Example: exclude Compose-generated classes
                annotatedBy("androidx.compose.runtime.Composable")
                packages("com.dovoh.android_mvi.di")
            }
        }
    }
}


