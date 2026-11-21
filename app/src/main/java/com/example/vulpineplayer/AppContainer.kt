package com.example.vulpineplayer

import android.content.Context
import androidx.room.Room
import com.example.vulpineplayer.data.repository.PlaylistRepository
import com.example.vulpineplayer.data.repository.TrackRepository
import com.example.vulpineplayer.model.AppDatabase
import com.example.vulpineplayer.model.TrackDao

class AppContainer(context: Context) {
    private val database : AppDatabase by lazy {
        Room.databaseBuilder(
            context = context,
            AppDatabase::class.java,
            "vulpine.db"
        )
            .fallbackToDestructiveMigration(dropAllTables = true)
            .build()
    }

    val trackRepository = TrackRepository(database.trackDao(), context.applicationContext)

    val playlistRepository = PlaylistRepository(database.playlistDao(), context.applicationContext)
}