package com.example.demop4app.viewmodel

import androidx.lifecycle.ViewModel
import com.example.demop4app.data.model.Note
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class NoteViewModel : ViewModel() {

    private val _notes = MutableStateFlow<List<Note>>(emptyList())
    val notes: StateFlow<List<Note>> = _notes.asStateFlow()

    private var nextId = 1

    /** Tambah catatan baru */
    fun addNote(title: String, content: String) {
        _notes.update { current ->
            current + Note(id = nextId++, title = title.trim(), content = content.trim())
        }
    }

    /** Update judul & isi catatan yang sudah ada */
    fun updateNote(id: Int, title: String, content: String) {
        _notes.update { current ->
            current.map { note ->
                if (note.id == id) note.copy(title = title.trim(), content = content.trim())
                else note
            }
        }
    }

    /** Toggle status favorit */
    fun toggleFavorite(id: Int) {
        _notes.update { current ->
            current.map { note ->
                if (note.id == id) note.copy(isFavorite = !note.isFavorite)
                else note
            }
        }
    }

    /** Hapus catatan berdasarkan id */
    fun deleteNote(id: Int) {
        _notes.update { current -> current.filter { it.id != id } }
    }

    /** Ambil satu note berdasarkan id */
    fun getNoteById(id: Int): Note? = _notes.value.find { it.id == id }
}
