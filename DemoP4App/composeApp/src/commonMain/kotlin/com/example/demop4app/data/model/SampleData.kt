package com.example.demop4app.data.model

/**
 * Data contoh (hardcoded) untuk demonstrasi navigasi.
 */
val sampleNotes = listOf(
    Note(
        id = 1,
        title = "Belajar Compose Navigation",
        content = "Navigation Compose memungkinkan kita berpindah antar layar secara deklaratif " +
                "menggunakan NavHost, NavController, dan composable destinations. " +
                "Argumen dapat dikirim melalui route pattern seperti \"detail/{id}\".",
        isFavorite = true
    ),
    Note(
        id = 2,
        title = "Sealed Class untuk Route",
        content = "Mendefinisikan route dengan sealed class memberikan keamanan tipe (type-safety) " +
                "dan menghindari kesalahan penulisan string route secara manual. " +
                "Setiap destination menjadi object atau data class yang eksplisit.",
        isFavorite = false
    ),
    Note(
        id = 3,
        title = "Bottom Navigation dengan Scaffold",
        content = "Material3 Scaffold menyediakan slot bottomBar yang ideal untuk NavigationBar. " +
                "Gunakan currentBackStackEntryAsState() untuk mendeteksi route aktif dan " +
                "menyoroti item yang sesuai di bottom navigation.",
        isFavorite = true
    ),
    Note(
        id = 4,
        title = "State Hoist & ViewModel",
        content = "State hoisting adalah pola mengangkat state ke owner yang lebih tinggi " +
                "agar composable bisa menjadi stateless dan mudah diuji. " +
                "ViewModel cocok sebagai state owner untuk logika yang melampaui satu screen.",
        isFavorite = false
    ),
    Note(
        id = 5,
        title = "Compose Multiplatform",
        content = "Compose Multiplatform (CMP) memungkinkan satu codebase UI berjalan di " +
                "Android, iOS, Desktop, dan Web. Sebagian besar library Jetpack Compose " +
                "tersedia di commonMain melalui versi multiplatform resmi dari JetBrains.",
        isFavorite = false
    )
)
