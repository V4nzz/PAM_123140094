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
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Email
import androidx.compose.material.icons.outlined.LocationOn
import androidx.compose.material.icons.outlined.Phone
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

/**
 * Main profile screen that assembles [ProfileHeader], [InfoItem] rows inside
 * a [ProfileCard], with a toggle [Button] to show / hide contact details
 * using [AnimatedVisibility].
 *
 * @param profile  The [Profile] data to display.
 * @param modifier Optional [Modifier] for the root layout.
 */
@Composable
fun ProfileScreen(
    profile: Profile,
    modifier: Modifier = Modifier
) {
    // ── State: toggle contact‑info visibility ──────────────────────────────
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
            // ── Profile Card ───────────────────────────────────────────────
            ProfileCard {
                // Header: avatar + name + bio
                ProfileHeader(
                    name = profile.name,
                    bio = profile.bio,
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(modifier = Modifier.height(24.dp))

                // Toggle button
                Button(
                    onClick = { showDetails = !showDetails },
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(12.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = MaterialTheme.colorScheme.primary
                    )
                ) {
                    Text(
                        text = if (showDetails) "Hide Contact Info" else "Show Contact Info",
                        style = MaterialTheme.typography.labelLarge,
                        modifier = Modifier.padding(vertical = 4.dp)
                    )
                }

                // ── Animated contact details ───────────────────────────────
                AnimatedVisibility(
                    visible = showDetails,
                    enter = expandVertically(animationSpec = tween(400)) + fadeIn(animationSpec = tween(400)),
                    exit = shrinkVertically(animationSpec = tween(300)) + fadeOut(animationSpec = tween(300))
                ) {
                    Column(
                        verticalArrangement = Arrangement.spacedBy(4.dp),
                        modifier = Modifier.padding(top = 16.dp)
                    ) {
                        HorizontalDivider(
                            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.1f),
                            modifier = Modifier.padding(bottom = 8.dp)
                        )

                        InfoItem(
                            icon = Icons.Outlined.Email,
                            label = "Email",
                            value = profile.email
                        )

                        InfoItem(
                            icon = Icons.Outlined.Phone,
                            label = "Phone",
                            value = profile.phone
                        )

                        InfoItem(
                            icon = Icons.Outlined.LocationOn,
                            label = "Location",
                            value = profile.location
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            // Footer text
            Text(
                text = "Built with Compose Multiplatform\uFE0F",
                style = MaterialTheme.typography.labelMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}
