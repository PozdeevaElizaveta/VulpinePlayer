package com.example.vulpineplayer
import android.content.pm.PackageManager
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.animation.EnterTransition
import androidx.compose.animation.ExitTransition
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.example.vulpineplayer.ui.theme.VulpinePlayerTheme
import androidx.compose.ui.unit.dp
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Folder
import androidx.compose.material.icons.filled.MusicNote
import androidx.compose.material3.BottomSheetDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.platform.LocalContext
import androidx.core.content.ContextCompat
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.vulpineplayer.components.MiniPlayer
import com.example.vulpineplayer.model.LibraryUiState
import com.example.vulpineplayer.ui.screens.library.LibraryScreen
import com.example.vulpineplayer.ui.screens.nowplaying.NowPlayingScreen
import com.example.vulpineplayer.ui.screens.playlists.PlaylistsScreen
import com.example.vulpineplayer.ui.theme.AccentPrimary
import com.example.vulpineplayer.ui.theme.AccentSecondary
import com.example.vulpineplayer.ui.theme.TextMuted
import com.example.vulpineplayer.ui.screens.library.LibraryViewModel
import com.example.vulpineplayer.player.PlayerViewModel
import com.example.vulpineplayer.ui.screens.playlists.PlaylistViewModel


class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            VulpinePlayerTheme {
                MainScreen()
            }
        }
    }
}
/*функция, чтобы не дублировать код для кнопок в mainscreen, создаем сами кнопки
* выравниваем и цвет от состояния*/
@Composable
fun BottomNavItem(
    icon: ImageVector,
    title: String,
    isSelect: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier.fillMaxHeight()
            .clickable(
                interactionSource = remember{ MutableInteractionSource()},
                indication = null
            ) { onClick() },
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
    ) {
        Icon(
            imageVector = icon,
            contentDescription = title,
            tint = if (isSelect) AccentPrimary else AccentSecondary
        )
        Text(
            text = title,
            color = if (isSelect) AccentPrimary else TextMuted,
            style = MaterialTheme.typography.headlineSmall
        )
    }
}
/*создаем конкретные наши кнопки*/
data class NavItem(val icon: ImageVector,val title: String,val route: String)
val items = listOf(
    NavItem(Icons.Default.Home,"Home", "home"),
    NavItem(Icons.Default.MusicNote,"Library", "library"),
    NavItem(Icons.Default.Folder,"Playlists", "playlists")
)


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainScreen(){
    val navController = rememberNavController()
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route

   /* Получаем базу*/
    val context = LocalContext.current
    val container = (context.applicationContext as VulpineApplication).container

    /*Создаем ViewModel через Фабрику*/
    val libraryViewModel: LibraryViewModel = viewModel(
      factory = container.libraryViewModelFactory)
    val playlistViewModel: PlaylistViewModel=viewModel(
        factory = container.playlistViewModelFactory)

    val playerViewModel: PlayerViewModel = viewModel()

    val tracks by libraryViewModel.tracks.collectAsStateWithLifecycle()
    val playlists by playlistViewModel.playlists.collectAsStateWithLifecycle()

    val currentTrack by playerViewModel.currentTrack.collectAsStateWithLifecycle()
    val isPlaying by playerViewModel.isPlaying.collectAsStateWithLifecycle()

    val permissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission()
    ) {
        isGranted ->
        if (isGranted){
            libraryViewModel.scanANDSaveMusic()
        }
    }
