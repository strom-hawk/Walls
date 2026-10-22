package com.example.trynewthingsinandroid.ui.news

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Bookmark
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.AsyncImage
import com.example.shared.domain.model.Article
import com.example.shared.ui.theme.ThemeManager
import com.example.trynewthingsinandroid.presentation.BookmarkedNewsViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BookmarkedNewsScreen(
    viewModel: BookmarkedNewsViewModel = viewModel(),
    onBackClick: () -> Unit,
    onArticleClick: (String) -> Unit
) {
    val bookmarkedArticles by viewModel.bookmarkedArticles.collectAsState()
    val currentTheme = ThemeManager.currentTheme
    val darkBackground = currentTheme.backgroundColor
    val neonCyan = currentTheme.accentColor
    val surfaceGlass = currentTheme.cardColor
    val borderColor = currentTheme.accentColor.copy(alpha = 0.3f)
    val textColor = if (currentTheme.isLight) Color.Black else Color.White
    val subtitleColor = if (currentTheme.isLight) Color.DarkGray else Color.LightGray

    var articleToDelete by remember { mutableStateOf<Article?>(null) }
    val sheetState = rememberModalBottomSheetState()

    Scaffold(
        containerColor = darkBackground,
        contentWindowInsets = WindowInsets(0, 0, 0, 0)
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .statusBarsPadding()
        ) {
            // Toolbar
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 12.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                Surface(
                    shape = CircleShape,
                    color = surfaceGlass,
                    border = BorderStroke(1.dp, borderColor)
                ) {
                    IconButton(onClick = onBackClick) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Back",
                            tint = textColor
                        )
                    }
                }

                Text(
                    text = "Bookmarked News",
                    style = MaterialTheme.typography.titleLarge.copy(
                        fontWeight = FontWeight.Bold,
                        color = textColor,
                        letterSpacing = 1.sp
                    )
                )
            }

            if (bookmarkedArticles.isEmpty()) {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .weight(1f),
                    contentAlignment = Alignment.Center
                ) {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Bookmark,
                            contentDescription = null,
                            tint = subtitleColor,
                            modifier = Modifier.size(56.dp)
                        )
                        Text(
                            text = "No bookmarked news found",
                            style = MaterialTheme.typography.bodyLarge,
                            color = subtitleColor
                        )
                    }
                }
            } else {
                LazyColumn(
                    modifier = Modifier
                        .fillMaxSize()
                        .weight(1f),
                    contentPadding = PaddingValues(16.dp),
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    items(bookmarkedArticles, key = { it.articleId }) { article ->
                        BookmarkedArticleCard(
                            article = article,
                            surfaceGlass = surfaceGlass,
                            borderColor = borderColor,
                            neonCyan = neonCyan,
                            textColor = textColor,
                            subtitleColor = subtitleColor,
                            onClick = { onArticleClick(article.articleId) },
                            onDeleteClick = { articleToDelete = article }
                        )
                    }
                }
            }
        }

        // Confirmation BottomSheet for Deletion
        if (articleToDelete != null) {
            ModalBottomSheet(
                onDismissRequest = { articleToDelete = null },
                sheetState = sheetState,
                containerColor = surfaceGlass,
                contentColor = textColor
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 24.dp, vertical = 16.dp),
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    Text(
                        text = "Remove Bookmark?",
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.Bold,
                        color = textColor
                    )
                    Text(
                        text = "Are you sure you want to remove \"${articleToDelete?.title}\" from your bookmarked news?",
                        style = MaterialTheme.typography.bodyMedium,
                        color = subtitleColor
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(16.dp)
                    ) {
                        Button(
                            onClick = { articleToDelete = null },
                            modifier = Modifier
                                .weight(1f)
                                .height(48.dp),
                            colors = ButtonDefaults.buttonColors(containerColor = Color.Gray.copy(alpha = 0.3f)),
                            shape = RoundedCornerShape(24.dp)
                        ) {
                            Text("Cancel", color = textColor, fontWeight = FontWeight.Bold)
                        }
                        Button(
                            onClick = {
                                articleToDelete?.let { article ->
                                    viewModel.removeBookmark(article.articleId)
                                }
                                articleToDelete = null
                            },
                            modifier = Modifier
                                .weight(1f)
                                .height(48.dp),
                            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFFF5252)),
                            shape = RoundedCornerShape(24.dp)
                        ) {
                            Text("Remove", color = Color.White, fontWeight = FontWeight.Bold)
                        }
                    }
                    Spacer(modifier = Modifier.height(32.dp))
                }
            }
        }
    }
}

@Composable
fun BookmarkedArticleCard(
    article: Article,
    surfaceGlass: Color,
    borderColor: Color,
    neonCyan: Color,
    textColor: Color,
    subtitleColor: Color,
    onClick: () -> Unit,
    onDeleteClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick),
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(containerColor = surfaceGlass),
        border = BorderStroke(1.dp, borderColor),
        elevation = CardDefaults.cardElevation(defaultElevation = 6.dp)
    ) {
        Column(modifier = Modifier.fillMaxWidth()) {
            if (!article.imageUrl.isNullOrBlank()) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(160.dp)
                ) {
                    AsyncImage(
                        model = article.imageUrl,
                        contentDescription = article.title,
                        modifier = Modifier
                            .fillMaxSize()
                            .clip(RoundedCornerShape(topStart = 20.dp, topEnd = 20.dp)),
                        contentScale = ContentScale.Crop
                    )

                    Surface(
                        modifier = Modifier
                            .align(Alignment.TopEnd)
                            .padding(12.dp),
                        shape = CircleShape,
                        color = Color.Black.copy(alpha = 0.6f),
                        border = BorderStroke(1.dp, borderColor)
                    ) {
                        IconButton(
                            onClick = onDeleteClick,
                            modifier = Modifier.size(36.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Default.Delete,
                                contentDescription = "Remove Bookmark",
                                tint = Color(0xFFFF5252),
                                modifier = Modifier.size(18.dp)
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
                        maxLines = 2
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
