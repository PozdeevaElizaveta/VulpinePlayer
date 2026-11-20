package com.example.vulpineplayer.viewmodel

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.CreationExtras
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.media3.common.MediaItem
import androidx.media3.exoplayer.ExoPlayer
import com.example.vulpineplayer.model.TrackDao
import com.example.vulpineplayer.model.TrackData
import com.example.vulpineplayer.model.scanMusic
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch



/*Обычный список — это статичная коробка. Если ты добавила трек в базу, обычный список об этом не узнает,
и экран не обновится.
StateFlow — это «живой» поток данных. Представь себе бегущую строку на бирже. Как только данные в базе меняются
(добавился трек), StateFlow сразу выплевывает новое значение, и Compose мгновенно перерисовывает экран.*/
class LibraryViewModel(private val dao: TrackDao): ViewModel() {
    fun scanANDsaveMusic(context: Context){
        viewModelScope.launch {
            val deviceTracks = scanMusic(context)
            if(deviceTracks.isNotEmpty()){
                dao.insertAllTracks((deviceTracks))
            }
        }
    }
    val tracks: StateFlow<List<TrackData>> = dao.getAllTracks()
        /*Функция, которая берет «холодный» поток из базы и превращает его в «живой»*/
        .stateIn(
            scope = viewModelScope,
            /*Если экран закроется и ViewModel умрет — поток данных тоже корректно закроется,
            чтобы не жрать батарейку.*/
            started = SharingStarted.WhileSubscribed(5000),
            /*Она говорит: "Слушай базу только пока экран включен". А цифра 5000 (5 секунд) — это защита от поворота экрана.
            Когда ты поворачиваешь телефон, экран на долю секунды исчезает и появляется снова.*/
            initialValue = emptyList()
            )
            fun addTrack(track: TrackData) {
                viewModelScope.launch {
                    dao.insertAllTracks(listOf(track))
                }
            }
}


/*«Фабрика» — это инструкция для Android, как правильно собрать ViewModel.*/
class LibraryViewModelFactory(private val dao: TrackDao) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>, extras: CreationExtras): T {
        if (modelClass.isAssignableFrom(LibraryViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return LibraryViewModel(dao) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}