package com.example.vulpineplayer.ui.screens.playlistDetail

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Download
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.filled.Pause
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.example.vulpineplayer.R
import com.example.vulpineplayer.components.PlaylistMenu
import com.example.vulpineplayer.data.db.Enity.PlaylistWithTracks
import com.example.vulpineplayer.data.db.Enity.TrackData
import com.example.vulpineplayer.model.isAllCached
import com.example.vulpineplayer.model.toColor
import com.example.vulpineplayer.player.PlayerViewModel
import com.example.vulpineplayer.ui.screens.playlists.PlaylistViewModel

@Composable
fun PlaylistDetailScreen(playlistWithTracks: PlaylistWithTracks?,
                         playlistViewModel: PlaylistViewModel,
                         playerViewModel: PlayerViewModel,
                         isPlaying: Boolean,
                         currentTrack: TrackData?,
                         onBackClick: ()-> Unit){
    if (playlistWithTracks == null){
        return
    }
    val playlist = playlistWithTracks.playlist
    val tracks = playlistWithTracks.tracks
    var isMenuOpen by remember { mutableStateOf(false) }
    var isEditMode by remember { mutableStateOf(false) }
    val context = LocalContext.current
    Column(modifier = Modifier.fillMaxSize()) {
        Row(modifier = Modifier.fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 10.dp),
            verticalAlignment = Alignment.CenterVertically) {
            IconButton(onClick = onBackClick) {
                Icon(Icons.Default.ArrowBack, contentDescription = "Back")
            }
            Text(
                text = playlist.name,
                style = MaterialTheme.typography.displayMedium,
                color = MaterialTheme.colorScheme.onBackground,
                modifier = Modifier.weight(1f),
                textAlign = TextAlign.Center
            )
            Box {
                IconButton(onClick = { isMenuOpen = true }) {
                    Icon(
                        imageVector = Icons.Default.MoreVert,
                        contentDescription = "Menu",
                        tint = MaterialTheme.colorScheme.onBackground
                    )
                }

                // Используем наше новое меню из AppMenus.kt
                PlaylistMenu(
                    expanded = isMenuOpen,
                    onDismiss = { isMenuOpen = false },
                    onEditPLaylist = {  },
                    onDownloadPlaylist = {  },
                    onDeletePlaylist = {
                        playlistViewModel.deletePlaylists(setOf(playlist.id))
                        onBackClick()
                    }
                )
            }
        }
        Spacer(Modifier.height(16.dp))
        Box(modifier = Modifier.fillMaxWidth(0.9f)
            .aspectRatio(1f)
            .align(Alignment.CenterHorizontally)
            .clip(RoundedCornerShape(16.dp))
            .background(playlist.colorHEX.toColor())){
            Icon(
                painter = painterResource(id = R.drawable.fox),
                contentDescription = "Fox",
                tint = MaterialTheme.colorScheme.surface
            )
            if (tracks.isAllCached()){
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
        Text(
            text = "${tracks.size} songs • ${DurationPlaylist(tracks)}",
            style = MaterialTheme.typography.titleSmall,
            color = MaterialTheme.colorScheme.onBackground,
            modifier = Modifier.padding(16.dp)
                .align(Alignment.CenterHorizontally)
        )
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 24.dp),
            horizontalArrangement = Arrangement.SpaceEvenly,
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(onClick = {isEditMode= !isEditMode}) {
               Icon(imageVector = if (isEditMode) Icons.Default.Check else Icons.Default.Edit,
                   contentDescription = "Edit",
                   tint = if (isEditMode) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurfaceVariant)
            }
            val isThisPlaylistPlaying = isPlaying && currentTrack != null && tracks.any { it.id == currentTrack.id }
            Box(
                modifier = Modifier
                    .size(72.dp)
                    .clip(CircleShape)
                    .clickable {if (isThisPlaylistPlaying) {
                        playerViewModel.PlayPause()
                    } else {
                        playerViewModel.playTrack(context, tracks, 0)
                    }},
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = if (isThisPlaylistPlaying) Icons.Default.Pause else Icons.Default.PlayArrow,
                    contentDescription = if (isThisPlaylistPlaying) "Pause" else "Play",
                    tint = MaterialTheme.colorScheme.primary,
                    modifier = Modifier.size(64.dp)
                )
            }
            IconButton(onClick = { /* TODO: Открыть выбор треков */ }) {
                Icon(
                    imageVector = Icons.Default.Add,
                    contentDescription = "Add Songs",
                    tint = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }
    }
}

fun DurationPlaylist (tracks: List<TrackData>): String {
    val totalMs = tracks.sumOf { it.durationMs }

    if (totalMs == 0L) return "0 min"

    // Переводим миллисекунды в минуты
    val totalMinutes = totalMs / 1000 / 60

    return "$totalMinutes min"
}