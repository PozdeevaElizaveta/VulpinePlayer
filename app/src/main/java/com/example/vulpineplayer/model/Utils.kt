package com.example.vulpineplayer.model

import androidx.compose.ui.graphics.Color
import com.example.vulpineplayer.data.db.Enity.TrackData

fun String.toColor(): Color {
    return try {
        Color(android.graphics.Color.parseColor(this))
    } catch (e: Exception) {
        Color(0xFFF2A93B)
    }
}
fun List<TrackData>.isAllCached(): Boolean {
    return this.isNotEmpty() && this.all { it.isCached }
}
fun formatTime(millis: Long): String {
    if (millis <= 0) return "0:00"
    val minutes = (millis / 1000) / 60
    val seconds = (millis / 1000) % 60
    return String.format("%d:%02d", minutes, seconds)
}