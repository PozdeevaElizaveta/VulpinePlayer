package com.example.vulpineplayer.model

import android.content.ContentUris
import android.content.Context
import android.provider.MediaStore
import androidx.core.util.TimeUtils.formatDuration
import java.util.concurrent.TimeUnit


    fun scanMusic(context: Context): List<TrackData>{
/*        ContentResolver — это специальный «ключ», который даёт приложению
        право читать данные из системных баз (включая MediaStore)*/
        val resolver = context.contentResolver
        val uri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI /*адрес «полки» с музыкой на телефоне*/
        val cursor = resolver.query(uri, null,null,null,null)
        if (cursor == null){
            return emptyList()
        }
        val tracks = mutableListOf<TrackData>()

        val idCol = cursor.getColumnIndexOrThrow((MediaStore.Audio.Media._ID))
        val nameCol = cursor.getColumnIndexOrThrow((MediaStore.Audio.Media.TITLE))
        val artistCol = cursor.getColumnIndexOrThrow((MediaStore.Audio.Media.ARTIST))
        val durationCol = cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.DURATION)

        while (cursor.moveToNext()) {
            val id = cursor.getLong(idCol)
            val name = cursor.getString(nameCol) ?: "Unknow track"
            val artist = cursor.getString(artistCol) ?: "Unknow artist"
            val durationMillis = cursor.getLong(durationCol)

            val contentUri = ContentUris.withAppendedId(
                MediaStore.Audio.Media.EXTERNAL_CONTENT_URI, id).toString()

            val track = TrackData(
                id=id,
                name=name,
                artist=artist,
                duration = formatTime(durationMillis),
                durationMs = durationMillis,
                uri=contentUri
            )
            tracks.add(track)

        }
        cursor.close()
        return tracks
    }
