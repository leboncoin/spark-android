# Prevent inlining of trace() calls which would lose the section names.
-keepclassmembers class * {
    *** trace(...);
}

# Keep tracing classes, including tracing-perfetto, which the benchmark framework loads reflectively.
-keep class androidx.tracing.** { *; }
