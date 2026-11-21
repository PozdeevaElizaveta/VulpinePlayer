package com.example.vulpineplayer.data.repository

import android.content.Context
import androidx.lifecycle.viewModelScope
import com.example.vulpineplayer.model.TrackDao
import com.example.vulpineplayer.model.TrackData
import com.example.vulpineplayer.model.scanMusic
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class TrackRepository(private val trackDao: TrackDao,
    private val context: Context) {

    fun observeAllTracks(): Flow<List<TrackData>>{
    return trackDao.getAllTracks()
    }

    suspend fun scanAndSaveMusic(context: Context) {
        withContext(Dispatchers.IO) {
            val deviceTracks = scanMusic(context)
            if (deviceTracks.isNotEmpty()) {
                trackDao.insertAllTracks((deviceTracks))
            }
        }
    }

    suspend fun addTrack(trackData: TrackData){
        withContext(Dispatchers.IO){
            trackDao.insertAllTracks(listOf(trackData))
        }
    }
}