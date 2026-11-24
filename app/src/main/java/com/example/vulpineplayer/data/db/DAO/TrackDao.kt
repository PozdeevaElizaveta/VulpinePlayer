package com.example.vulpineplayer.data.db.DAO

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.vulpineplayer.data.db.Enity.TrackData
import kotlinx.coroutines.flow.Flow

@Dao /*(Data Access Object) — это интерфейс, то есть список правил, с которыми ты можешь обращаться к Room.*/
interface TrackDao {
    @Query ("SELECT * FROM tracks") /*запрос к SQLite*/
    fun getAllTracks(): Flow<List<TrackData>>

    @Insert (onConflict = OnConflictStrategy.REPLACE)
/*    Это команда (аннотация) для Room, которая говорит: «Эй, эта функция должна записывать новые данные в базу».*/
    suspend fun insertAllTracks(tracks: List<TrackData>)
}