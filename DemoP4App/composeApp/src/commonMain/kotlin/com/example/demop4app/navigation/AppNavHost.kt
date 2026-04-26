package com.example.demop4app.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.example.demop4app.screens.AddNoteScreen
import com.example.demop4app.screens.EditNoteScreen
import com.example.demop4app.screens.FavoritesScreen
import com.example.demop4app.screens.NoteDetailScreen
import com.example.demop4app.screens.NoteListScreen
import com.example.demop4app.screens.ProfileScreen
import com.example.demop4app.viewmodel.NoteViewModel

/**
 * NavHost utama aplikasi.
 * [viewModel] dibuat di App.kt dan digunakan bersama oleh semua screen.
 */
@Composable
fun AppNavHost(
    navController: NavHostController,
    viewModel: NoteViewModel,
    modifier: Modifier = Modifier
) {
    val notes by viewModel.notes.collectAsStateWithLifecycle()

    NavHost(
        navController = navController,
        startDestination = Route.NoteList.route,
        modifier = modifier
    ) {
        // ── Tab: Daftar Catatan ──────────────────────────────────────────────
        composable(route = Route.NoteList.route) {
            NoteListScreen(
                notes = notes,
                onNoteClick = { noteId ->
                    navController.navigate(Route.NoteDetail(noteId).route)
                },
                onAddClick = {
                    navController.navigate(Route.AddNote.route)
                }
            )
        }

        // ── Layar Detail Catatan ─────────────────────────────────────────────
        composable(
            route = Route.NoteDetail.ROUTE_PATTERN,
            arguments = listOf(
                navArgument(Route.NoteDetail.ARG_NOTE_ID) { type = NavType.IntType }
            )
        ) { backStackEntry ->
            val noteId = backStackEntry.arguments?.getInt(Route.NoteDetail.ARG_NOTE_ID) ?: -1
            val note = notes.find { it.id == noteId }
            NoteDetailScreen(
                note = note,
                onBackClick = { navController.popBackStack() },
                onEditClick = { id ->
                    navController.navigate(Route.EditNote(id).route)
                },
                onToggleFavorite = { id ->
                    viewModel.toggleFavorite(id)
                },
                onDeleteClick = { id ->
                    viewModel.deleteNote(id)
                    navController.popBackStack()
                }
            )
        }

        // ── Layar Tambah Catatan Baru ────────────────────────────────────────
        composable(route = Route.AddNote.route) {
            AddNoteScreen(
                onSave = { title, content ->
                    viewModel.addNote(title, content)
                },
                onBackClick = { navController.popBackStack() }
            )
        }

        // ── Layar Edit Catatan ───────────────────────────────────────────────
        composable(
            route = Route.EditNote.ROUTE_PATTERN,
            arguments = listOf(
                navArgument(Route.EditNote.ARG_NOTE_ID) { type = NavType.IntType }
            )
        ) { backStackEntry ->
            val noteId = backStackEntry.arguments?.getInt(Route.EditNote.ARG_NOTE_ID) ?: -1
            val note = notes.find { it.id == noteId }
            EditNoteScreen(
                note = note,
                onSave = { id, title, content ->
                    viewModel.updateNote(id, title, content)
                },
                onBackClick = { navController.popBackStack() }
            )
        }

        // ── Tab: Favorit ─────────────────────────────────────────────────────
        composable(route = Route.Favorites.route) {
            FavoritesScreen(favorites = notes.filter { it.isFavorite })
        }

        // ── Tab: Profil ──────────────────────────────────────────────────────
        composable(route = Route.Profile.route) {
            ProfileScreen()
        }
    }
}
