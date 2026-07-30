# Keep trace section names so they appear in Perfetto traces during benchmarks.
-keep class androidx.tracing.Trace { *; }
-keep class androidx.compose.ui.util.TraceKt { *; }

# Prevent inlining of trace() calls which would lose the section names.
-keepclassmembers class * {
    *** trace(...);
}

# Keep tracing-perfetto classes (loaded reflectively by the benchmark framework).
-keep class androidx.tracing.perfetto.** { *; }

# Keep the startup initializer that registers the perfetto tracing broadcast receiver.
-keep class androidx.startup.** { *; }
-keep class androidx.tracing.** { *; }
