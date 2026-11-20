package com.example.vulpineplayer.components

import androidx.compose.foundation.background
import androidx.compose.foundation.basicMarquee
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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Pause
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.SkipNext
import androidx.compose.material.icons.filled.SkipPrevious
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Slider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.media3.extractor.mp4.Track
import com.example.vulpineplayer.R
import com.example.vulpineplayer.model.LibraryUiState
import com.example.vulpineplayer.model.TrackData
import com.example.vulpineplayer.ui.theme.VulpinePlayerTheme
import com.example.vulpineplayer.viewmodel.PlayerViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MiniPlayer(track: TrackData,
               isPlaying: Boolean,
               playerViewModel: PlayerViewModel,
               onNextClick:()-> Unit,
               onPlayPauseClick:()-> Unit,
               onPreviousClick:()-> Unit,
               onClick:()-> Unit,){
    val progress by playerViewModel.progress.collectAsStateWithLifecycle()
    Column(modifier = Modifier.height(80.dp)
        .padding(top = 9.dp)
        .clickable { onClick()}){

        LinearProgressIndicator(
            progress = { progress },
            modifier = Modifier
                .fillMaxWidth()
                .height(4.dp), // Толщина линии
            color = MaterialTheme.colorScheme.primary,
            trackColor = MaterialTheme.colorScheme.secondary,
            strokeCap = StrokeCap.Butt, //  Делает концы ровными
            gapSize = 0.dp,             // Убирает разрыв между линией прогресса и фоном
            drawStopIndicator = {} // Убираем точку
        )

        Row(modifier = Modifier.background(color = MaterialTheme.colorScheme.surface)
            .clickable{onClick()}
            .padding(horizontal = 16.dp),
            verticalAlignment = Alignment.CenterVertically) {
            Box(modifier = Modifier
                .size(56.dp)
                .background(color = MaterialTheme.colorScheme.background,
                    shape = RoundedCornerShape(8.dp)),
                contentAlignment = Alignment.Center) {
                Icon(
                    painter = painterResource(id = R.drawable.fox),
                    contentDescription = "Fox",
                    tint = MaterialTheme.colorScheme.secondary
                )
            }
            Spacer(modifier = Modifier.width(20.dp))
            Column(modifier = Modifier
                .padding(vertical = 12.dp)
                .weight(1f),
                verticalArrangement = Arrangement.SpaceEvenly) {

                Text(track.name,
                    color = MaterialTheme.colorScheme.onBackground,
                    style = MaterialTheme.typography.headlineMedium,
                    maxLines = 1,
                    modifier = Modifier.weight(1f)
                        .basicMarquee())
                Text(track.artist,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    style = MaterialTheme.typography.labelMedium,
                    maxLines = 1,
                    modifier = Modifier.weight(1f)
                        .basicMarquee())
            }
            Row() {
                IconButton(onClick={onPreviousClick()}
                ) {
                    Icon(imageVector = Icons.Default.SkipPrevious, contentDescription = "SkipPrevious",
                        modifier = Modifier.align(Alignment.CenterVertically)
                            .size(32.dp),
                        tint = MaterialTheme.colorScheme.onBackground)
                }

                IconButton(
                    onClick = { onPlayPauseClick() }
                ) {
                    Icon(
                        imageVector = if (isPlaying) Icons.Default.Pause else Icons.Default.PlayArrow,
                        contentDescription = if (isPlaying) "Pause" else "Play",
                        tint = MaterialTheme.colorScheme.primary,
                        modifier = Modifier.size(48.dp)
                    )
                }
                IconButton(onClick={onNextClick()}
                ) {
                    Icon(imageVector = Icons.Default.SkipNext, contentDescription = "SkipNext",
                        modifier = Modifier.align(Alignment.CenterVertically)
                            .size(32.dp),
                        tint = MaterialTheme.colorScheme.onBackground)
                }

            }
        }
    }
}
