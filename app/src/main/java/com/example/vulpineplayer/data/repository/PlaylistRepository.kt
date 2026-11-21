package com.example.vulpineplayer.data.repository

import android.content.Context
import com.example.vulpineplayer.model.PlaylistDao
import com.example.vulpineplayer.model.PlaylistData
import com.example.vulpineplayer.model.PlaylistTrackCrossRef
import com.example.vulpineplayer.model.PlaylistWithTracks
import com.example.vulpineplayer.model.TrackDao
import com.example.vulpineplayer.model.TrackData
import com.example.vulpineplayer.model.scanMusic
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.withContext

class PlaylistRepository(private val playlistDao: PlaylistDao,
                      private val context: Context) {

    fun observeAllPlaylists(): Flow<List<PlaylistData>>{
        return playlistDao.getAllPlaylists()
    }
    fun observePlaylistWithTracks(playlistId: Long): Flow<PlaylistWithTracks>{
        return playlistDao.getPlaylistWithTracks(playlistId)
    }

    suspend fun createPlaylist(name: String, colorHex: String) {
        withContext(Dispatchers.IO) {
            val newPlaylist = PlaylistData(name=name, colorHEX = colorHex)
            playlistDao.insertPlaylist(newPlaylist)
        }
    }

    suspend fun deletePlaylists(ids: Set<Long>){
        withContext(Dispatchers.IO){
            playlistDao.deletePlaylistsByIds(ids.toList())
        }
    }

    suspend fun addTrackToPlaylist(playlistId: Long, trackId: Long){
        withContext(Dispatchers.IO){
            playlistDao.addTrackToPlaylist(PlaylistTrackCrossRef(playlistId, trackId))
        }
    }

    suspend fun removeTrackFromPlaylist(playlistId: Long, trackId: Long){
        withContext(Dispatchers.IO){
            playlistDao.removeTrackFrompPlaylist(PlaylistTrackCrossRef(playlistId, trackId))
        }
    }
}