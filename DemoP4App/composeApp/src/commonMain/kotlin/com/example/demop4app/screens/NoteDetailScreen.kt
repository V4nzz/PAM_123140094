package com.example.demop4app.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.StarBorder
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.demop4app.data.model.Note

/**
 * Layar detail catatan.
 * TopAppBar actions: ★ toggle favorit | ✎ edit | 🗑 hapus (dengan konfirmasi).
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NoteDetailScreen(
    note: Note?,
    onBackClick: () -> Unit,
    onEditClick: (Int) -> Unit,
    onToggleFavorite: (Int) -> Unit,
    onDeleteClick: (Int) -> Unit
) {
    var showDeleteDialog by remember { mutableStateOf(false) }

    // Dialog konfirmasi hapus
    if (showDeleteDialog && note != null) {
        AlertDialog(
            onDismissRequest = { showDeleteDialog = false },
            title = { Text("Hapus Catatan?") },
            text = { Text("\"${note.title}\" akan dihapus secara permanen.") },
            confirmButton = {
                TextButton(
                    onClick = {
                        showDeleteDialog = false
                        onDeleteClick(note.id)
                    }
                ) {
                    Text("Hapus", color = MaterialTheme.colorScheme.error)
                }
            },
            dismissButton = {
                TextButton(onClick = { showDeleteDialog = false }) {
                    Text("Batal")
                }
            }
        )
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(note?.title ?: "Catatan Tidak Ditemukan") },
                navigationIcon = {
                    IconButton(onClick = onBackClick) {
                        Icon(
                            imageVector = Icons.Default.ArrowBack,
                            contentDescription = "Kembali"
                        )
                    }
                },
                actions = {
                    if (note != null) {
                        // Toggle favorit
                        IconButton(onClick = { onToggleFavorite(note.id) }) {
                            Icon(
                                imageVector = if (note.isFavorite) Icons.Default.Star
                                              else Icons.Default.StarBorder,
                                contentDescription = if (note.isFavorite) "Hapus dari Favorit"
                                                     else "Tambah ke Favorit",
                                tint = if (note.isFavorite) MaterialTheme.colorScheme.primary
                                       else MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                        // Edit
                        IconButton(onClick = { onEditClick(note.id) }) {
                            Icon(
                                imageVector = Icons.Default.Edit,
                                contentDescription = "Edit Catatan"
                            )
                        }
                        // Hapus
                        IconButton(onClick = { showDeleteDialog = true }) {
                            Icon(
                                imageVector = Icons.Default.Delete,
                                contentDescription = "Hapus Catatan",
                                tint = MaterialTheme.colorScheme.error
                            )
                        }
                    }
                }
            )
        }
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(16.dp)
        ) {
            if (note == null) {
                Text(
                    text = "Catatan tidak ditemukan.",
                    modifier = Modifier.align(Alignment.Center),
                    style = MaterialTheme.typography.bodyLarge
                )
            } else {
                Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                    Text(
                        text = note.title,
                        style = MaterialTheme.typography.headlineMedium
                    )
                    HorizontalDivider()
                    Text(
                        text = note.content,
                        style = MaterialTheme.typography.bodyLarge
                    )
                }
            }
        }
    }
}
