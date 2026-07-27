package com.example.trynewthingsinandroid.imagedetail

import android.app.Activity
import android.app.DownloadManager
import android.content.Context
import android.content.Intent
import android.os.Environment
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Download
import androidx.compose.material.icons.filled.Share
import androidx.compose.material3.BottomSheetScaffold
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberBottomSheetScaffoldState
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
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.core.net.toUri
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.WindowInsetsControllerCompat
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.AsyncImage
import com.example.trynewthingsinandroid.R

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
    val scaffoldState = rememberBottomSheetScaffoldState()

    // Collect and handle UI effects (one-time events like sharing)
    LaunchedEffect(Unit) {
        viewModel.uiEffect.collect { effect ->
            when (effect) {
                is ImageDetailsScreenUIEffect.ShareImageUrl -> {
                    shareImage(context = context, url = effect.url)
                }

                is ImageDetailsScreenUIEffect.DownloadImage -> {
                    downloadImage(
                        context = context,
                        url = effect.url,
                        fileName = effect.fileName,
                    )
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
            controller.systemBarsBehavior =
                WindowInsetsControllerCompat.BEHAVIOR_SHOW_TRANSIENT_BARS_BY_SWIPE
        }
    }

    BottomSheetScaffold(
        scaffoldState = scaffoldState,
        sheetPeekHeight = if (uiState.isImageFullScreen) 140.dp else 0.dp,
        sheetContainerColor = Color.Black.copy(alpha = 0.4f),
        sheetContentColor = Color.White,
        sheetDragHandle = null,
        sheetSwipeEnabled = uiState.isImageFullScreen,
        sheetContent = {
            OptionsBottomSheet(onEvent = viewModel::onUIEvent)
        },
        containerColor = Color.Black
    ) { _ ->
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
                Column {
                    TopAppBar(
                        title = {
                            Text(
                                text = stringResource(R.string.image_detail_title),
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

                    uiState.selectedImage?.photoCredit?.let { credit ->
                        Text(
                            text = stringResource(R.string.photo_credit_label, credit.name),
                            modifier = Modifier
                                .padding(start = 16.dp)
                                .background(
                                    color = Color.Black.copy(alpha = 0.4f),
                                    shape = RoundedCornerShape(8.dp)
                                )
                                .padding(horizontal = 12.dp, vertical = 6.dp)
                                .clickable(
                                    interactionSource = remember { MutableInteractionSource() },
                                    indication = null
                                ) { /* Consume click to prevent toggle */ },
                            color = Color.White,
                            style = MaterialTheme.typography.labelMedium
                        )
                    }
                }
            }
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
            .padding(bottom = 60.dp, top = 24.dp),
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

private fun shareImage(
    url: String,
    context: Context
) {
    val sendIntent: Intent = Intent().apply {
        action = Intent.ACTION_SEND
        putExtra(Intent.EXTRA_TEXT, context.getString(R.string.share_message_template, url))
        type = "text/plain"
    }
    val shareIntent = Intent.createChooser(sendIntent, null)
    context.startActivity(shareIntent)
}

private fun downloadImage(
    url: String,
    fileName: String,
    context: Context
) {
    val downloadManager = context.getSystemService(Context.DOWNLOAD_SERVICE) as DownloadManager
    val request = DownloadManager.Request(url.toUri()).apply {
        setTitle(fileName)
        setDescription(context.getString(R.string.downloading_wallpaper_message))
        setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED)
        setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS, fileName)
        setAllowedOverMetered(true)
        setAllowedOverRoaming(true)
    }
    downloadManager.enqueue(request)
}