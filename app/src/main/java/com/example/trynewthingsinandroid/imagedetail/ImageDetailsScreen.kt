package com.example.trynewthingsinandroid.imagedetail

import android.app.Activity
import android.app.WallpaperManager
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
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
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.WindowInsetsControllerCompat
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.AsyncImage
import com.example.trynewthingsinandroid.R
import com.example.trynewthingsinandroid.data.PhotoCredit
import com.example.trynewthingsinandroid.utils.downloadImage
import com.example.trynewthingsinandroid.utils.setAsWallpaper
import com.example.trynewthingsinandroid.utils.shareImage

/**
 * Screen displaying details for a specific image.
 *
 * @param onBackClick Callback invoked when the user clicks the back button.
 * @param viewModel The ViewModel providing the state for this screen.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ImageDetailsScreen(
    onBackClick: () -> Unit,
    viewModel: ImageDetailsViewModel = viewModel()
) {
    val uiState by viewModel.uiState.collectAsState()
    val context = LocalContext.current
    val view = LocalView.current
    val scaffoldState = rememberBottomSheetScaffoldState()

    // Synchronize BottomSheet state with UI Visibility
    LaunchedEffect(uiState.isImageFullScreen) {
        scaffoldState.bottomSheetState.partialExpand()
    }

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

                is ImageDetailsScreenUIEffect.SetAsWallpaper -> {
                    setAsWallpaper(context = context, url = effect.url)
                }

                is ImageDetailsScreenUIEffect.SetAsLockScreen -> {
                    setAsWallpaper(context = context, url = effect.url, which = WallpaperManager.FLAG_LOCK)
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
        sheetPeekHeight = if (uiState.isImageFullScreen) 220.dp else 0.dp,
        sheetContainerColor = Color.Transparent,
        sheetContentColor = Color.White,
        sheetDragHandle = null,
        sheetSwipeEnabled = uiState.isImageFullScreen,
        sheetContent = {
            OptionsBottomSheetUI(onEvent = viewModel::onUIEvent)
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
            BackgroundImageUI(url = uiState.selectedImage?.url)

            if (uiState.isImageFullScreen) {
                Column {
                    TopAppBar(
                        modifier = Modifier.clickable(
                            interactionSource = remember { MutableInteractionSource() },
                            indication = null
                        ) { /* Consume click to prevent toggle */ },
                        title = {
                            Text(
                                text = stringResource(R.string.image_detail_title),
                                color = Color.White
                            )
                        },
                        navigationIcon = {
                            IconButton(
                                onClick = onBackClick,
                                modifier = Modifier
                                    .padding(8.dp)
                                    .background(
                                        color = Color.Black.copy(alpha = 0.4f),
                                        shape = RoundedCornerShape(50)
                                    )
                            ) {
                                Icon(
                                    imageVector = Icons.Default.ArrowBack,
                                    contentDescription = stringResource(R.string.back_button_content_description),
                                    tint = Color.White
                                )
                            }
                        },
                        colors = TopAppBarDefaults.topAppBarColors(
                            containerColor = Color.Transparent
                        )
                    )

                    PhotoCreditUI(photoCredit = uiState.selectedImage?.photoCredit)
                }
            }
        }
    }
}

/**
 * Composable representing the main background image.
 *
 * @param url The URL of the image to display.
 */
@Composable
fun BackgroundImageUI(url: String?) {
    AsyncImage(
        model = url,
        contentDescription = stringResource(R.string.wallpaper_detail_content_description),
        modifier = Modifier.fillMaxSize(),
        contentScale = ContentScale.Crop
    )
}

/**
 * Composable representing the photographer's credit.
 *
 * @param photoCredit The photo credit information to display.
 */
@Composable
fun PhotoCreditUI(photoCredit: PhotoCredit?) {
    photoCredit?.name?.let { name ->
        Text(
            text = name,
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
            style = MaterialTheme.typography.titleMedium
        )
    }
}
