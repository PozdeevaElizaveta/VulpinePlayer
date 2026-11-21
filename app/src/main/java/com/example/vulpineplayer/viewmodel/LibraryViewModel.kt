package com.example.vulpineplayer.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.CreationExtras
import com.example.vulpineplayer.data.repository.TrackRepository
import com.example.vulpineplayer.model.TrackDao
import com.example.vulpineplayer.model.TrackData
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch



/*Обычный список — это статичная коробка. Если ты добавила трек в базу, обычный список об этом не узнает,
и экран не обновится.
StateFlow — это «живой» поток данных. Представь себе бегущую строку на бирже. Как только данные в базе меняются
(добавился трек), StateFlow сразу выплевывает новое значение, и Compose мгновенно перерисовывает экран.*/
class LibraryViewModel(private val repository: TrackRepository): ViewModel() {
    val tracks: StateFlow<List<TrackData>> = repository.observeAllTracks()
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
            repository.addTrack(track)
        }
    }
    fun scanANDSaveMusic(){
        viewModelScope.launch {
            repository.scanAndSaveMusic(context(StateFlow<List<TrackData>>))
        }
    }
}


/*«Фабрика» — это инструкция для Android, как правильно собрать ViewModel.*/
class LibraryViewModelFactory(private val repository: TrackRepository) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>, extras: CreationExtras): T {
        if (modelClass.isAssignableFrom(LibraryViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return LibraryViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}