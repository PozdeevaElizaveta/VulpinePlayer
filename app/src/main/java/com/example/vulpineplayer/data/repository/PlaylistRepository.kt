package com.example.vulpineplayer.data.repository

import com.example.vulpineplayer.data.db.DAO.PlaylistDao
import com.example.vulpineplayer.data.db.Enity.PlaylistData
import com.example.vulpineplayer.data.db.Enity.PlaylistTrackCrossRef
import com.example.vulpineplayer.data.db.Enity.PlaylistWithTracks
import kotlinx.coroutines.flow.Flow

class PlaylistRepository(private val playlistDao: PlaylistDao) {

    fun observeAllPlaylists(): Flow<List<PlaylistData>>{
        return playlistDao.getAllPlaylists()
    }
    fun observePlaylistWithTracks(playlistId: Long): Flow<PlaylistWithTracks>{
        return playlistDao.getPlaylistWithTracks(playlistId)
    }

    suspend fun createPlaylist(name: String, colorHex: String) {
            val newPlaylist = PlaylistData(name=name, colorHEX = colorHex)
            playlistDao.insertPlaylist(newPlaylist)
    }

    suspend fun deletePlaylists(ids: Set<Long>){
            playlistDao.deletePlaylistsByIds(ids.toList())

    }

    suspend fun addTrackToPlaylist(playlistId: Long, trackId: Long){
            playlistDao.addTrackToPlaylist(PlaylistTrackCrossRef(playlistId, trackId))

    }

    suspend fun removeTrackFromPlaylist(playlistId: Long, trackId: Long){
            playlistDao.removeTrackFromPlaylist(PlaylistTrackCrossRef(playlistId, trackId))
    }
}