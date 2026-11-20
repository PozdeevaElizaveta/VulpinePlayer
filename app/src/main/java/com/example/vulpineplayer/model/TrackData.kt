package com.example.vulpineplayer.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlin.time.Duration

@Entity(tableName = "tracks")
data class TrackData (
    @PrimaryKey
    val id: Long,
    val name: String,
    val artist: String,
    val duration: String,
    val durationMs: Long = 0L,
    val isCached: Boolean = false,
    val uri: String) {
}


