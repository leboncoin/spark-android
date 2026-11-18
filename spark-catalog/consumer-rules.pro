# Keep all spark-icons resources explicitly in the catalog app
-keep class com.adevinta.spark.icons.R$drawable {
    public static <fields>;
}

# Keep colors as we use reflection to display them in examples
-keepclassmembers class com.adevinta.spark.tokens.SparkColors { long get*(); }
