package com.example.myfirstkmpapp

import android.os.Build

actual fun getPlatform(): Platform = Platform(
    name = "Android ${Build.VERSION.RELEASE} (API ${Build.VERSION.SDK_INT})"
)
