package com.example.vulpineplayer.model

import androidx.room.Embedded
import androidx.room.Junction
import androidx.room.Relation

data class PlaylistWithTracks(
    @Embedded val playlist: PlaylistData, // Данные самого плейлиста
    //получаем список треков в плейлисте
    @Relation(
        parentColumn = "id",
        entityColumn = "id",
        associateBy = Junction( //это параметр внутри @Relation, который применяется для описания связи типа «Многие ко многим»
            value = PlaylistTrackCrossRef::class,
            parentColumn = "playlistId",
            entityColumn = "trackId"
        )
    )
    val tracks: List<TrackData>
)