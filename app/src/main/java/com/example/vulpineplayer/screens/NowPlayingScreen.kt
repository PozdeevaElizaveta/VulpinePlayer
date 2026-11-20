package com.example.vulpineplayer.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.basicMarquee
import androidx.compose.foundation.border
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
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.List
import androidx.compose.material.icons.filled.Cancel
import androidx.compose.material.icons.filled.CreateNewFolder
import androidx.compose.material.icons.filled.DeleteOutline
import androidx.compose.material.icons.filled.Download
import androidx.compose.material.icons.filled.DriveFolderUpload
import androidx.compose.material.icons.filled.EditNote
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.filled.Pause
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Repeat
import androidx.compose.material.icons.filled.Share
import androidx.compose.material.icons.filled.Shuffle
import androidx.compose.material.icons.filled.SkipNext
import androidx.compose.material.icons.filled.SkipPrevious
import androidx.compose.material3.BottomSheetDefaults
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Slider
import androidx.compose.material3.SliderDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.vulpineplayer.R
import com.example.vulpineplayer.model.TrackData
import com.example.vulpineplayer.model.formatTime
import com.example.vulpineplayer.ui.theme.Red
import com.example.vulpineplayer.viewmodel.PlayerViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NowPlayingScreen(track: TrackData,
                     isPlaying: Boolean,
                     playerViewModel: PlayerViewModel,
                     onPreviousClick:()-> Unit,
                     onPlayPauseClick:()-> Unit,
                     onNextClick:()-> Unit,
                     onSeek:(Float)-> Unit,
                     onClose:()-> Unit){
    // Выключатели для кнопок
    var isShuffleOn by remember { mutableStateOf(false) }
    var isRepeatOn by remember { mutableStateOf(false) }
    var isFavorite by remember { mutableStateOf(false) }
    Column(
        modifier = Modifier.fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center)
    {
        Box(
            modifier = Modifier.fillMaxWidth(0.9f)
                .aspectRatio(1f)
                .shadow(elevation = 12.dp, shape = RoundedCornerShape(16.dp))
                .clip(RoundedCornerShape(16.dp))
                .background(color = MaterialTheme.colorScheme.surface),
            contentAlignment = Alignment.Center) {
            Icon(
                modifier = Modifier.fillMaxSize(0.7f),
                painter = painterResource(id = R.drawable.fox),
                contentDescription = "Fox",
                tint = MaterialTheme.colorScheme.secondary
            )
        }
        Spacer(modifier = Modifier.height(16.dp))
        Text(
            text = track.name,
            style = MaterialTheme.typography.displayMedium,
            color = MaterialTheme.colorScheme.onBackground,
            textAlign = TextAlign.Center,
            maxLines = 1,
            modifier = Modifier.fillMaxWidth()
                .basicMarquee()
        )
        Text(text = track.artist,
            style = MaterialTheme.typography.titleMedium,
            color = MaterialTheme.colorScheme.onBackground,
            textAlign = TextAlign.Center,
            maxLines = 1,
            modifier = Modifier.fillMaxWidth()
                .basicMarquee()
        )
        Spacer(modifier = Modifier.height(16.dp))
        Box(
            modifier = Modifier.fillMaxWidth()
                .height(57.dp)
                .clip(RoundedCornerShape(8.dp))
                .background(MaterialTheme.colorScheme.surface.copy(0.3f))
                .clickable(){

                },
            contentAlignment = Alignment.Center
        ){
            Text(
                text = "No lyrics",
                style = MaterialTheme.typography.headlineSmall,
                color = MaterialTheme.colorScheme.secondary
            )
        }
        Spacer(modifier = Modifier.height(24.dp))
        PlayerProgressBar(playerViewModel=playerViewModel, onSeek=onSeek)
        PlayerTime(playerViewModel=playerViewModel)
        Spacer(modifier = Modifier.height(24.dp))
        Row(modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceEvenly,
            verticalAlignment = Alignment.CenterVertically) {
            IconButton(onClick = { isShuffleOn=!isShuffleOn}) {
                Icon(
                    imageVector = Icons.Default.Shuffle,
                    contentDescription = "Shuffle",
                    tint = if (isShuffleOn) MaterialTheme.colorScheme.onBackground
                    else MaterialTheme.colorScheme.onSurfaceVariant
                )
            }

            IconButton(onClick = onPreviousClick) {
                Icon(
                    imageVector = Icons.Default.SkipPrevious,
                    contentDescription = "Previous",
                    modifier = Modifier.size(48.dp),
                    tint = MaterialTheme.colorScheme.onBackground
                )
            }

            Box(
                modifier = Modifier
                    .size(72.dp)
                    .clip(CircleShape)
                    .clickable { onPlayPauseClick() },
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = if (isPlaying) Icons.Default.Pause else Icons.Default.PlayArrow,
                    contentDescription = if (isPlaying) "Pause" else "Play",
                    tint = MaterialTheme.colorScheme.primary,
                    modifier = Modifier.size(64.dp)
                )
            }

            IconButton(onClick = onNextClick) {
                Icon(
                    imageVector = Icons.Default.SkipNext,
                    contentDescription = "Next",
                    modifier = Modifier.size(48.dp),
                    tint = MaterialTheme.colorScheme.onBackground
                )
            }

            IconButton(onClick = { isRepeatOn=!isRepeatOn}) {
                Icon(
                    imageVector = Icons.Default.Repeat,
                    contentDescription = "Repeat",
                    tint = if (isRepeatOn) MaterialTheme.colorScheme.onBackground
                    else MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }
        Spacer(modifier = Modifier.height(24.dp))
        Row(modifier = Modifier
            .fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceEvenly,
            verticalAlignment = Alignment.CenterVertically) {
            IconButton(onClick = { isFavorite=!isFavorite }) {
                Icon(
                    imageVector = Icons.Default.FavoriteBorder,
                    contentDescription = "Favorite",
                    tint = if (isFavorite) MaterialTheme.colorScheme.primary
                    else MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
            IconButton(onClick = { }) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.List,
                    contentDescription = "Queue",
                    tint = MaterialTheme.colorScheme.onBackground
                )
            }

            var showMenuSheet by remember { mutableStateOf(false) }
            LaunchedEffect(showMenuSheet) {
                playerViewModel.setUiPaused(showMenuSheet)
            }
            Box() {
                IconButton(onClick = { showMenuSheet=true}) {
                    Icon(
                        imageVector = Icons.Default.MoreVert,
                        contentDescription = "More",
                        tint = MaterialTheme.colorScheme.onBackground
                    )
                }
            }
            if (showMenuSheet){
                ModalBottomSheet(
                    onDismissRequest = {showMenuSheet = false},
                    containerColor = MaterialTheme.colorScheme.surface,
                    dragHandle = { BottomSheetDefaults.DragHandle()}
                ) {
                    Column(modifier = Modifier.fillMaxWidth()
                        .padding(bottom = 32.dp)) {
                        Text(text = track.name,
                            style = MaterialTheme.typography.titleLarge,
                            color = MaterialTheme.colorScheme.onBackground,
                            modifier = Modifier.padding(horizontal = 24.dp, vertical = 8.dp))
                        HorizontalDivider(color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.1f))
                        DropdownMenuItem(
                            text = {Text("Track Info")},
                            leadingIcon = {Icon(Icons.Default.Info, null)},
                            onClick = {}
                        )
                        DropdownMenuItem(
                            text = {Text("Edit Track")},
                            leadingIcon = {Icon(Icons.Default.EditNote, null)},
                            onClick = {}
                        )
                        DropdownMenuItem(
                            text = {Text("Download")},
                            leadingIcon = {Icon(Icons.Default.Download, null)},
                            onClick = {}
                        )
                        DropdownMenuItem(
                            text = {Text("Add to Playlist")},
                            leadingIcon = {Icon(Icons.Default.CreateNewFolder, null)},
                            onClick = {}
                        )
                        DropdownMenuItem(
                            text = {Text("Go to Artist")},
                            leadingIcon = {Icon(Icons.Default.Person, null)},
                            onClick = {}
                        )
                        DropdownMenuItem(
                            text = {Text("Share")},
                            leadingIcon = {Icon(Icons.Default.Share, null)},
                            onClick = {}
                        )
                        DropdownMenuItem(
                            text = {Text("Delete Track", color = MaterialTheme.colorScheme.onSecondary)},
                            leadingIcon = {Icon(Icons.Default.DeleteOutline, null, tint = MaterialTheme.colorScheme.onSecondary)},
                            onClick = {}
                        )
                    }
                }
            }
        }
    }
}
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PlayerProgressBar(
    playerViewModel: PlayerViewModel,
    onSeek: (Float) -> Unit
){
    val progress by playerViewModel.progress.collectAsStateWithLifecycle()
    var isDragging by remember { mutableStateOf(false) }
    var sliderPosition by remember { mutableStateOf(progress) }
    LaunchedEffect(progress,isDragging) {
        if (!isDragging){
            sliderPosition = progress
        }
    }
    Slider(
        value = sliderPosition,
        onValueChange = {
            isDragging = true
            sliderPosition = it
        },
        onValueChangeFinished = {
            isDragging = false
            onSeek(sliderPosition)
        },
        modifier = Modifier.fillMaxWidth()
            .height(16.dp)
            .zIndex(1f)
            .padding(horizontal = 16.dp),
        thumb = {
            Box(modifier = Modifier.size(16.dp)
                .clip(RoundedCornerShape(16.dp))
                .background(MaterialTheme.colorScheme.primary))
        },
        track = {
                sliderState ->
            LinearProgressIndicator(
                progress = {sliderState.value},
                modifier = Modifier.fillMaxWidth()
                    .height(4.dp),
                color = MaterialTheme.colorScheme.primary,
                trackColor = MaterialTheme.colorScheme.secondary,
                strokeCap = StrokeCap.Butt,
                gapSize = 0.dp,
                drawStopIndicator = {}
            )
        },
        colors = SliderDefaults.colors(
            thumbColor = Color.Transparent,
            activeTrackColor = Color.Transparent,
            inactiveTrackColor = Color.Transparent,
            activeTickColor = Color.Transparent,
            inactiveTickColor = Color.Transparent
        )
    )
}
@Composable
fun PlayerTime(playerViewModel: PlayerViewModel){
    val currentPositionMs by playerViewModel.currentPositionMs.collectAsStateWithLifecycle()
    val durationMs by playerViewModel.durationMs.collectAsStateWithLifecycle()
    Row(modifier = Modifier.fillMaxWidth()
        .padding(top = 8.dp),
        horizontalArrangement = Arrangement.SpaceBetween) {
        Text(
            text = formatTime(currentPositionMs),
            style = MaterialTheme.typography.titleSmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
        Text(
            text = formatTime(durationMs),
            style = MaterialTheme.typography.titleSmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
    }
}
