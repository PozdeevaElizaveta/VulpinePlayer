package com.example.vulpineplayer.components

import androidx.compose.foundation.background
import androidx.compose.foundation.combinedClickable
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
import com.example.vulpineplayer.data.db.Enity.PlaylistData
import com.example.vulpineplayer.model.toColor

enum class CardPlaylistMode{
    NORMAL,
    SELECTION
}
@Composable
fun CardPlaylist(playlist: PlaylistData,
                 trackCount: Int,
                 mode: CardPlaylistMode = CardPlaylistMode.NORMAL,
                 isSelected: Boolean = false,
                 isCached: Boolean = false,
                 onClick: () -> Unit,
                 onLongClick: () -> Unit,
                 onMoreClick: () -> Unit = {} ){
    Row(modifier = Modifier.height(72.dp)
        .fillMaxWidth()
        .combinedClickable(
            onClick=onClick,
            onLongClick = onLongClick
        )
        .background(if (isSelected) MaterialTheme.colorScheme.surface else Color.Transparent),
        verticalAlignment = Alignment.CenterVertically) {
        Spacer(modifier = Modifier.width(16.dp))
        Box(modifier = Modifier
            .size(56.dp)
            .clip(RoundedCornerShape(8.dp))
            .background(playlist.colorHEX.toColor()),
            contentAlignment = Alignment.Center) {
            Icon(
                painter = painterResource(id = R.drawable.fox),
                contentDescription = "Fox",
                tint = MaterialTheme.colorScheme.surface
            )
            if (isCached){
                Icon(
                    imageVector = Icons.Default.Download,
                    contentDescription = "Cached",
                    tint = MaterialTheme.colorScheme.onBackground,
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

            Text(playlist.name,
                color = MaterialTheme.colorScheme.onBackground,
                modifier = Modifier.weight(1f),
                style = MaterialTheme.typography.titleLarge,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis)
            Text("$trackCount tracks",
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                modifier = Modifier.weight(1f),
                style = MaterialTheme.typography.labelMedium,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis)
        }
        when (mode) {
            CardPlaylistMode.NORMAL -> {
                IconButton(onClick = onMoreClick) {
                    Icon(
                        imageVector = Icons.Default.MoreVert,
                        contentDescription = "Menu",
                        tint = if (isSelected) MaterialTheme.colorScheme.secondary else MaterialTheme.colorScheme.onSurfaceVariant,
                    )
                }
            }

            CardPlaylistMode.SELECTION -> {
                Checkbox(
                    checked = isSelected,
                    onCheckedChange = { onClick() }, // Клик по чекбоксу = клик по карточке
                    colors = CheckboxDefaults.colors(
                        checkedColor = MaterialTheme.colorScheme.primary
                    )
                )
            }
        }
    }
}