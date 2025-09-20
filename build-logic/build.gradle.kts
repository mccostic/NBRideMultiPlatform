plugins {
    `kotlin-dsl`
    `java-gradle-plugin`
}

kotlin {
    jvmToolchain(17)
}

repositories {
    google()
    mavenCentral()
    gradlePluginPortal()
}

dependencies {
    // Keep these wired to the version catalog so build-logic stays in lockstep
    implementation(libs.kotlin.gradle.plugin)         // org.jetbrains.kotlin:kotlin-gradle-plugin:<kotlin>
    implementation(libs.android.gradle.plugin)        // com.android.tools.build:gradle:<agp>
    implementation(libs.compose.gradle.plugin)        // org.jetbrains.compose:compose-gradle-plugin:<composeMultiplatform>
    implementation(libs.kotlin.serialization.plugin)  // org.jetbrains.kotlin:kotlin-serialization:<kotlin>
    implementation("io.gitlab.arturbosch.detekt:detekt-gradle-plugin:1.23.8")
}
group = "com.dovoh.convention"
version = "0.1.0"

// Register your convention plugin so it can be applied by ID from feature modules
gradlePlugin {
    plugins {
        register("kmp-feature") {
            id = "com.dovoh.convention.kmp-feature"
            implementationClass = "com.dovoh.convention.KmpFeatureConventionPlugin"
            displayName = "KMP Feature Convention"
            description = "Shared configuration for feature modules (KMP + Android + Compose)"
        }
    }
}
