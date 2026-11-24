package com.example.vulpineplayer.data.db.DAO

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import com.example.vulpineplayer.data.db.Enity.PlaylistData
import com.example.vulpineplayer.data.db.Enity.PlaylistTrackCrossRef
import com.example.vulpineplayer.data.db.Enity.PlaylistWithTracks
import kotlinx.coroutines.flow.Flow

@Dao
interface PlaylistDao{
    //create playlist
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertPlaylist(playlistData: PlaylistData): Long

    @Query("SELECT * FROM playlists ORDER BY createdAt DESC")
    fun getAllPlaylists(): Flow<List<PlaylistData>>

    @Transaction
    @Query("SELECT * FROM playlists WHERE id = :playlistId")
    fun getPlaylistWithTracks(playlistId: Long): Flow<PlaylistWithTracks>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun addTrackToPlaylist(crossRef: PlaylistTrackCrossRef)

    @Delete
    suspend fun removeTrackFromPlaylist(crossRef: PlaylistTrackCrossRef)

    @Query("DELETE FROM playlists WHERE id = :playlistId")
    suspend fun deletePlaylist(playlistId: Long)

    @Query("DELETE FROM playlists WHERE id IN (:ids)")
    suspend fun deletePlaylistsByIds(ids: List<Long>)
}