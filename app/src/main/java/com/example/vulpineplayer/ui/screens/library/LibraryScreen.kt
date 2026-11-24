package com.example.vulpineplayer.ui.screens.library

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.example.vulpineplayer.data.db.Enity.TrackData
import com.example.vulpineplayer.components.CardTrack
import com.example.vulpineplayer.model.LibraryUiState



    @Composable
    fun LibraryScreen(uiState: LibraryUiState,
                      currentTrack: TrackData?,
                onTrackClick: (TrackData) -> Unit) {
        when (uiState) {
            is LibraryUiState.Loading -> {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    Text("Loading...", color = MaterialTheme.colorScheme.onBackground)
                }
            }
            is LibraryUiState.Success -> {
                LazyColumn(modifier = Modifier.fillMaxSize()) {
                    items(uiState.tracks, key = { it.id }) { itemTrack ->
                        CardTrack(track = itemTrack,
                            isPlaying = (currentTrack?.id == itemTrack.id),
                            onClick = {onTrackClick(itemTrack)},
                            onMoreClick = {});
                    }
                }
            }
            is LibraryUiState.Empty -> {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    Text("Library is empty", color = MaterialTheme.colorScheme.onBackground)
                }
            }
            is LibraryUiState.Error -> {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    Text("Error", color = MaterialTheme.colorScheme.onBackground)
                }
            }
        }
    }

