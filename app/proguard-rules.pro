# Proguard rules for Retrofit & Gson serialization
-keepclassmembers class * {
    @com.google.gson.annotations.SerializedName <fields>;
}
-keep class au.edu.jcu.assessment.utilityapp.data.model.** { *; }
