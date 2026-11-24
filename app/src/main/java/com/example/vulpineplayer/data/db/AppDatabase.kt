package com.example.vulpineplayer.data.db

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.vulpineplayer.data.db.DAO.PlaylistDao
import com.example.vulpineplayer.data.db.Enity.PlaylistData
import com.example.vulpineplayer.data.db.Enity.PlaylistTrackCrossRef
import com.example.vulpineplayer.data.db.DAO.TrackDao
import com.example.vulpineplayer.data.db.Enity.TrackData

@Database(entities = [TrackData::class, PlaylistData::class, PlaylistTrackCrossRef::class],
    version = 3,
    exportSchema = false)
abstract class AppDatabase: RoomDatabase(){
    abstract fun trackDao(): TrackDao
    abstract fun playlistDao(): PlaylistDao
}















