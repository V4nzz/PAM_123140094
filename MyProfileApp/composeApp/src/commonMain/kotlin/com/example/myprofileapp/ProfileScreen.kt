package com.example.myprofileapp

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.DarkMode
import androidx.compose.material.icons.outlined.Edit
import androidx.compose.material.icons.outlined.Email
import androidx.compose.material.icons.outlined.LocationOn
import androidx.compose.material.icons.outlined.Phone
import androidx.compose.material.icons.outlined.Save
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.myprofileapp.data.ProfileUiState
import com.example.myprofileapp.ui.LabeledTextField
import com.example.myprofileapp.viewmodel.ProfileViewModel

/**
 * Main profile screen — fully stateless with respect to business data.
 *
 * All state comes from [viewModel] via [ProfileViewModel.uiState] (State ↓).
 * All user interactions are forwarded back to [viewModel] functions (Events ↑).
 *
 * @param viewModel The ViewModel that owns all profile UI state.
 * @param modifier  Optional [Modifier] for the root layout.
 */
@Composable
fun ProfileScreen(
    viewModel: ProfileViewModel,
    modifier: Modifier = Modifier
) {
    // ── Collect state from ViewModel ───────────────────────────────────────────
    val uiState by viewModel.uiState.collectAsState()
    val editName by viewModel.editName.collectAsState()
    val editBio  by viewModel.editBio.collectAsState()

    // ── Local UI-only state (show/hide contact info) ───────────────────────────
    var showDetails by remember { mutableStateOf(false) }

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 20.dp, vertical = 32.dp)
        ) {

            // ── Dark Mode Row ───────────────────────────────────────────────────
            DarkModeToggleRow(
                isDarkMode = uiState.isDarkMode,
                onToggle   = { viewModel.toggleDarkMode() }
            )

            Spacer(modifier = Modifier.height(16.dp))

            // ── Profile Card ────────────────────────────────────────────────────
            ProfileCard {

                // Header: avatar + name + bio
                ProfileHeader(
                    name     = uiState.name,
                    bio      = uiState.bio,
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(modifier = Modifier.height(20.dp))

                // ── Edit Profile Button ─────────────────────────────────────────
                OutlinedButton(
                    onClick  = { viewModel.toggleEditMode() },
                    modifier = Modifier.fillMaxWidth(),
                    shape    = RoundedCornerShape(12.dp)
                ) {
                    Icon(
                        imageVector        = if (uiState.isEditing) Icons.Outlined.Save else Icons.Outlined.Edit,
                        contentDescription = null,
                        modifier           = Modifier.padding(end = 8.dp)
                    )
                    Text(
                        text  = if (uiState.isEditing) "Cancel Edit" else "Edit Profile",
                        style = MaterialTheme.typography.labelLarge
                    )
                }

                // ── Animated Edit Form ──────────────────────────────────────────
                AnimatedVisibility(
                    visible = uiState.isEditing,
                    enter   = expandVertically(animationSpec = tween(400)) +
                              fadeIn(animationSpec = tween(400)),
                    exit    = shrinkVertically(animationSpec = tween(300)) +
                              fadeOut(animationSpec = tween(300))
                ) {
                    EditProfileSection(
                        name          = editName,
                        bio           = editBio,
                        onNameChange  = { viewModel.updateName(it) },
                        onBioChange   = { viewModel.updateBio(it) },
                        onSave        = { viewModel.saveProfile() }
                    )
                }

                Spacer(modifier = Modifier.height(16.dp))

                // ── Show / Hide Contact Info Button ─────────────────────────────
                Button(
                    onClick = { showDetails = !showDetails },
                    modifier = Modifier.fillMaxWidth(),
                    shape    = RoundedCornerShape(12.dp),
                    colors   = ButtonDefaults.buttonColors(
                        containerColor = MaterialTheme.colorScheme.primary
                    )
                ) {
                    Text(
                        text      = if (showDetails) "Hide Contact Info" else "Show Contact Info",
                        style     = MaterialTheme.typography.labelLarge,
                        modifier  = Modifier.padding(vertical = 4.dp)
                    )
                }

                // ── Animated Contact Details ────────────────────────────────────
                AnimatedVisibility(
                    visible = showDetails,
                    enter   = expandVertically(animationSpec = tween(400)) +
                              fadeIn(animationSpec = tween(400)),
                    exit    = shrinkVertically(animationSpec = tween(300)) +
                              fadeOut(animationSpec = tween(300))
                ) {
                    ContactInfoSection(uiState = uiState)
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            // Footer text
            Text(
                text  = "Built with Compose Multiplatform",
                style = MaterialTheme.typography.labelMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}

// ── Private sub-composables (stateless) ────────────────────────────────────────

/**
 * Dark mode switch row — fully stateless, driven by [isDarkMode] from ViewModel.
 */
@Composable
private fun DarkModeToggleRow(
    isDarkMode: Boolean,
    onToggle: () -> Unit
) {
    Row(
        verticalAlignment     = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween,
        modifier              = Modifier
            .fillMaxWidth()
            .padding(horizontal = 4.dp)
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Icon(
                imageVector        = Icons.Outlined.DarkMode,
                contentDescription = "Dark mode",
                tint               = MaterialTheme.colorScheme.primary,
                modifier           = Modifier.padding(end = 8.dp)
            )
            Text(
                text  = if (isDarkMode) "Dark Mode" else "Light Mode",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onBackground
            )
        }
        Switch(
            checked         = isDarkMode,
            onCheckedChange = { onToggle() },
            colors          = SwitchDefaults.colors(
                checkedThumbColor   = MaterialTheme.colorScheme.onPrimary,
                checkedTrackColor   = MaterialTheme.colorScheme.primary,
                uncheckedThumbColor = MaterialTheme.colorScheme.onSurfaceVariant,
                uncheckedTrackColor = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.1f)
            )
        )
    }
}

/**
 * Edit form section — stateless. Receives edit values and callbacks.
 * Uses [LabeledTextField] for each editable field.
 */
@Composable
private fun EditProfileSection(
    name: String,
    bio: String,
    onNameChange: (String) -> Unit,
    onBioChange: (String) -> Unit,
    onSave: () -> Unit
) {
    Column(
        verticalArrangement = Arrangement.spacedBy(12.dp),
        modifier            = Modifier.padding(top = 16.dp)
    ) {
        HorizontalDivider(
            color    = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.1f),
            modifier = Modifier.padding(bottom = 4.dp)
        )

        Text(
            text  = "Edit Profile",
            style = MaterialTheme.typography.titleMedium,
            color = MaterialTheme.colorScheme.primary
        )

        LabeledTextField(
            label         = "Name",
            value         = name,
            onValueChange = onNameChange
        )

        LabeledTextField(
            label         = "Bio",
            value         = bio,
            onValueChange = onBioChange
        )

        Button(
            onClick  = onSave,
            modifier = Modifier.fillMaxWidth(),
            shape    = RoundedCornerShape(12.dp),
            colors   = ButtonDefaults.buttonColors(
                containerColor = MaterialTheme.colorScheme.primary
            )
        ) {
            Icon(
                imageVector        = Icons.Outlined.Save,
                contentDescription = null,
                modifier           = Modifier.padding(end = 8.dp)
            )
            Text(
                text  = "Save Changes",
                style = MaterialTheme.typography.labelLarge,
                modifier = Modifier.padding(vertical = 4.dp)
            )
        }
    }
}

/**
 * Contact information section — stateless, receives the full [ProfileUiState].
 */
@Composable
private fun ContactInfoSection(uiState: ProfileUiState) {
    Column(
        verticalArrangement = Arrangement.spacedBy(4.dp),
        modifier            = Modifier.padding(top = 16.dp)
    ) {
        HorizontalDivider(
            color    = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.1f),
            modifier = Modifier.padding(bottom = 8.dp)
        )

        InfoItem(
            icon  = Icons.Outlined.Email,
            label = "Email",
            value = uiState.email
        )

        InfoItem(
            icon  = Icons.Outlined.Phone,
            label = "Phone",
            value = uiState.phone
        )

        InfoItem(
            icon  = Icons.Outlined.LocationOn,
            label = "Location",
            value = uiState.location
        )
    }
}
