package com.example.trynewthingsinandroid.home

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.FilterList
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil.compose.SubcomposeAsyncImage
import coil.request.ImageRequest
import com.example.trynewthingsinandroid.R
import com.example.trynewthingsinandroid.data.ImageUIState
import com.example.trynewthingsinandroid.ui.theme.TryNewThingsInAndroidTheme

/**
 * The main screen of the application displaying a grid of wallpapers.
 *
 * @param modifier The modifier to be applied to the layout.
 * @param viewModel The ViewModel providing the state for this screen.
 * @param onImageClick Callback invoked when an image is clicked.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    modifier: Modifier = Modifier,
    viewModel: HomeScreenViewModel = HomeScreenViewModel(),
    onImageClick: (String) -> Unit = {}
) {
    val uiState by viewModel.uiState.collectAsState()

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = stringResource(id = R.string.toolbar_title),
                        fontWeight = FontWeight.Bold
                    )
                },
                navigationIcon = {
                    CategoryDropdown(
                        categories = uiState.categories,
                        selectedCategory = uiState.selectedCategory,
                        onCategorySelected = { category ->
                            viewModel.onUIEvent(HomeScreenUIEvent.SelectCategory(category))
                        }
                    )
                }
            )
        }
    ) { paddingValues ->
        Column(
            modifier = modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(8.dp)
        ) {
            LazyVerticalGrid(
                columns = GridCells.Fixed(3),
                contentPadding = PaddingValues(4.dp),
                horizontalArrangement = Arrangement.spacedBy(4.dp),
                verticalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                items(uiState.images) { image ->
                    ImageUI(
                        image = image,
                        onClick = { onImageClick(image.id.orEmpty()) },
                        onLoadError = { viewModel.onUIEvent(HomeScreenUIEvent.RemoveImage(image.id.orEmpty())) }
                    )
                }
            }
        }
    }
}

/**
 * A single image card item in the gallery.
 *
 * @param image The image state to display.
 * @param onClick Callback invoked when the card is clicked.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun ImageUI(
    image: ImageUIState,
    onClick: () -> Unit = {},
    onLoadError: () -> Unit = {}
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .aspectRatio(9f / 16f),
        shape = RectangleShape,
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceVariant
        ),
        onClick = onClick
    ) {
        SubcomposeAsyncImage(
            model = ImageRequest.Builder(LocalContext.current)
                .data(image.url)
                .crossfade(true)
                .build(),
            contentDescription = stringResource(R.string.wallpaper_image_content_description),
            modifier = Modifier.fillMaxSize(),
            contentScale = ContentScale.Crop,
            loading = { ImageLoadingIndicatorUI() },
            onError = { onLoadError() }
        )
    }
}

/**
 * Centered loading indicator for images.
 */
@Composable
private fun ImageLoadingIndicatorUI() {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        CircularProgressIndicator(
            modifier = Modifier.size(24.dp),
            strokeWidth = 2.dp,
            color = MaterialTheme.colorScheme.primary
        )
    }
}

@Composable
private fun CategoryDropdown(
    categories: List<String>,
    selectedCategory: String,
    onCategorySelected: (String) -> Unit
) {
    var expanded by remember { mutableStateOf(false) }

    Box {
        IconButton(onClick = { expanded = true }) {
            Icon(
                imageVector = Icons.Default.FilterList,
                contentDescription = "Filter by Category"
            )
        }

        DropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false }
        ) {
            categories.forEach { category ->
                val displayName = if (category == "All") {
                    stringResource(id = R.string.category_all)
                } else {
                    category
                }
                DropdownMenuItem(
                    text = {
                        Text(
                            text = displayName,
                            fontWeight = if (category == selectedCategory) FontWeight.Bold else FontWeight.Normal
                        )
                    },
                    onClick = {
                        onCategorySelected(category)
                        expanded = false
                    }
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun HomeScreenPreview() {
    TryNewThingsInAndroidTheme {
        HomeScreen()
    }
}
