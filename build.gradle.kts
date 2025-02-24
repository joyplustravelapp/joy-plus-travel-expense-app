plugins {
    // Apply the kotlin plugin version to use across all the modules
    kotlin("multiplatform").apply(false).version("1.9.0")
    kotlin("android").apply(false).version("1.9.0")
    id("com.android.application").apply(false).version("8.0.0")
    id("com.android.library").apply(false).version("8.0.0")
    id("org.jetbrains.compose").apply(false).version("1.5.0")
}

tasks.register("clean", Delete::class) {
    delete(rootProject.buildDir)
}