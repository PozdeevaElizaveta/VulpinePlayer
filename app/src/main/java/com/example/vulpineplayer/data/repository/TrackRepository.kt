package com.example.vulpineplayer.data.repository

import android.content.ContentResolver
import com.example.vulpineplayer.data.db.DAO.TrackDao
import com.example.vulpineplayer.data.db.Enity.TrackData
import com.example.vulpineplayer.model.scanMusic
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.withContext

class TrackRepository(private val trackDao: TrackDao,
    private val contentResolver: ContentResolver) {

    fun observeAllTracks(): Flow<List<TrackData>>{
    return trackDao.getAllTracks()
    }

    suspend fun scanAndSaveMusic() {
        withContext(Dispatchers.IO) {
            val deviceTracks = scanMusic(contentResolver)
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