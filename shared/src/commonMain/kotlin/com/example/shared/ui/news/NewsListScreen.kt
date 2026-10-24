package com.example.shared.ui.news

import androidx.compose.animation.*
import androidx.compose.animation.core.*
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil3.compose.AsyncImage
import com.example.shared.domain.model.Article
import com.example.shared.presentation.NewsUiState
import com.example.shared.presentation.NewsListViewModel
import com.example.shared.ui.theme.ThemeManager

/**
 * Main Home feed screen displaying Samachar news articles in a futuristic dark cyan theme.
 * Features search filtering, category selection chips, state handling (loading, error, success),
 * and interactive article card feeds.
 *
 * @param viewModel The ViewModel providing news feed state and fetch operations.
 * @param onArticleClick Callback invoked when an article card is tapped, passing its unique articleId.
 */
@Composable
fun NewsListScreen(
    viewModel: NewsListViewModel = remember { NewsListViewModel() },
    onArticleClick: (String) -> Unit
) {
    val uiState by viewModel.uiState.collectAsState()

    // Interactive states
    var searchQuery by remember { mutableStateOf("") }
    var selectedCategory by remember { mutableStateOf("All") }
    val categories = listOf("All", "Quantum", "Neural AI", "Space", "Cybernetics", "Biotech")

    // Dynamic Theme Colors
    val currentTheme = ThemeManager.currentTheme
    val neonCyan = currentTheme.accentColor
    val neonViolet = currentTheme.accentColor.copy(alpha = 0.7f)
    val darkBackground = currentTheme.backgroundColor
    val surfaceGlass = currentTheme.cardColor
    val borderColor = currentTheme.accentColor.copy(alpha = 0.3f)
    val textColor = if (currentTheme.isLight) Color.Black else Color.White
    val subtitleColor = if (currentTheme.isLight) Color.DarkGray else Color.LightGray

    Scaffold(
        containerColor = darkBackground,
        contentWindowInsets = WindowInsets(0, 0, 0, 0),
        topBar = {
            // Futuristic Top App Bar & Search Header
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(darkBackground)
                    .statusBarsPadding()
                    .padding(horizontal = 16.dp, vertical = 12.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Surface(
                            shape = CircleShape,
                            color = neonCyan.copy(alpha = 0.2f),
                            border = BorderStroke(1.dp, neonCyan)
                        ) {
                            Box(modifier = Modifier.size(36.dp), contentAlignment = Alignment.Center) {
                                Icon(Icons.Default.Star, contentDescription = null, tint = neonCyan, modifier = Modifier.size(20.dp))
                            }
                        }
                        Text(
                            text = "SAMACHAR",
                            style = MaterialTheme.typography.titleLarge.copy(
                                fontWeight = FontWeight.Bold,
                                color = textColor,
                                letterSpacing = 1.5.sp
                            )
                        )
                    }

                    // Profile / Status Indicator
                    Surface(
                        shape = CircleShape,
                        color = surfaceGlass,
                        border = BorderStroke(1.dp, borderColor)
                    ) {
                        IconButton(onClick = { viewModel.fetchNews() }) {
                            Icon(
                                imageVector = Icons.Default.Refresh,
                                contentDescription = "Refresh Feed",
                                tint = neonCyan
                            )
                        }
                    }
                }

                // Interactive Search Bar
                OutlinedTextField(
                    value = searchQuery,
                    onValueChange = { searchQuery = it },
                    placeholder = { Text("Search Samachar articles...", color = subtitleColor) },
                    leadingIcon = { Icon(Icons.Default.Search, contentDescription = null, tint = neonCyan) },
                    trailingIcon = {
                        if (searchQuery.isNotEmpty()) {
                            IconButton(onClick = { searchQuery = "" }) {
                                Icon(Icons.Default.Clear, contentDescription = "Clear", tint = subtitleColor)
                            }
                        }
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(54.dp),
                    shape = RoundedCornerShape(27.dp),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedContainerColor = surfaceGlass,
                        unfocusedContainerColor = surfaceGlass,
                        focusedBorderColor = neonCyan,
                        unfocusedBorderColor = borderColor,
                        focusedTextColor = textColor,
                        unfocusedTextColor = textColor
                    ),
                    singleLine = true
                )

                // Category Filter Chips
                LazyRow(
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    contentPadding = PaddingValues(vertical = 4.dp)
                ) {
                    items(categories) { category ->
                        val isSelected = selectedCategory == category
                        Surface(
                            onClick = { selectedCategory = category },
                            shape = RoundedCornerShape(20.dp),
                            color = if (isSelected) neonCyan.copy(alpha = 0.2f) else surfaceGlass,
                            border = BorderStroke(
                                width = 1.dp,
                                color = if (isSelected) neonCyan else borderColor
                            )
                        ) {
                            Text(
                                text = category,
                                color = if (isSelected) neonCyan else subtitleColor,
                                style = MaterialTheme.typography.labelMedium,
                                fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
                                modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)
                            )
                        }
                    }
                }
            }
        }
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            when (val state = uiState) {
                is NewsUiState.Loading -> {
                    CircularProgressIndicator(
                        modifier = Modifier.align(Alignment.Center),
                        color = neonCyan
                    )
                }
                is NewsUiState.Error -> {
                    Column(
                        modifier = Modifier
                            .align(Alignment.Center)
                            .padding(24.dp),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Warning,
                            contentDescription = null,
                            tint = Color(0xFFFF5252),
                            modifier = Modifier.size(48.dp)
                        )
                        Text(
                            text = "Connection Failed: ${state.message}",
                            color = textColor,
                            style = MaterialTheme.typography.bodyLarge,
                            textAlign = androidx.compose.ui.text.style.TextAlign.Center
                        )
                        Button(
                            onClick = { viewModel.fetchNews() },
                            colors = ButtonDefaults.buttonColors(containerColor = neonCyan)
                        ) {
                            Text("Retry Connection", color = darkBackground, fontWeight = FontWeight.Bold)
                        }
                    }
                }
                is NewsUiState.Success -> {
                    // Filter articles based on search query and category
                    val filteredArticles = state.articles.filter { article ->
                        val matchesSearch = article.title.contains(searchQuery, ignoreCase = true) ||
                                article.description.contains(searchQuery, ignoreCase = true)
                        val matchesCategory = selectedCategory == "All" ||
                                article.title.contains(selectedCategory, ignoreCase = true) ||
                                article.description.contains(selectedCategory, ignoreCase = true)
                        matchesSearch && matchesCategory
                    }

                    if (filteredArticles.isEmpty()) {
                        Column(
                            modifier = Modifier
                                .align(Alignment.Center)
                                .padding(24.dp),
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            Icon(Icons.Default.Search, contentDescription = null, tint = subtitleColor, modifier = Modifier.size(48.dp))
                            Text("No Samachar feeds found", color = subtitleColor, style = MaterialTheme.typography.bodyLarge)
                        }
                    } else {
                        LazyColumn(
                            modifier = Modifier.fillMaxSize(),
                            contentPadding = PaddingValues(16.dp),
                            verticalArrangement = Arrangement.spacedBy(16.dp)
                        ) {
                            items(filteredArticles, key = { it.articleId }) { article ->
                                FuturisticArticleCard(
                                    article = article,
                                    onClick = { onArticleClick(article.articleId) },
                                    neonCyan = neonCyan,
                                    neonViolet = neonViolet,
                                    surfaceGlass = surfaceGlass,
                                    borderColor = borderColor,
                                    textColor = textColor,
                                    subtitleColor = subtitleColor
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun FuturisticArticleCard(
    article: Article,
    onClick: () -> Unit,
    neonCyan: Color,
    neonViolet: Color,
    surfaceGlass: Color,
    borderColor: Color,
    textColor: Color,
    subtitleColor: Color
) {
    var isBookmarked by remember { mutableStateOf(false) }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick),
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(containerColor = surfaceGlass),
        border = BorderStroke(1.dp, borderColor),
        elevation = CardDefaults.cardElevation(defaultElevation = 8.dp)
    ) {
        Column(modifier = Modifier.fillMaxWidth()) {
            if (!article.imageUrl.isNullOrBlank()) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(180.dp)
                ) {
                    AsyncImage(
                        model = article.imageUrl,
                        contentDescription = article.title,
                        modifier = Modifier
                            .fillMaxSize()
                            .clip(RoundedCornerShape(topStart = 20.dp, topEnd = 20.dp)),
                        contentScale = ContentScale.Crop
                    )

                    if (!article.category.isNullOrBlank()) {
                        Surface(
                            modifier = Modifier
                                .align(Alignment.TopStart)
                                .padding(12.dp),
                            shape = RoundedCornerShape(12.dp),
                            color = Color.Black.copy(alpha = 0.6f),
                            border = BorderStroke(1.dp, neonCyan.copy(alpha = 0.7f))
                        ) {
                            Text(
                                text = "⚡ ${article.category}",
                                color = neonCyan,
                                style = MaterialTheme.typography.labelSmall,
                                fontWeight = FontWeight.Bold,
                                modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                            )
                        }
                    }

                    // Interactive Bookmark Button overlay on image
                    Surface(
                        modifier = Modifier
                            .align(Alignment.TopEnd)
                            .padding(12.dp),
                        shape = CircleShape,
                        color = Color.Black.copy(alpha = 0.6f),
                        border = BorderStroke(1.dp, borderColor)
                    ) {
                        IconButton(
                            onClick = { isBookmarked = !isBookmarked },
                            modifier = Modifier.size(36.dp)
                        ) {
                            val scale by animateFloatAsState(
                                targetValue = if (isBookmarked) 1.3f else 1f,
                                animationSpec = spring(dampingRatio = Spring.DampingRatioMediumBouncy)
                            )
                            Icon(
                                imageVector = if (isBookmarked) Icons.Default.Favorite else Icons.Default.FavoriteBorder,
                                contentDescription = "Bookmark",
                                tint = if (isBookmarked) neonViolet else Color.White,
                                modifier = Modifier
                                    .size(18.dp)
                                    .scale(scale)
                            )
                        }
                    }
                }
            }

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Text(
                    text = article.title,
                    style = MaterialTheme.typography.titleMedium.copy(
                        fontWeight = FontWeight.Bold,
                        color = textColor,
                        lineHeight = 22.sp
                    ),
                    maxLines = 2
                )

                if (article.description.isNotBlank()) {
                    Text(
                        text = article.description,
                        style = MaterialTheme.typography.bodyMedium,
                        color = subtitleColor,
                        maxLines = 3
                    )
                }

                Spacer(modifier = Modifier.height(4.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = article.sourceName,
                        style = MaterialTheme.typography.labelMedium,
                        color = neonCyan,
                        fontWeight = FontWeight.SemiBold
                    )

                    Text(
                        text = article.readingTime,
                        style = MaterialTheme.typography.labelSmall,
                        color = subtitleColor
                    )
                }
            }
        }
    }
}
