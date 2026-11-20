package com.example.vulpineplayer.screens
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.vulpineplayer.components.CardPlaylist
import com.example.vulpineplayer.components.CardPlaylistMode
import com.example.vulpineplayer.components.PlaylistSelectionMenu
import com.example.vulpineplayer.components.SelectionTopBar
import com.example.vulpineplayer.model.PlaylistData
import com.example.vulpineplayer.viewmodel.PlaylistViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PlaylistsScreen(playlists: List<PlaylistData>,
                    playlistViewModel: PlaylistViewModel,
                    onPlaylistClick: (PlaylistData) -> Unit,
                    onCreatePlaylistClick: () -> Unit) {
    var isSelectionMode by remember { mutableStateOf(false) }
    var selectedIds by remember {mutableStateOf(setOf<Long>())}
    var isMenuOpen by remember { mutableStateOf(false) }
    var showCreateDialog by remember { mutableStateOf(false) }

    Column (modifier = Modifier.fillMaxSize()) {
        if (isSelectionMode){
            SelectionTopBar(
                selectedCount = selectedIds.size,
                onCancel = {
                    isSelectionMode = false
                    selectedIds = emptySet()
                },
                isMenuOpen = isMenuOpen,
                onMenuExpandedChange = { isMenuOpen = it },
                dropdownMenu = {
                    PlaylistSelectionMenu(
                        expanded = isMenuOpen,
                        onDismiss = { isMenuOpen = false },
                        onChangeColor = { /* TODO */ },
                        onDownloadPlaylists = { /* TODO */ },
                        onDeletePlaylists = {
                            playlistViewModel.deletePlaylists(selectedIds)
                            isSelectionMode = false
                            selectedIds = emptySet()
                        }
                    )
                }
            )
        } else {
            Button (
                onClick = { showCreateDialog = true },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 8.dp)
                    .height(48.dp),
                shape = RoundedCornerShape(24.dp),
                colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.secondary)
            ) {
                Icon(Icons.Default.Add, contentDescription = null)
                Spacer(modifier = Modifier.width(16.dp))
                Text("New Playlist",
                    style = MaterialTheme.typography.titleMedium,
                    color = MaterialTheme.colorScheme.onBackground)
            }
        }
        if (playlists.isEmpty()) {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                Text(
                    text = "No playlists yet",
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    style = MaterialTheme.typography.displaySmall
                )
            }
        } else {
            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                contentPadding = PaddingValues(bottom = 16.dp)
            ) {
                items(playlists, key = { it.id }) { playlist ->
                    val isSelected = selectedIds.contains(playlist.id)
                    CardPlaylist(
                        playlist = playlist,
                        trackCount = 0,
                        isCached = false,
                        mode = if (isSelectionMode) CardPlaylistMode.SELECTION else CardPlaylistMode.NORMAL,
                        isSelected = isSelected,
                        onClick = {
                            if (isSelectionMode) {
                                // Если в режиме выбора: добавляем или убираем из списка
                                selectedIds = if (isSelected) selectedIds - playlist.id else selectedIds + playlist.id
                            } else {
                                // Если обычный режим: открываем плейлист
                                onPlaylistClick(playlist)
                            }
                        },
                        onLongClick = {
                            // Долгое нажатие включает режим выбора
                            if (!isSelectionMode) {
                                isSelectionMode = true
                                selectedIds = setOf(playlist.id)
                            }
                        },
                        onMoreClick = { /* Пока пусто */ }
                    )
                }
            }
        }
    }
    if (showCreateDialog) {
        var playlistName by remember { mutableStateOf("") }

        ModalBottomSheet(
            onDismissRequest = { showCreateDialog = false },
            containerColor = MaterialTheme.colorScheme.surface
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = "Name",
                    style = MaterialTheme.typography.titleLarge,
                    color = MaterialTheme.colorScheme.onSurface
                )

                Spacer(modifier = Modifier.height(16.dp))

                // Поле ввода
                OutlinedTextField(
                    value = playlistName,
                    onValueChange = { playlistName = it },
                    label = { Text("New Playlist") },
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true,
                    trailingIcon = {
                        // Показываем крестик только если в поле что-то есть
                        if (playlistName.isNotEmpty()) {
                            IconButton(onClick = { playlistName = "" }) {
                                Icon(
                                    imageVector = Icons.Default.Clear, // Иконка крестика
                                    contentDescription = "Clear text",
                                    tint = MaterialTheme.colorScheme.onSurfaceVariant
                                )
                            }
                        }
                    }
                )

                Spacer(modifier = Modifier.height(24.dp))

                Button(
                    onClick = {
                        if (playlistName.isNotBlank()) {
                            playlistViewModel.createPlaylist(
                                name = playlistName,
                                colorHEX = "#F2A93B"
                            )
                            showCreateDialog = false // Закрываем модалку
                        }
                    },
                    modifier = Modifier.fillMaxWidth().height(48.dp),
                    shape = RoundedCornerShape(24.dp)
                ) {
                    Text("Create")
                }

                Spacer(modifier = Modifier.height(16.dp))
            }
        }
    }
}