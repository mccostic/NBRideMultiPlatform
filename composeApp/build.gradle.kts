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

    // ----------------- SIGNING (Only use repo-local keystore at ci/ci-keystore.jks) -----------------
    // No GitHub-specific env like GITHUB_WORKSPACE is referenced here.
    val keystoreFile = file("${rootDir}/ci/ci-keystore.jks")

    // Read from environment variables first, then fall back to gradle.properties if present
    fun envOrProp(name: String): String? =
        providers.environmentVariable(name).orNull ?: (findProperty(name) as String?)

    val storePass = envOrProp("ANDROID_KEYSTORE_PASSWORD")
    val keyAlias  = envOrProp("ANDROID_KEY_ALIAS")
    val keyPass   = envOrProp("ANDROID_KEY_PASSWORD")

    val haveSecrets = !storePass.isNullOrBlank() && !keyAlias.isNullOrBlank() && !keyPass.isNullOrBlank()
    val canSign = keystoreFile.exists() && haveSecrets

    signingConfigs {
        if (canSign) {
            create("ci") {
                storeFile = keystoreFile
                storePassword = storePass
                this.keyAlias = keyAlias
                keyPassword = keyPass
                enableV3Signing = true
                enableV4Signing = true
            }
        }
    }
    // -----------------------------------------------------------------------------------------------

    buildTypes {
        getByName("debug") {
            isMinifyEnabled = false
        }

        getByName("release") {
            isMinifyEnabled = true
            isShrinkResources = true
            isDebuggable = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                file("proguard-rules.pro")
            )
            if (canSign) {
                signingConfig = signingConfigs.getByName("ci")
            } else {
                // Keep behavior minimal; no GitHub message, just note that release will be unsigned.
                println("Release signing not configured: using unsigned release (keystore missing or secrets not set).")
            }
        }

        maybeCreate("integration").apply {
            initWith(getByName("debug"))
            // Harden integration builds if you share them externally
            isMinifyEnabled = true
            isShrinkResources = true
            isDebuggable = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                file("proguard-rules.pro")
            )
            matchingFallbacks += listOf("debug")
            if (canSign) {
                signingConfig = signingConfigs.findByName("ci")
            }
        }

        maybeCreate("beta").apply {
            initWith(getByName("debug"))
            isMinifyEnabled = true
            isShrinkResources = true
            isDebuggable = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                file("proguard-rules.pro")
            )
            matchingFallbacks += listOf("debug")
            if (canSign) {
                signingConfig = signingConfigs.getByName("ci")
            }
        }
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }
}

firebaseAppDistribution {
    // (unchanged)
}

// Optional: per-variant tweaks (example for 'beta')
tasks.matching { it.name == "appDistributionUploadBeta" }.configureEach {
    // nothing needed; exists so you can attach logic if you want
}

dependencies {
    debugImplementation(compose.uiTooling)
}
