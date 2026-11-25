package com.example.vulpineplayer.player

import android.content.ComponentName
import android.content.Context
import androidx.media3.session.MediaController
import android.util.Log
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.media3.common.MediaItem
import androidx.media3.common.PlaybackException
import androidx.media3.common.Player
import androidx.media3.session.SessionToken
import com.example.vulpineplayer.data.db.Enity.TrackData
import com.google.common.util.concurrent.ListenableFuture
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch

class PlayerViewModel : ViewModel() {
    private var mediaController: MediaController? = null

    private var controllerFuture: ListenableFuture<MediaController>? = null
    /*Хранение списка треков в очереди*/
    private var queue : List<TrackData> = emptyList()

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

    private val playerListener = object : Player.Listener {

        // Плеер начал или прекратил воспроизведение.
        override fun onPlayWhenReadyChanged(
            playWhenReady: Boolean,
            reason: Int
        ) {
            _isPlaying.value = playWhenReady
        }

        // Переключился текущий трек.
        override fun onMediaItemTransition(
            mediaItem: MediaItem?,
            reason: Int
        ) {
            updateCurrentTrack()
            updatePlaybackInfo()
        }

        // Изменилось состояние плеера.
        override fun onPlaybackStateChanged(playbackState: Int) {
            // Если очередь закончилась, показываем Play.
            if (playbackState == Player.STATE_ENDED) {
                _isPlaying.value = false
            }

            updatePlaybackInfo()

            Log.d(
                "PLAYER_TEST",
                "Состояние плеера изменилось: $playbackState"
            )
        }

        // Возникла ошибка воспроизведения.
        override fun onPlayerError(error: PlaybackException) {
            _isPlaying.value = false

            Log.e(
                "PLAYER_TEST",
                "Ошибка плеера: ${error.message}",
                error
            )
        }
    }

    fun connectToPlaybackService(context: Context) {

        // Не создаём несколько подключений одновременно.
        if (controllerFuture != null || mediaController != null) {
            return
        }

        val appContext = context.applicationContext

        // Адресуемся именно к нашему PlaybackService.
        val sessionToken = SessionToken(
            appContext,
            ComponentName(
                appContext,
                PlaybackService::class.java
            )
        )

        // Подключение асинхронное:
        // контроллер появится не сразу.
        val future = MediaController.Builder(
            appContext,
            sessionToken
        ).buildAsync()

        controllerFuture = future

        future.addListener(
            {
                try {
                    val controller = future.get()

                    // ViewModel могла уничтожиться,
                    // пока шло подключение.
                    if (controllerFuture !== future) {
                        return@addListener
                    }

                    mediaController = controller

                    // Регистрируем слушатель один раз,
                    // а не при каждом переключении трека.
                    controller.addListener(playerListener)
                    _isPlaying.value =
                        controller.playWhenReady &&
                                controller.playbackState != Player.STATE_ENDED

                    // Получаем актуальное состояние сервиса.
                    updateCurrentTrack()
                    updatePlaybackInfo()

                    Log.d(
                        "PLAYER_TEST",
                        "Подключение к PlaybackService успешно"
                    )

                } catch (e: Exception) {
                    Log.e(
                        "PLAYER_TEST",
                        "Не удалось подключиться к PlaybackService",
                        e
                    )

                    if (controllerFuture === future) {
                        controllerFuture = null
                    }
                }
            },
            ContextCompat.getMainExecutor(appContext)
        )
    }

/*    сохраняет список и говорит "включи трек под номером X*/
fun playTrack(
    tracks: List<TrackData>,
    startIndex: Int
) {
    val controller = mediaController ?: run {
        Log.e(
            "PLAYER_TEST",
            "Плеер ещё не подключён к сервису"
        )
        return
    }

    if (tracks.isEmpty()) return
    if (startIndex !in tracks.indices) return

    queue = tracks

    // Преобразуем собственные TrackData в MediaItem.
    val mediaItems = tracks.map { track ->
        MediaItem.fromUri(track.uri)
    }

    // Передаём сервису всю очередь и начальный индекс.
    controller.setMediaItems(
        mediaItems,
        startIndex,
        0L
    )

    controller.prepare()
    controller.play()

    _progress.value = 0f
    _currentPositionMs.value = 0L
}
    fun playPause() {
        val controller = mediaController ?: return

        if (controller.playWhenReady) {
            controller.pause()
        } else {
            controller.play()
        }
    }
    fun playAtIndex(index: Int) {
        val controller = mediaController ?: return

        if (index !in queue.indices) return

        controller.seekTo(index, 0L)
        controller.play()
    }


    fun playNext() {
        val controller = mediaController ?: return

        if (controller.hasNextMediaItem()) {
            controller.seekToNextMediaItem()
        }
    }
    fun playPrevious() {
        val controller = mediaController ?: return

        if (controller.hasPreviousMediaItem()) {
            controller.seekToPreviousMediaItem()
        }
    }

    fun seekTo(progress: Float) {
        val controller = mediaController ?: return

        val duration = controller.duration

        if (duration <= 0L) return

        // Ограничиваем значение диапазоном 0..1.
        val safeProgress = progress.coerceIn(0f, 1f)

        val newPosition = (
                duration * safeProgress
                ).toLong()

        controller.seekTo(newPosition)

        _progress.value = safeProgress
        _currentPositionMs.value = newPosition
    }
    private fun updateCurrentTrack() {
        val controller = mediaController ?: return

        val index = controller.currentMediaItemIndex

        _currentTrack.value = queue.getOrNull(index)
    }
    private fun updatePlaybackInfo() {
        val controller = mediaController ?: return

        val position = controller.currentPosition
        val duration = controller.duration

        _currentPositionMs.value = position

        if (duration > 0L) {
            _durationMs.value = duration

            _progress.value = (
                    position.toFloat() / duration.toFloat()
                    ).coerceIn(0f, 1f)
        } else {
            _durationMs.value = 0L
            _progress.value = 0f
        }
    }
    init {
        viewModelScope.launch {
            while (isActive) {
                delay(250)

                if (isUiPaused) continue

                val controller = mediaController ?: continue

                updatePlaybackInfo()
            }
        }
    }
    override fun onCleared() {
        // ViewModel больше не должна получать события.
        mediaController?.removeListener(playerListener)

        // Освобождаем контроллер и отключаемся от сервиса.
        controllerFuture?.let { future ->
            MediaController.releaseFuture(future)
        }

        mediaController = null
        controllerFuture = null

        super.onCleared()
    }
}