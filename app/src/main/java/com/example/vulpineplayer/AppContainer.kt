package com.example.vulpineplayer

import android.content.Context
import androidx.room.Room
import com.example.vulpineplayer.data.repository.PlaylistRepository
import com.example.vulpineplayer.data.repository.TrackRepository
import com.example.vulpineplayer.model.AppDatabase
import com.example.vulpineplayer.model.TrackDao
import com.example.vulpineplayer.viewmodel.LibraryViewModel
import com.example.vulpineplayer.viewmodel.LibraryViewModelFactory
import com.example.vulpineplayer.viewmodel.PlaylistViewModel
import com.example.vulpineplayer.viewmodel.PlaylistViewModelFactory

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