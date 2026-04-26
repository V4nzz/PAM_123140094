package com.example.myprofileapp.viewmodel

import androidx.lifecycle.ViewModel
import com.example.myprofileapp.Profile
import com.example.myprofileapp.data.ProfileUiState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

/**
 * ViewModel for the profile screen.
 *
 * Holds all UI state in a [MutableStateFlow] seeded with [Profile.sampleProfile].
 * The UI observes the read-only [uiState] and sends events back via the public
 * functions below — following the State ↓ / Events ↑ pattern.
 */
class ProfileViewModel : ViewModel() {

    // ── Private mutable state ──────────────────────────────────────────────────
    private val _uiState = MutableStateFlow(
        ProfileUiState(
            name     = Profile.sampleProfile.name,
            bio      = Profile.sampleProfile.bio,
            email    = Profile.sampleProfile.email,
            phone    = Profile.sampleProfile.phone,
            location = Profile.sampleProfile.location
        )
    )

    // ── Public read-only StateFlow exposed to the UI ───────────────────────────
    val uiState: StateFlow<ProfileUiState> = _uiState.asStateFlow()

    // ── Temporary edit buffers (local to the form, committed on save) ──────────
    private val _editName = MutableStateFlow(_uiState.value.name)
    val editName: StateFlow<String> = _editName.asStateFlow()

    private val _editBio = MutableStateFlow(_uiState.value.bio)
    val editBio: StateFlow<String> = _editBio.asStateFlow()

    // ── Event handlers ─────────────────────────────────────────────────────────

    /** Called while the user is typing in the name TextField (does NOT save yet). */
    fun updateName(newName: String) {
        _editName.update { newName }
    }

    /** Called while the user is typing in the bio TextField (does NOT save yet). */
    fun updateBio(newBio: String) {
        _editBio.update { newBio }
    }

    /** Commits the edited name & bio into the main [uiState] and closes the form. */
    fun saveProfile() {
        _uiState.update { current ->
            current.copy(
                name      = _editName.value,
                bio       = _editBio.value,
                isEditing = false
            )
        }
    }

    /** Toggles between light and dark mode. */
    fun toggleDarkMode() {
        _uiState.update { it.copy(isDarkMode = !it.isDarkMode) }
    }

    /**
     * Shows or hides the edit-profile form.
     * When opening the form, pre-fills the edit buffers with the current values.
     */
    fun toggleEditMode() {
        val currentlyEditing = _uiState.value.isEditing
        if (!currentlyEditing) {
            // Pre-fill edit buffers with the latest saved values
            _editName.value = _uiState.value.name
            _editBio.value  = _uiState.value.bio
        }
        _uiState.update { it.copy(isEditing = !currentlyEditing) }
    }
}
