plugins {
    // Android plugin
    id("com.android.application") version "8.5.2" apply false
    id("com.android.library") version "8.5.2" apply false
    // Kotlin multiplatform
    kotlin("multiplatform") version "2.0.21" apply false
    // Compose multiplatform
    id("org.jetbrains.compose") version "1.7.0" apply false
    id("org.jetbrains.kotlin.plugin.compose") version "2.0.21" apply false
}
