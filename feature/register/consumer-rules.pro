# Keep public API or classes used reflectively by the library
# (examples—trim to only what you really need)
-keep class com.dovoh.android_mvi.core.** { *; }
-keep class com.dovoh.android_mvi.feature.** { *; }

# If the library has @Serializable models:
-keepclassmembers class ** { @kotlinx.serialization.Serializable *; }
-keep class **$$serializer { *; }