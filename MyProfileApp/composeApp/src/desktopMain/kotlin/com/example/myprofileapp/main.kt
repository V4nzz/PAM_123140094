package com.example.myprofileapp

import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import androidx.compose.ui.window.rememberWindowState
import androidx.compose.ui.unit.dp

fun main() = application {
    Window(
        onCloseRequest = ::exitApplication,
        title = "My Profile App",
        state = rememberWindowState(width = 420.dp, height = 800.dp)
    ) {
        App()
    }
}
