# ---- Warnings to suppress (targeted) ----
-dontwarn javax.annotation.**
-dontwarn kotlin.reflect.**
-dontwarn org.jetbrains.annotations.**

# Keep Kotlin metadata (helps reflection/serialization)
-keep class kotlin.Metadata { *; }

# ---- Jetpack Compose ----
# AGP/R8 already include Compose rules, these are safe extras:
-keep class androidx.compose.** { *; }
-keep class androidx.activity.compose.** { *; }
# If you reflect on @Composable methods (usually not needed):
-keepclassmembers class * {
    @androidx.compose.runtime.Composable <methods>;
}

# ---- Coroutines ----
-dontwarn kotlinx.coroutines.**

# ---- Koin ----
-dontwarn org.koin.**
# If you do reflective DI, keeping ctors is a safe (broad) default:
-keepclassmembers class * {
    public <init>(...);
}

# ---- Kotlinx Serialization ----
# Keep generated serializers and annotated classes
-keepclassmembers class ** {
    @kotlinx.serialization.Serializable *;
}
-keep class **$$serializer { *; }
-keepclassmembers class kotlinx.serialization.** { *; }
-dontwarn kotlinx.serialization.**

# ---- Ktor ----
-dontwarn io.ktor.**
-dontwarn kotlinx.io.**

# ---- Parcelable (@Parcelize) ----
-keep class kotlinx.parcelize.** { *; }
-keepclassmembers class * implements android.os.Parcelable {
    public static final ** CREATOR;
}

# ---- Your app packages ----
-keep class com.dovoh.android_mvi.** { *; }

# ---- Optional: strip android.util.Log calls in release ----
-assumenosideeffects class android.util.Log {
    public static *** d(...);
    public static *** v(...);
    public static *** i(...);
    public static *** w(...);
    public static *** e(...);
}