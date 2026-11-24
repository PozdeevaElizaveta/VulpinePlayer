package com.example.vulpineplayer.player

import android.content.Context
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.media3.common.MediaItem
import androidx.media3.common.PlaybackException
import androidx.media3.common.Player
import androidx.media3.exoplayer.ExoPlayer
import com.example.vulpineplayer.data.db.Enity.TrackData
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class PlayerViewModel : ViewModel() {
    private var exoPlayer: ExoPlayer? = null
    /*Хранение списка треков в очереди*/
    private var queue : List<TrackData> = emptyList()
    private var currentIndex: Int = -1
    /*  Храним текущий играющий трек*/
    private val _currentTrack = MutableStateFlow<TrackData?>(null)
    val currentTrack: StateFlow<TrackData?> = _currentTrack.asStateFlow()
    /*Храним состояние: играет или на паузе*/
    private val _isPlaying = MutableStateFlow(false)
    val isPlaying : StateFlow<Boolean> = _isPlaying.asStateFlow()
    private val _progress = MutableStateFlow(0f)
    val progress : StateFlow<Float> = _progress.asStateFlow()
    private val _currentPositionMs = MutableStateFlow(0L)
    val currentPositionMs: StateFlow<Long> = _currentPositionMs.asStateFlow()

    private val _durationMs = MutableStateFlow(0L)
    val durationMs: StateFlow<Long> = _durationMs.asStateFlow()
    //чтобы экран в мею не моргал
    private var isUiPaused = false
    fun setUiPaused(paused: Boolean) {
        isUiPaused = paused
    }

    /*расчет прогресса для прогресс-бара при проигрывании песни*/
    init {
        viewModelScope.launch {
            while (true){
                delay(250)
                if (isUiPaused) continue

                val miniplayer = exoPlayer
                if (miniplayer != null&&miniplayer.isPlaying){
                    val currentPosition = miniplayer.currentPosition.toFloat()
                    val duration = miniplayer.duration.toFloat()

                    if (duration>0){
                        _progress.value = currentPosition / duration

                        _currentPositionMs.value = miniplayer.currentPosition
                        _durationMs.value = miniplayer.duration
                    }
                }
            }
        }
    }
/*    сохраняет список и говорит "включи трек под номером X*/
    fun playTrack(context: Context, tracks: List<TrackData>, startIndex: Int) {
        queue = tracks
        PlayAtIndex(context, startIndex)
    }
    fun PlayPause(){
        val player = exoPlayer ?:return
        if (player.isPlaying){
            player.pause()
            _isPlaying.value = false
        } else {
            player.play()
            _isPlaying.value = true
        }
    }
    fun PlayAtIndex(context: Context, index: Int){
        val track = queue.getOrNull(index)?:return
        currentIndex = index
        _currentTrack.value=track
        _isPlaying.value = true
        _progress.value = 0f
        if (exoPlayer==null){
            exoPlayer = ExoPlayer.Builder(context).build()
        }
        val mediaItem = MediaItem.fromUri(track.uri)
        exoPlayer?.addListener(object : Player.Listener {
            override fun onPlayerError(error: PlaybackException) {
                Log.e("PLAYER_TEST", "ОШИБКА ПЛЕЕРА: ${error.message}")
            }
            override fun onPlaybackStateChanged(playbackState: Int) {
                Log.d("PLAYER_TEST", "Состояние плеера изменилось: $playbackState")
            }
        })
        exoPlayer?.setMediaItem(mediaItem)
        exoPlayer?.prepare()
        exoPlayer?.play()

    }

    fun PlayNext(context: Context){
        if(currentIndex< queue.size-1){
            PlayAtIndex(context,currentIndex+1)
        }
    }
    fun PlayPrevious(context: Context){
        if(currentIndex>0){
            PlayAtIndex(context,currentIndex-1)
        }
    }
    fun seekTo(progress: Float){
        val player = exoPlayer?:return
        val duration = player.duration
        if (duration>0){
            val newPOsition = (duration*progress).toLong()
            player.seekTo(newPOsition)
            _progress.value=progress
        }
    }
}