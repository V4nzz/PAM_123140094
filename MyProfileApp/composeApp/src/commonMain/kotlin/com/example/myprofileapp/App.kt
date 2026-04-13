package com.example.myprofileapp

import androidx.compose.runtime.Composable

/**
 * Root application composable.
 * Wraps the profile screen with the custom Material 3 theme.
 */
@Composable
fun App() {
    MyProfileTheme {
        ProfileScreen(profile = Profile.sampleProfile)
    }
}
