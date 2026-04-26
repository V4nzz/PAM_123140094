package com.example.myprofileapp

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.myprofileapp.viewmodel.ProfileViewModel

/**
 * Root application composable.
 *
 * Creates (or retrieves from the composition) the [ProfileViewModel] and passes
 * [isDarkMode] from the ViewModel state into [MyProfileTheme] so the entire
 * theme reacts to the toggle.
 *
 * State ↓: ViewModel → App → MyProfileTheme & ProfileScreen
 * Events ↑: ProfileScreen → ViewModel
 */
@Composable
fun App() {
    val viewModel: ProfileViewModel = viewModel { ProfileViewModel() }
    val uiState by viewModel.uiState.collectAsState()

    MyProfileTheme(darkTheme = uiState.isDarkMode) {
        ProfileScreen(viewModel = viewModel)
    }
}
