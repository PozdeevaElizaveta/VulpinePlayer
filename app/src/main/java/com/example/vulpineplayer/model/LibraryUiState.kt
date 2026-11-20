package com.example.vulpineplayer.model

import android.os.Message

sealed interface LibraryUiState {
    object Loading : LibraryUiState
    data class Success(val tracks: List<TrackData>) : LibraryUiState
    object Empty : LibraryUiState
    data class Error (val message: String) : LibraryUiState
}