/*Проверка на наличие разрешения на чтение музыки, если есть -сканируем, если нет - сист окно дать доступ*/
    LaunchedEffect(Unit) {
        val permissionCheckResult = ContextCompat.checkSelfPermission(
            context,
            android.Manifest.permission.READ_MEDIA_AUDIO
        )
        if (permissionCheckResult == PackageManager.PERMISSION_GRANTED){
            libraryViewModel.scanANDSaveMusic()
        } else {
            permissionLauncher.launch(android.Manifest.permission.READ_MEDIA_AUDIO)
        }
    }
    val isNowPlayingOpen = rememberSaveable {mutableStateOf(false) }
    var selectedPlaylistId by remember { mutableStateOf<Long?>(null) }


    Scaffold(modifier = Modifier.fillMaxSize(),
        topBar = {Row(modifier = Modifier
            .fillMaxWidth()
            .background(MaterialTheme.colorScheme.background)
            .padding(16.dp)
            .statusBarsPadding(),
            verticalAlignment = Alignment.CenterVertically) {
            Row(modifier = Modifier
                .weight(1f)
                .height(56.dp)
                .background(
                    MaterialTheme.colorScheme.surface,
                    shape = RoundedCornerShape(32.dp)
                )
                .padding(horizontal = 20.dp),verticalAlignment = Alignment.CenterVertically) {
                Icon(imageVector = Icons.Default.Search, contentDescription = "Search", modifier = Modifier
                    .size(24.dp),
                    tint = MaterialTheme.colorScheme.onSurfaceVariant)
                Spacer(modifier = Modifier.width(16.dp))
                Text("Search", color = MaterialTheme.colorScheme.onSurfaceVariant, modifier = Modifier.weight(1f),
                    style = MaterialTheme.typography.labelLarge)
            }
            Spacer(modifier = Modifier.width(16.dp))
            Icon(imageVector = Icons.Default.Settings, contentDescription = "Settings", modifier = Modifier
                .size(24.dp),
                tint = MaterialTheme.colorScheme.onSurfaceVariant)

        }},
        bottomBar = {
            Column() {
                if (currentTrack != null) {
                    MiniPlayer(
                        track = currentTrack!!,
                        isPlaying = isPlaying,
                        playerViewModel = playerViewModel,
                        onPreviousClick = {playerViewModel.PlayPrevious(context)},
                        onPlayPauseClick = { playerViewModel.PlayPause() },
                        onNextClick = {playerViewModel.PlayNext(context)},
                        onClick = { isNowPlayingOpen.value=true}
                    )
                }
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(MaterialTheme.colorScheme.background)
                        .navigationBarsPadding()
                        .height(80.dp)
                        .padding(horizontal = 16.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    items.forEach { item ->
                        BottomNavItem(
                            modifier = Modifier.weight(1f),
                            icon = item.icon,
                            title = item.title,
                            isSelect = currentRoute == item.route,
                            onClick = {
                                navController.navigate(item.route){
                                    popUpTo(navController.graph.startDestinationId)
                                    launchSingleTop = true
                                    restoreState = true
                                }
                            }
                        )
                    }
                }
            }
        }
    ) { innerPadding ->
        NavHost(
            navController = navController,
            startDestination = "home",
            modifier = Modifier
            .padding(innerPadding)
            .fillMaxSize(),
            contentAlignment = Alignment.Center,
            enterTransition = { EnterTransition.None},
            exitTransition = { ExitTransition.None},
            popEnterTransition = { EnterTransition.None },
            popExitTransition = { ExitTransition.None }){
            composable("home"){
                    Text("Home Page")
                }
            composable("library") {
                LibraryScreen(
                    uiState = LibraryUiState.Success(tracks),
                    currentTrack = currentTrack,
                    onTrackClick = { clickedTrack ->
                        val index = tracks.indexOf(clickedTrack)
                        if (index != -1) {
                            playerViewModel.playTrack(context, tracks, index)
                        }
                    })
            }

            composable("playlists"){
                PlaylistsScreen(
                    playlists=playlists,
                    playlistViewModel=playlistViewModel,
                    onPlaylistClick = {playlist->
                        selectedPlaylistId = playlist.id
                    },
                    onCreatePlaylistClick = {}
                )
            }
        }
    }
    if (isNowPlayingOpen.value) {
        ModalBottomSheet(
            onDismissRequest = { isNowPlayingOpen.value = false },
            sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true),
            containerColor = MaterialTheme.colorScheme.background,
            dragHandle = { BottomSheetDefaults.DragHandle(width = 64.dp) }
        ) {
            NowPlayingScreen(
                track = currentTrack!!,
                isPlaying = isPlaying,
                playerViewModel = playerViewModel,
                onPlayPauseClick = { playerViewModel.PlayPause() },
                onNextClick = { playerViewModel.PlayNext(context) },
                onPreviousClick = { playerViewModel.PlayPrevious(context) },
                onSeek = { newProgress -> playerViewModel.seekTo(newProgress) },
                onClose = { isNowPlayingOpen.value = false },
            )
        }
    }
}



@Preview(showBackground = true)
@Composable
fun Preview() {
    VulpinePlayerTheme {
        MainScreen()
    }
}