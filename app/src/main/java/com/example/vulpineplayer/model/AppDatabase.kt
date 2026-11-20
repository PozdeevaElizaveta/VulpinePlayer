package com.example.vulpineplayer.model

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(entities = [TrackData::class, PlaylistData::class, PlaylistTrackCrossRef::class],
    version = 3,
    exportSchema = false)
abstract class AppDatabase: RoomDatabase(){
    abstract fun trackDao(): TrackDao
    abstract fun playlistDao(): PlaylistDao
}















