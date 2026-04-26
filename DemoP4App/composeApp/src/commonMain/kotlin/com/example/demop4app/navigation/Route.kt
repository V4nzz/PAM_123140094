package com.example.demop4app.navigation

/**
 * Mendefinisikan semua route navigasi secara type-safe menggunakan sealed class.
 */
sealed class Route(val route: String) {

    /** Tab: Daftar catatan */
    data object NoteList : Route("note_list")

    /** Layar detail catatan — menerima argumen noteId */
    data class NoteDetail(val noteId: Int) : Route("note_detail/$noteId") {
        companion object {
            /** Pola route dengan placeholder argumen untuk NavHost */
            const val ROUTE_PATTERN = "note_detail/{noteId}"
            const val ARG_NOTE_ID = "noteId"
        }
    }

    /** Tab: Catatan favorit */
    data object Favorites : Route("favorites")

    /** Tab: Profil pengguna */
    data object Profile : Route("profile")

    /** Layar tambah catatan baru */
    data object AddNote : Route("add_note")

    /** Layar edit catatan — menerima argumen noteId */
    data class EditNote(val noteId: Int) : Route("edit_note/$noteId") {
        companion object {
            const val ROUTE_PATTERN = "edit_note/{noteId}"
            const val ARG_NOTE_ID = "noteId"
        }
    }
}
