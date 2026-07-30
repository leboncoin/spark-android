plugins {
    alias(libs.plugins.android.test)
    alias(libs.plugins.spark.android)
    alias(libs.plugins.baselineprofile)
}

android {
    namespace = "com.adevinta.spark.catalog.baselineprofile"

    defaultConfig {
        minSdk = 28

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        // Enables Perfetto SDK (Compose) composition tracing. Use only with WARM/HOT startup:
        // the cold-start path writes a properties file to /sdcard which fails on API 34+.
        testInstrumentationRunnerArguments["androidx.benchmark.perfettoSdkTracing.enable"] = "true"
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }


    testOptions.managedDevices.allDevices {
        create<com.android.build.api.dsl.ManagedVirtualDevice>("pixel6Api33") {
            device = "Pixel 6"
            apiLevel = 33
            systemImageSource = "aosp"
        }
    }

    targetProjectPath = ":catalog"

}

baselineProfile {
    // This specifies the managed devices to use that you run the tests on.
    managedDevices.clear()
    managedDevices += "pixel6Api33"

    // Don't use a connected device but rely on a GMD for consistency between local and CI builds.
    useConnectedDevices = false
}

dependencies {
    implementation(libs.androidx.test.junit)
    implementation(libs.androidx.espresso.core)
    implementation(libs.androidx.uiautomator)
    implementation(libs.androidx.benchmark.macro.junit4)
    implementation(libs.androidx.tracing.perfetto)
    // binary must stay in this test-only module — do NOT add to :catalog or :spark production deps
    implementation(libs.androidx.tracing.perfetto.binary)
}

androidComponents {
    onVariants { v ->
        val artifactsLoader = v.artifacts.getBuiltArtifactsLoader()
        v.instrumentationRunnerArguments.put(
            "targetAppId",
            v.testedApks.map { artifactsLoader.load(it)?.applicationId }
        )
    }
}
