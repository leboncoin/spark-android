# This is generated automatically by the Android Gradle plugin.
-dontwarn org.bouncycastle.jsse.BCSSLParameters
-dontwarn org.bouncycastle.jsse.BCSSLSocket
-dontwarn org.bouncycastle.jsse.provider.BouncyCastleJsseProvider
-dontwarn org.conscrypt.Conscrypt$Version
-dontwarn org.conscrypt.Conscrypt
-dontwarn org.conscrypt.ConscryptHostnameVerifier
-dontwarn org.openjsse.javax.net.ssl.SSLParameters
-dontwarn org.openjsse.javax.net.ssl.SSLSocket
-dontwarn org.openjsse.net.ssl.OpenJSSE
-dontobfuscate
-keepattributes LineNumberTable,SourceFile
-renamesourcefileattribute SourceFile
-verbose

# Rules for Filekit
-keep class com.sun.jna.** { *; }
-keep class * implements com.sun.jna.** { *; }

# Prevent R8 full-mode class merging of compose-animation internals into unrelated classes.
# Without this, ART verify mode (used by CompilationMode.None() in benchmarks) rejects the
# merged bytecode due to type mismatches between ScaleToBoundsImpl and RemeasureImpl.
-keep class androidx.compose.animation.ScaleToBoundsImpl
-keep class androidx.compose.animation.RemeasureImpl
