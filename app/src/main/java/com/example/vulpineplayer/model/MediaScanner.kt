package com.example.vulpineplayer.model

import android.content.ContentResolver
import android.content.ContentUris
import android.provider.MediaStore
import com.example.vulpineplayer.data.db.Enity.TrackData


fun scanMusic(resolver : ContentResolver): List<TrackData>{
/*        ContentResolver — это специальный «ключ», который даёт приложению
        право читать данные из системных баз (включая MediaStore)*/
        val uri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI

        val projection = arrayOf(
            MediaStore.Audio.Media._ID,
            MediaStore.Audio.Media.TITLE,
            MediaStore.Audio.Media.ARTIST,
            MediaStore.Audio.Media.DURATION
        )

        val cursor = resolver.query(
            uri,
            projection,
            null,
            null,
            null
        ) ?: return emptyList()

        return cursor.use {
            val tracks = mutableListOf<TrackData>()

            val idCol = it.getColumnIndexOrThrow(MediaStore.Audio.Media._ID)
            val nameCol = it.getColumnIndexOrThrow(MediaStore.Audio.Media.TITLE)
            val artistCol = it.getColumnIndexOrThrow(MediaStore.Audio.Media.ARTIST)
            val durationCol = it.getColumnIndexOrThrow(MediaStore.Audio.Media.DURATION)

            while (it.moveToNext()) {
                val id = it.getLong(idCol)
                val name = it.getString(nameCol) ?: "Unknown track"
                val artist = it.getString(artistCol) ?: "Unknown artist"
                val durationMillis = it.getLong(durationCol)

                val contentUri = ContentUris.withAppendedId(uri, id).toString()

                tracks.add(
                    TrackData(
                        id = id,
                        name = name,
                        artist = artist,
                        duration = formatTime(durationMillis),
                        durationMs = durationMillis,
                        uri = contentUri
                    )
                )
            }

            tracks
        }
    }
