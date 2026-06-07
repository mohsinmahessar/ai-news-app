# SmartReader AI — ProGuard / R8 rules

# Keep Gson model classes (AI response DTOs) — reflection based serialization.
-keep class com.smartreader.ai.data.remote.ai.** { *; }
-keepclassmembers class com.smartreader.ai.data.remote.ai.** { *; }

# Retrofit
-keepattributes Signature, InnerClasses, EnclosingMethod
-keepattributes RuntimeVisibleAnnotations, RuntimeVisibleParameterAnnotations
-dontwarn retrofit2.**
-keep class retrofit2.** { *; }

# OkHttp
-dontwarn okhttp3.**
-dontwarn okio.**

# Room
-keep class * extends androidx.room.RoomDatabase { *; }

# Hilt
-dontwarn dagger.hilt.**

# PDFium
-keep class com.shockwave.** { *; }

# Keep Kotlin coroutines metadata
-dontwarn kotlinx.coroutines.**
