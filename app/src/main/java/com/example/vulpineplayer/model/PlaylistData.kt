package com.example.vulpineplayer.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "playlists")
data class PlaylistData (
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0, // Room сам выдаст уникальный номер при создании
    val name: String,
    val colorHEX: String,
    val createdAt: Long = System.currentTimeMillis()
) {
}