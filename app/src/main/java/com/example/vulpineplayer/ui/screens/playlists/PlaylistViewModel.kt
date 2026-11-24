package com.example.vulpineplayer.ui.screens.playlists

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.CreationExtras
import com.example.vulpineplayer.data.repository.PlaylistRepository
import com.example.vulpineplayer.data.db.Enity.PlaylistData
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class PlaylistViewModel(private val repository: PlaylistRepository): ViewModel() {
    val playlists : StateFlow<List<PlaylistData>> = repository.observeAllPlaylists()
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )
    fun createPlaylist(name: String, colorHEX: String){
        viewModelScope.launch {
            val newPlaylist = PlaylistData(name=name, colorHEX = colorHEX)
            repository.createPlaylist(newPlaylist.name, newPlaylist.colorHEX)
        }
    }
    fun deletePlaylists(ids: Set<Long>) {
        viewModelScope.launch {
            repository.deletePlaylists(ids)
        }
    }
    fun getPlaylistWithTracks(playlistId: Long) = repository.observePlaylistWithTracks(playlistId)
}

//viewmodel ищет пустой конструктор, но унас он не пустой, фабрика передает параметр и инструкцию по использванию
class PlaylistViewModelFactory(private val repository: PlaylistRepository) : ViewModelProvider.Factory{
    override fun <T : ViewModel> create(modelClass: Class<T>, extras: CreationExtras): T {
        if (modelClass.isAssignableFrom(PlaylistViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return PlaylistViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}