package com.example.vulpineplayer.data.db.Enity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "tracks")
data class TrackData (
    @PrimaryKey
    val id: Long,
    val name: String,
    val artist: String,
    val duration: String,
    val durationMs: Long = 0L,
    val isCached: Boolean = false,
    val uri: String)


