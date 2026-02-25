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

-keep class com.adevinta.spark.tools.preview.UserProProvider { *; }

# Keep all spark-icons resources explicitly in the catalog app
-keep class com.adevinta.spark.icons.R$drawable {
    public static <fields>;
}

# Keep colors as we use reflection to display them in examples
-keepclasseswithmembers class com.adevinta.spark.tokens.SparkColors {
    *;
}

# Rules for Filekit
-keep class com.sun.jna.** { *; }
-keep class * implements com.sun.jna.** { *; }
