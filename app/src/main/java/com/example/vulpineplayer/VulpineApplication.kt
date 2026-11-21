package com.example.vulpineplayer

import android.app.Application
import androidx.room.Room
import com.example.vulpineplayer.model.AppDatabase

class VulpineApplication: Application(){
/*    Ленивая инициализация: база создастся только при первом обращении к ней*/
    val container : AppContainer by lazy {
    AppContainer(this)
    }
}