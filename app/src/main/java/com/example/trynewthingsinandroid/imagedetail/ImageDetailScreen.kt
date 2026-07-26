package com.example.trynewthingsinandroid.imagedetail

import android.app.Activity
import android.content.Intent
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Download
import androidx.compose.material.icons.filled.Share
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.unit.dp
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.WindowInsetsControllerCompat
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.AsyncImage

/**
 * Screen displaying details for a specific image.
 *
 * @param onBackClick Callback invoked when the user clicks the back button.
 * @param viewModel The ViewModel providing the state for this screen.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ImageDetailScreen(
    onBackClick: () -> Unit,
    viewModel: ImageDetailsViewModel = viewModel()
) {
    val uiState by viewModel.uiState.collectAsState()
    val context = LocalContext.current
    val view = LocalView.current

    // Collect and handle UI effects (one-time events like sharing)
    LaunchedEffect(Unit) {
        viewModel.uiEffect.collect { effect ->
            when (effect) {
                is ImageDetailsScreenUIEffect.ShareImageUrl -> {
                    val sendIntent: Intent = Intent().apply {
                        action = Intent.ACTION_SEND
                        putExtra(Intent.EXTRA_TEXT, "Check out this wallpaper: ${effect.url}")
                        type = "text/plain"
                    }
                    val shareIntent = Intent.createChooser(sendIntent, null)
                    context.startActivity(shareIntent)
                }
            }
        }
    }

    // Toggle System Bars visibility based on full screen state
    LaunchedEffect(uiState.isImageFullScreen) {
        val window = (view.context as? Activity)?.window ?: return@LaunchedEffect
        val controller = WindowCompat.getInsetsController(window, view)
        
        if (uiState.isImageFullScreen) {
            controller.show(WindowInsetsCompat.Type.systemBars())
            controller.systemBarsBehavior = WindowInsetsControllerCompat.BEHAVIOR_DEFAULT
        } else {
            controller.hide(WindowInsetsCompat.Type.systemBars())
            controller.systemBarsBehavior = WindowInsetsControllerCompat.BEHAVIOR_SHOW_TRANSIENT_BARS_BY_SWIPE
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .clickable(
                interactionSource = remember { MutableInteractionSource() },
                indication = null
            ) {
                viewModel.onUIEvent(ImageDetailsScreenUIEvent.OnImageClicked)
            }
    ) {
        AsyncImage(
            model = uiState.selectedImage?.url,
            contentDescription = "Wallpaper Detail",
            modifier = Modifier.fillMaxSize(),
            contentScale = ContentScale.Crop
        )

        if (uiState.isImageFullScreen) {
            Scaffold(
                containerColor = Color.Transparent,
                topBar = {
                    TopAppBar(
                        title = {
                            Text(
                                text = "Image Detail",
                                color = Color.White
                            )
                        },
                        navigationIcon = {
                            IconButton(onClick = onBackClick) {
                                Icon(
                                    imageVector = Icons.Default.ArrowBack,
                                    contentDescription = "Back",
                                    tint = Color.White
                                )
                            }
                        },
                        colors = TopAppBarDefaults.topAppBarColors(
                            containerColor = Color.Transparent
                        )
                    )
                }
            ) { paddingValues: PaddingValues ->
                Box(modifier = Modifier.padding(paddingValues))
            }

            ImageDetailsBottomSheetUI(
                activeBottomSheet = uiState.activeBottomSheet,
                onEvent = viewModel::onUIEvent
            )
        }
    }
}

/**
 * Composable representing the bottom sheet UI for image details.
 *
 * @param activeBottomSheet The currently active bottom sheet to display.
 * @param onEvent Callback invoked for UI events.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ImageDetailsBottomSheetUI(
    activeBottomSheet: ImageDetailsScreenBottomSheet,
    onEvent: (ImageDetailsScreenUIEvent) -> Unit
) {
    val sheetState = rememberModalBottomSheetState()

    if (activeBottomSheet == ImageDetailsScreenBottomSheet.Options) {
        ModalBottomSheet(
            onDismissRequest = { 
                onEvent(ImageDetailsScreenUIEvent.DismissBottomSheet) 
            },
            sheetState = sheetState,
            containerColor = Color.Black.copy(alpha = 0.4f),
            dragHandle = null,
            scrimColor = Color.Transparent
        ) {
            OptionsBottomSheet(onEvent = onEvent)
        }
    }
}

/**
 * Composable representing the options bottom sheet with share and download actions.
 *
 * @param onEvent Callback invoked for UI events.
 * @param modifier The modifier to be applied to the layout.
 */
@Composable
fun OptionsBottomSheet(
    onEvent: (ImageDetailsScreenUIEvent) -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(bottom = 100.dp, top = 32.dp),
        horizontalArrangement = Arrangement.spacedBy(32.dp, Alignment.CenterHorizontally),
        verticalAlignment = Alignment.CenterVertically
    ) {
        IconButton(onClick = { onEvent(ImageDetailsScreenUIEvent.OnShareClicked) }) {
            Icon(
                imageVector = Icons.Default.Share,
                contentDescription = "Share",
                tint = Color.White
            )
        }
        IconButton(onClick = { onEvent(ImageDetailsScreenUIEvent.OnDownloadClicked) }) {
            Icon(
                imageVector = Icons.Default.Download,
                contentDescription = "Download",
                tint = Color.White
            )
        }
    }
}
