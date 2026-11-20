package com.example.vulpineplayer.model

import androidx.room.Entity

//связь многие ко многим плейлистов и треков
@Entity(
    tableName = "playlist_track_cross_ref",
    primaryKeys = ["playlistId", "trackId"]
)
data class PlaylistTrackCrossRef(
    val playlistId: Long,
    val trackId: Long
)