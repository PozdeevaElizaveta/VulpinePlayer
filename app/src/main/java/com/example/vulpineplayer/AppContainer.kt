package com.example.vulpineplayer

import android.content.Context
import androidx.room.Room
import com.example.vulpineplayer.data.repository.PlaylistRepository
import com.example.vulpineplayer.data.repository.TrackRepository
import com.example.vulpineplayer.data.db.AppDatabase
import com.example.vulpineplayer.ui.screens.library.LibraryViewModelFactory
import com.example.vulpineplayer.ui.screens.playlists.PlaylistViewModelFactory

class AppContainer(context: Context) {
    private val appContext = context.applicationContext
    private val database : AppDatabase by lazy {
        Room.databaseBuilder(
            context = appContext,
            AppDatabase::class.java,
            "vulpine.db"
        )
            .fallbackToDestructiveMigration(dropAllTables = true) //УБРАТЬ, СНОСИТ БД
            .build()
    }

    val trackRepository = TrackRepository(database.trackDao(), appContext.contentResolver)

    val playlistRepository = PlaylistRepository(database.playlistDao())

    val libraryViewModelFactory by lazy {
        LibraryViewModelFactory(trackRepository)
    }
    val playlistViewModelFactory by lazy {
        PlaylistViewModelFactory(playlistRepository)
    }
}