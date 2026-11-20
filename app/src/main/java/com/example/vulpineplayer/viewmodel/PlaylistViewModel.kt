package com.example.vulpineplayer.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.CreationExtras
import com.example.vulpineplayer.model.PlaylistDao
import com.example.vulpineplayer.model.PlaylistData
import com.example.vulpineplayer.model.TrackDao
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.WhileSubscribed
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class PlaylistViewModel(private val dao: PlaylistDao): ViewModel() {
    val playlists : StateFlow<List<PlaylistData>> = dao.getAllPlaylists()
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )
    fun createPlaylist(name: String, colorHEX: String){
        viewModelScope.launch {
            val newPlaylist = PlaylistData(name=name, colorHEX = colorHEX)
            dao.insertPlaylist(newPlaylist)
        }
    }
    fun deletePlaylists(ids: Set<Long>) {
        viewModelScope.launch {
            dao.deletePlaylistsByIds(ids.toList())
        }
    }
    fun getPlaylistWithTracks(playlistId: Long) = dao.getPlaylistWithTracks(playlistId)
}

//viewmodel ищет пустой конструктор, но унас он не пустой, фабрика передает параметр и инструкцию по использванию
class PlaylistViewModelFactory(private val dao: PlaylistDao) : ViewModelProvider.Factory{
    override fun <T : ViewModel> create(modelClass: Class<T>, extras: CreationExtras): T {
        if (modelClass.isAssignableFrom(PlaylistViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return PlaylistViewModel(dao) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}