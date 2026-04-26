package com.example.myprofileapp

/**
 * Data model representing a user profile.
 */
data class Profile(
    val name: String,
    val bio: String,
    val email: String,
    val phone: String,
    val location: String,
    val imageUrl: String
) {
    companion object {
        val sampleProfile = Profile(
            name = "Ivan Nandira Mangunang",
            bio = "Mobile & Multiplatform Developer · Kotlin Enthusiast · UI/UX Lover",
            email = "ivannandira@gmail.com",
            phone = "+62 89510379507",
            location = "Bandar Lampung, Indonesia",
            imageUrl = ""
        )
    }
}
