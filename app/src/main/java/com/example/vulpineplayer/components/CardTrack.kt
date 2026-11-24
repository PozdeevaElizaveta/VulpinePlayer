package com.example.vulpineplayer.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Download
import androidx.compose.material.icons.filled.DragHandle
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CheckboxDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.example.vulpineplayer.R
import com.example.vulpineplayer.data.db.Enity.TrackData

enum class CardTrackMode{
    NORMAL,
    EDIT,
    SELECTION
}

@Composable
fun CardTrack(track: TrackData,
              isPlaying: Boolean = false,
              isCached: Boolean = false,
              isSelected: Boolean = false,
              mode: CardTrackMode = CardTrackMode.NORMAL,
              onClick: () -> Unit,
              onMoreClick: () -> Unit = {}
){
    Row(modifier = Modifier.height(72.dp)
        .fillMaxWidth()
        .clickable{onClick()}
        .background(if (isSelected) MaterialTheme.colorScheme.surface else Color.Transparent),
        verticalAlignment = Alignment.CenterVertically) {
        Box(modifier = Modifier.width(4.dp)
            .height(72.dp)
            .background(if (isPlaying) MaterialTheme.colorScheme.primary else Color.Transparent))
        Spacer(modifier = Modifier.width(12.dp))
        Box(modifier = Modifier
            .size(56.dp)
            .clip(RoundedCornerShape(8.dp))
            .background(if (isSelected) MaterialTheme.colorScheme.secondary else MaterialTheme.colorScheme.surface),
            contentAlignment = Alignment.Center) {
            Icon(
                painter = painterResource(id = R.drawable.fox),
                contentDescription = "Fox",
                tint = if (isSelected) MaterialTheme.colorScheme.onSurfaceVariant else MaterialTheme.colorScheme.secondary
            )
            if (isCached){
                Icon(
                    imageVector = Icons.Default.Download,
                    contentDescription = "Cached",
                    tint = if (isSelected) MaterialTheme.colorScheme.onBackground else MaterialTheme.colorScheme.onSurfaceVariant,
                    modifier = Modifier
                        .align(Alignment.BottomEnd)
                        .padding(4.dp)
                        .size(16.dp)
                )
            }
        }
        Spacer(modifier = Modifier.width(20.dp))
        Column(modifier = Modifier
            .padding(vertical = 12.dp)
            .weight(1f),
            verticalArrangement = Arrangement.SpaceEvenly) {

            Text(track.name,
                color = if (isPlaying) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onBackground,
                modifier = Modifier.weight(1f),
                style = MaterialTheme.typography.titleLarge,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis)
            Text(track.artist,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                modifier = Modifier.weight(1f),
                style = MaterialTheme.typography.labelMedium,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis)
        }
        // Правая часть (зависит от режима)
        when (mode) {
            CardTrackMode.NORMAL -> {
                // Обычный режим — три точки
                IconButton(onClick = onMoreClick) {
                    Icon(
                        imageVector = Icons.Default.MoreVert,
                        contentDescription = "More",
                        tint = if (isSelected) MaterialTheme.colorScheme.secondary else MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }
            CardTrackMode.EDIT -> {
                // Режим редактирования — ручка для перетаскивания
                Icon(
                    imageVector = Icons.Default.DragHandle,
                    contentDescription = "Drag",
                    tint = if (isSelected) MaterialTheme.colorScheme.secondary else MaterialTheme.colorScheme.onSurfaceVariant,
                    modifier = Modifier.padding(16.dp) // Отступы, чтобы было удобно хватать
                )
            }
            CardTrackMode.SELECTION -> {
                // Режим выбора — чекбокс
                Checkbox(
                    checked = isSelected,
                    onCheckedChange = { onClick() }, // Клик по чекбоксу тоже выделяет
                    colors = CheckboxDefaults.colors(
                        checkedColor = MaterialTheme.colorScheme.primary,
                        uncheckedColor = MaterialTheme.colorScheme.onSurfaceVariant
                    ),
                    modifier = Modifier.padding(16.dp)
                )
            }
        }
    }
}
fun pluralizeTracks(count: Int): String {
    return when {
        count % 10 == 1 && count % 100 != 11 -> "track"
        count % 10 in 2..4 && count % 100 !in 12..14 -> "tracks"
        else -> "tracks"
    }
}

