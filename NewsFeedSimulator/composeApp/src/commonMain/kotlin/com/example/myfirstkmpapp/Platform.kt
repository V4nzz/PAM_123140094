package com.example.myfirstkmpapp

/**
 * Represents a platform with a human-readable name.
 */
data class Platform(val name: String)

/**
 * expect declaration — each platform provides its own actual implementation.
 */
expect fun getPlatform(): Platform
