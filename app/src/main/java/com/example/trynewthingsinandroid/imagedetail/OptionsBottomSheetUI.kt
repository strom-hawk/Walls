package com.example.trynewthingsinandroid.imagedetail

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Download
import androidx.compose.material.icons.filled.ScreenLockPortrait
import androidx.compose.material.icons.filled.Share
import androidx.compose.material.icons.filled.Wallpaper
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.example.trynewthingsinandroid.R

/**
 * Composable representing the options bottom sheet UI with actions like share, download, and set as wallpaper/lock screen.
 *
 * @param onEvent Callback invoked for UI events.
 * @param modifier The modifier to be applied to the layout.
 */
@Composable
fun OptionsBottomSheetUI(
    onEvent: (ImageDetailsScreenUIEvent) -> Unit,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .background(Color.Black.copy(alpha = 0.4f))
            .clickable(
                interactionSource = remember { MutableInteractionSource() },
                indication = null
            ) { /* Consume click to prevent toggle */ }
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 100.dp, top = 24.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Row(
                horizontalArrangement = Arrangement.spacedBy(32.dp, Alignment.CenterHorizontally),
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(onClick = { onEvent(ImageDetailsScreenUIEvent.OnShareClicked) }) {
                    Icon(
                        imageVector = Icons.Default.Share,
                        contentDescription = stringResource(R.string.share_button_content_description),
                        tint = Color.White
                    )
                }
                IconButton(onClick = { onEvent(ImageDetailsScreenUIEvent.OnDownloadClicked) }) {
                    Icon(
                        imageVector = Icons.Default.Download,
                        contentDescription = stringResource(R.string.download_button_content_description),
                        tint = Color.White
                    )
                }
            }

            // Set as Wallpaper Button
            Button(
                onClick = { onEvent(ImageDetailsScreenUIEvent.OnSetAsWallpaperClicked) },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 32.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.Wallpaper,
                    contentDescription = null,
                    modifier = Modifier.padding(end = 8.dp)
                )
                Text(text = stringResource(R.string.set_as_wallpaper))
            }

            // Set as Lock Screen Button
            Button(
                onClick = { onEvent(ImageDetailsScreenUIEvent.OnSetAsLockScreenClicked) },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 32.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.ScreenLockPortrait,
                    contentDescription = null,
                    modifier = Modifier.padding(end = 8.dp)
                )
                Text(text = stringResource(R.string.set_as_lock_screen))
            }
        }
    }
}
