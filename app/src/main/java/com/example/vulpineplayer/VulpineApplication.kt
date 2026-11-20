package com.example.vulpineplayer

import android.app.Application
import androidx.room.Room
import com.example.vulpineplayer.model.AppDatabase

class VulpineApplication: Application(){
/*    Ленивая инициализация: база создастся только при первом обращении к ней*/
    val database : AppDatabase by lazy {
        Room.databaseBuilder(
            this,
            AppDatabase::class.java,
            "vulpine.db"
        )
            .fallbackToDestructiveMigration()
            .build()
    }
}