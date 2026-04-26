package com.example.myprofileapp.data

/**
 * Immutable UI state for the profile screen.
 * All state mutations are done via [copy], enforcing unidirectional data flow.
 *
 * @param name       Display name of the user.
 * @param bio        Short tagline / bio.
 * @param email      Contact email address.
 * @param phone      Contact phone number.
 * @param location   Physical location string.
 * @param isDarkMode Whether the app is currently in dark mode.
 * @param isEditing  Whether the edit-profile form is currently visible.
 */
data class ProfileUiState(
    val name: String,
    val bio: String,
    val email: String,
    val phone: String,
    val location: String,
    val isDarkMode: Boolean = false,
    val isEditing: Boolean = false
)
