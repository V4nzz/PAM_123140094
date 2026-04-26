plugins {
    // Android plugin
    alias(libs.plugins.androidApplication) apply false
    alias(libs.plugins.androidLibrary) apply false
    // Kotlin multiplatform
    alias(libs.plugins.kotlinMultiplatform) apply false
    // Compose multiplatform
    alias(libs.plugins.composeMultiplatform) apply false
    alias(libs.plugins.composeCompiler) apply false
}
