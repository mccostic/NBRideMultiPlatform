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
        versionCode = 1
        versionName = "1.0"
    }
    packaging {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
    }

    signingConfigs {
        create("ci") {
            val keystorePath = project.rootDir.resolve("ci/ci-keystore.jks")
            if (keystorePath.exists()) {
                storeFile = keystorePath
                storePassword = System.getenv("ANDROID_KEYSTORE_PASSWORD") ?: ""
                keyAlias = System.getenv("ANDROID_KEY_ALIAS") ?: ""
                keyPassword = System.getenv("ANDROID_KEY_PASSWORD") ?: ""
            }
        }
    }

    buildTypes {
        getByName("release") {
            isMinifyEnabled = false
        }

        maybeCreate("beta")
        getByName("beta") {
            signingConfig = signingConfigs.getByName("ci")
            initWith(getByName("debug"))
            isMinifyEnabled = false
            matchingFallbacks += listOf("debug")
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


