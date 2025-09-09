import kotlinx.kover.features.jvm.KoverLegacyFeatures.verify
import org.jetbrains.compose.desktop.application.dsl.TargetFormat
import org.jetbrains.kotlin.gradle.dsl.JvmTarget

plugins {
    alias(libs.plugins.kotlinMultiplatform)
    alias(libs.plugins.androidApplication)
    alias(libs.plugins.composeMultiplatform)
    alias(libs.plugins.composeCompiler)
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

        val versionNameBase = (project.findProperty("VERSION_NAME_BASE") as String?) ?: "1.0.0.0"

        val buildNumber = (project.findProperty("buildNumber") as String?)
            ?: System.getenv("BUILD_NUMBER")
            ?: "1"

        val buildTypeSuffix = (project.findProperty("BUILD_TYPE") as String?)
            ?: System.getenv("BUILD_TYPE")
            ?: "debug"

        versionCode = buildNumber.takeLast(9).toIntOrNull() ?: 1
        versionName = "$versionNameBase-$buildTypeSuffix"
    }

    packaging {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
    }

    // ----- CI signing detection (updated) -----
    val workspace = providers.environmentVariable("GITHUB_WORKSPACE").orNull
    val ciKeystore = workspace
        ?.let { file("$it/ci/ci-keystore.jks") }
        ?: file("${rootDir}/ci/ci-keystore.jks") // local fallback for dry-run

    val useCiSigning =
        providers.environmentVariable("ANDROID_KEYSTORE_PASSWORD").isPresent &&
            providers.environmentVariable("ANDROID_KEY_ALIAS").isPresent &&
            providers.environmentVariable("ANDROID_KEY_PASSWORD").isPresent &&
            ciKeystore.exists()

    signingConfigs {
        if (useCiSigning) {
            create("ci") {
                storeFile = ciKeystore
                storePassword = providers.environmentVariable("ANDROID_KEYSTORE_PASSWORD").orNull
                keyAlias = providers.environmentVariable("ANDROID_KEY_ALIAS").orNull
                keyPassword = providers.environmentVariable("ANDROID_KEY_PASSWORD").orNull
                enableV3Signing = true
                enableV4Signing = true
            }
        }
    }

    buildTypes {
        getByName("debug") {
            // default debug signing
        }

        getByName("release") {
            isMinifyEnabled = false
            if (useCiSigning) {
                signingConfig = signingConfigs.getByName("ci")
            } else {
                // Leave unsigned in local or when CI secrets are absent
                println("Release: CI keystore not provided -> using default/unsigned build.")
            }
        }

        maybeCreate("integration").apply {
            initWith(getByName("debug"))
            isDebuggable = true
            matchingFallbacks += listOf("debug")
        }

        maybeCreate("beta")
        getByName("beta") {
            initWith(getByName("debug"))
            isMinifyEnabled = false
            matchingFallbacks += listOf("debug")
            if (useCiSigning) {
                signingConfig = signingConfigs.getByName("ci")
            } else {
                println("Beta: CI keystore not provided -> using DEBUG signing.")
            }
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

