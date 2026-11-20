package com.example.vulpineplayer.components

import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CreateNewFolder
import androidx.compose.material.icons.filled.DeleteForever
import androidx.compose.material.icons.filled.Download
import androidx.compose.material.icons.filled.EditNote
import androidx.compose.material.icons.filled.Palette
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.SubdirectoryArrowRight
import androidx.compose.material.icons.filled.TurnRight
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.unit.dp


@Composable
fun PlaylistSelectionMenu(
    expanded: Boolean,
    onDismiss: () -> Unit,
    onChangeColor: () -> Unit,
    onDownloadPlaylists: () -> Unit,
    onDeletePlaylists: () -> Unit
) {
    DropdownMenu(
        expanded = expanded,
        onDismissRequest = onDismiss,
        containerColor = MaterialTheme.colorScheme.surface,
        shape = RoundedCornerShape(16.dp)
    ) {
        DropdownMenuItem(
            text = { Text("Change Color") },
            leadingIcon = { Icon(Icons.Default.Palette, null) },
            onClick = { onChangeColor(); onDismiss() }
        )
        DropdownMenuItem(
            text = { Text("Download") },
            leadingIcon = { Icon(Icons.Default.Download, null) },
            onClick = { onDownloadPlaylists(); onDismiss() }
        )
        DropdownMenuItem(
            text = { Text("Delete Playlists", color = MaterialTheme.colorScheme.error) },
            leadingIcon = {
                Icon(
                    Icons.Default.DeleteForever,
                    null,
                    tint = MaterialTheme.colorScheme.error
                )
            },
            onClick = {
                onDeletePlaylists(); onDismiss()
            }
        )
    }
}
@Composable
fun AppDropdownMenu(
    expanded: Boolean,
    onDismiss: () -> Unit,
    onDownloadTracks: () -> Unit = {},
    onPlayNext: () -> Unit = {},
    onAddToQueue: () -> Unit = {},
    onEditTrack: () -> Unit = {},
    onAddToPlaylist: () -> Unit = {},
    onGoToArtist: () -> Unit = {},
    onDeleteTracks: () -> Unit
){
    DropdownMenu(
        expanded = expanded,
        onDismissRequest = onDismiss,
        containerColor = MaterialTheme.colorScheme.surface,
        shape = RoundedCornerShape(16.dp)
    ) {
        DropdownMenuItem(
            text = { Text("Play Next") },
            leadingIcon = { Icon(Icons.Default.TurnRight, null) },
            onClick = { onPlayNext(); onDismiss() }
        )
        DropdownMenuItem(
            text = { Text("Add to Queue") },
            leadingIcon = { Icon(Icons.Default.SubdirectoryArrowRight, null) },
            onClick = { onAddToQueue(); onDismiss() }
        )
        DropdownMenuItem(
            text = { Text("Edit Track") },
            leadingIcon = { Icon(Icons.Default.EditNote, null) },
            onClick = { onEditTrack(); onDismiss() }
        )
        DropdownMenuItem(
            text = { Text("Download") },
            leadingIcon = { Icon(Icons.Default.Download, null) },
            onClick = { onDownloadTracks(); onDismiss() }
        )
        DropdownMenuItem(
            text = { Text("Add to Playlist") },
            leadingIcon = { Icon(Icons.Default.CreateNewFolder, null) },
            onClick = { onAddToPlaylist(); onDismiss() }
        )
        DropdownMenuItem(
            text = { Text("Go to Artist") },
            leadingIcon = { Icon(Icons.Default.Person, null) },
            onClick = { onGoToArtist(); onDismiss() }
        )
        DropdownMenuItem(
            text = { Text("Delete Track", color = MaterialTheme.colorScheme.error) },
            leadingIcon = {
                Icon(
                    Icons.Default.DeleteForever,
                    null,
                    tint = MaterialTheme.colorScheme.error
                )
            },
            onClick = {
                onDeleteTracks(); onDismiss()
            }
        )
    }
}
@Composable
fun PlaylistMenu(
    expanded: Boolean,
    onDismiss: () -> Unit,
    onEditPLaylist: () -> Unit,
    onDownloadPlaylist: () -> Unit,
    onDeletePlaylist: () -> Unit
) {
    DropdownMenu(
        expanded = expanded,
        onDismissRequest = onDismiss,
        containerColor = MaterialTheme.colorScheme.surface,
        shape = RoundedCornerShape(16.dp)
    ) {
        DropdownMenuItem(
            text = { Text("Edit Playlist") },
            leadingIcon = { Icon(Icons.Default.EditNote, null) },
            onClick = { onEditPLaylist(); onDismiss() }
        )
        DropdownMenuItem(
            text = { Text("Download") },
            leadingIcon = { Icon(Icons.Default.Download, null) },
            onClick = { onDownloadPlaylist(); onDismiss() }
        )
        DropdownMenuItem(
            text = {
                Text(
                    "Delete Playlist",
                    color = MaterialTheme.colorScheme.error
                )
            },
            leadingIcon = {
                Icon(
                    Icons.Default.DeleteForever,
                    null,
                    tint = MaterialTheme.colorScheme.error
                )
            },
            onClick = {onDeletePlaylist(); onDismiss()
            }
        )
    }
}