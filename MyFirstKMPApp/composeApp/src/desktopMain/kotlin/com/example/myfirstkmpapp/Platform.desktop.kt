package com.example.myfirstkmpapp

actual fun getPlatform(): Platform = Platform(
    name = "Desktop (JVM ${System.getProperty("java.version")})"
)
