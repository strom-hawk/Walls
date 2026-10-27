package com.example.shared.ui.news

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Bookmark
import androidx.compose.material.icons.filled.BookmarkBorder
import androidx.compose.material.icons.filled.Pause
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Share
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.VerticalDivider
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil3.compose.AsyncImage
import com.example.shared.domain.model.Article
import com.example.shared.presentation.NewsDetailUiState
import com.example.shared.presentation.NewsDetailUIEvent
import com.example.shared.ui.theme.ThemeManager
import com.example.shared.util.TextToSpeechPlayer
import com.example.shared.util.rememberTextToSpeechPlayer
import kotlinx.coroutines.delay

/**
 * Detailed view screen for a selected news article using MVI architecture.
 *
 * @param uiState State containing article details, bookmark status, and loading state.
 * @param onEvent Callback dispatching user UI events.
 */
@Composable
fun NewsDetailScreen(
    uiState: NewsDetailUiState,
    onEvent: (NewsDetailUIEvent) -> Unit
) {
    val currentTheme = ThemeManager.currentTheme
    val darkBackground = currentTheme.backgroundColor
    val neonCyan = currentTheme.accentColor
    val neonViolet = currentTheme.accentColor.copy(alpha = 0.7f)
    val surfaceGlass = currentTheme.cardColor
    val borderColor = currentTheme.accentColor.copy(alpha = 0.3f)
    val textColor = if (currentTheme.isLight) Color.Black else Color.White
    val subtitleColor = if (currentTheme.isLight) Color.DarkGray else Color.LightGray

    if (uiState.isLoading) {
        Box(modifier = Modifier.fillMaxSize().background(darkBackground), contentAlignment = Alignment.Center) {
            CircularProgressIndicator(color = neonCyan)
        }
        return
    }

    val currentArticle = uiState.article
    if (currentArticle == null) {
        ArticleNotFoundScreen(
            title = "Article Not Found",
            description = "This article is no longer cached in memory. Please return to the news feed to explore the latest Samachar feeds.",
            ctaText = "⚡ Return to News Feed",
            onCtaClick = { onEvent(NewsDetailUIEvent.OnBackClicked) },
            onBackClick = { onEvent(NewsDetailUIEvent.OnBackClicked) }
        )
        return
    }

    val isBookmarked = uiState.isBookmarked

    // State management
    var textScale by remember { mutableStateOf(1f) }
    var isAiVoicePlaying by remember { mutableStateOf(false) }
    var playbackProgress by remember { mutableStateOf(0f) }

    val ttsPlayer = rememberTextToSpeechPlayer(
        onDone = {
            isAiVoicePlaying = false
            playbackProgress = 0f
        }
    )

    // Extracted TTS audio playback handling function
    HandleTextToSpeechPlayback(
        isAiVoicePlaying = isAiVoicePlaying,
        title = currentArticle.title,
        description = currentArticle.description,
        ttsPlayer = ttsPlayer,
        onProgressUpdate = { playbackProgress = it }
    )

    Scaffold(
        containerColor = darkBackground
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .verticalScroll(rememberScrollState())
            ) {
                // Hero Header Section
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(380.dp)
                ) {
                    DetailHeroSection(
                        imageUrl = currentArticle.imageUrl,
                        title = currentArticle.title,
                        neonViolet = neonViolet,
                        darkBackground = darkBackground
                    )
                    DetailTopBar(
                        category = currentArticle.category,
                        surfaceGlass = surfaceGlass,
                        borderColor = borderColor,
                        neonCyan = neonCyan,
                        textColor = textColor,
                        onBackClick = { onEvent(NewsDetailUIEvent.OnBackClicked) },
                        onTextScaleDecrease = { if (textScale > 0.8f) textScale -= 0.1f },
                        onTextScaleIncrease = { if (textScale < 1.4f) textScale += 0.1f }
                    )
                }

                // Scrollable Content Container
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .offset(y = (-32.dp))
                        .clip(RoundedCornerShape(topStart = 32.dp, topEnd = 32.dp))
                        .background(darkBackground)
                        .padding(horizontal = 24.dp),
                    verticalArrangement = Arrangement.spacedBy(20.dp)
                ) {
                    Spacer(modifier = Modifier.height(8.dp))

                    DetailHeaderContent(
                        article = currentArticle,
                        textScale = textScale,
                        neonCyan = neonCyan,
                        neonViolet = neonViolet,
                        textColor = textColor,
                        subtitleColor = subtitleColor
                    )

                    // Unified Action Control Center (Listen as Primary CTA)
                    ActionControlCenter(
                        articleUrl = currentArticle.link,
                        articleTitle = currentArticle.title,
                        isBookmarked = isBookmarked,
                        isAiVoicePlaying = isAiVoicePlaying,
                        playbackProgress = playbackProgress,
                        surfaceGlass = surfaceGlass,
                        borderColor = borderColor,
                        neonCyan = neonCyan,
                        textColor = textColor,
                        onReadFullNewsClick = { url -> onEvent(NewsDetailUIEvent.OnReadFullNewsClicked(url)) },
                        onListenClick = {
                            isAiVoicePlaying = !isAiVoicePlaying
                            if (!isAiVoicePlaying) {
                                playbackProgress = 0f
                                ttsPlayer.stop()
                            }
                        },
                        onBookmarkClick = { onEvent(NewsDetailUIEvent.OnBookmarkClicked) },
                        onShareClick = { title, url -> onEvent(NewsDetailUIEvent.OnShareClicked(title, url)) }
                    )

                    ArticleContentBody(
                        article = currentArticle,
                        textScale = textScale,
                        borderColor = borderColor,
                        textColor = textColor
                    )

                    Spacer(modifier = Modifier.height(16.dp))
                }
            }
        }
    }
}

/**
 * Handles text-to-speech audio playback simulation in a separate side-effect function.
 */
@Composable
fun HandleTextToSpeechPlayback(
    isAiVoicePlaying: Boolean,
    title: String,
    description: String,
    ttsPlayer: TextToSpeechPlayer,
    onProgressUpdate: (Float) -> Unit
) {
    LaunchedEffect(isAiVoicePlaying) {
        if (isAiVoicePlaying) {
            val fullText = buildString {
                append("$title. ")
                if (description.isNotBlank()) {
                    append(description)
                }
            }
            ttsPlayer.speak(fullText)
            onProgressUpdate(0f)

            val wordCount = fullText.split("\\s+".toRegex()).size
            val estimatedDurationMs = (wordCount * 300L).coerceIn(4000L, 60000L)
            val stepTime = estimatedDurationMs / 50
            var progress = 0f

            while (isAiVoicePlaying && progress < 1f) {
                delay(stepTime)
                progress = (progress + 0.02f).coerceAtMost(1f)
                onProgressUpdate(progress)
            }
        } else {
            ttsPlayer.stop()
            onProgressUpdate(0f)
        }
    }
}

// --- Modular Components ---

@Composable
fun DetailTopBar(
    category: String?,
    surfaceGlass: Color,
    borderColor: Color,
    neonCyan: Color,
    textColor: Color,
    onBackClick: () -> Unit,
    onTextScaleDecrease: () -> Unit,
    onTextScaleIncrease: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .statusBarsPadding()
            .padding(horizontal = 16.dp, vertical = 12.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
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

        Row(
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            if (!category.isNullOrBlank()) {
                Surface(
                    shape = RoundedCornerShape(16.dp),
                    color = surfaceGlass,
                    border = BorderStroke(1.dp, neonCyan.copy(alpha = 0.6f))
                ) {
                    Text(
                        text = "⚡ $category",
                        color = neonCyan,
                        style = MaterialTheme.typography.labelSmall,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp)
                    )
                }
            }

            Surface(
                shape = RoundedCornerShape(16.dp),
                color = surfaceGlass,
                border = BorderStroke(1.dp, borderColor)
            ) {
                Row(
                    modifier = Modifier.padding(horizontal = 4.dp, vertical = 2.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    IconButton(onClick = onTextScaleDecrease, modifier = Modifier.size(32.dp)) {
                        Text("A-", color = textColor, fontSize = 10.sp, fontWeight = FontWeight.Bold)
                    }
                    VerticalDivider(modifier = Modifier.height(16.dp), color = borderColor)
                    IconButton(onClick = onTextScaleIncrease, modifier = Modifier.size(32.dp)) {
                        Text("A+", color = neonCyan, fontSize = 10.sp, fontWeight = FontWeight.Bold)
                    }
                }
            }
        }
    }
}

@Composable
fun DetailHeroSection(imageUrl: String?, title: String, neonViolet: Color, darkBackground: Color) {
    Box(modifier = Modifier.fillMaxSize()) {
        if (!imageUrl.isNullOrBlank()) {
            AsyncImage(
                model = imageUrl,
                contentDescription = title,
                modifier = Modifier
                    .fillMaxSize()
                    .drawBehind {
                        drawCircle(
                            brush = Brush.radialGradient(
                                colors = listOf(neonViolet.copy(alpha = 0.3f), Color.Transparent),
                                center = Offset(size.width / 2, 0f),
                                radius = size.width
                            ),
                            radius = size.width
                        )
                    },
                contentScale = ContentScale.Crop
            )
        }

        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    Brush.verticalGradient(
                        colors = listOf(
                            Color.Black.copy(alpha = 0.3f),
                            Color.Transparent,
                            darkBackground
                        ),
                        startY = 0f,
                        endY = Float.POSITIVE_INFINITY
                    )
                )
        )
    }
}

@Composable
fun DetailHeaderContent(
    article: Article,
    textScale: Float,
    neonCyan: Color,
    neonViolet: Color,
    textColor: Color,
    subtitleColor: Color
) {
    Column(
        modifier = Modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Text(
            text = article.title,
            style = MaterialTheme.typography.headlineMedium.copy(
                fontSize = (23.5 * textScale).sp,
                fontWeight = FontWeight.Bold,
                color = textColor,
                lineHeight = (30 * textScale).sp
            )
        )

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row(
                horizontalArrangement = Arrangement.spacedBy(12.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Surface(
                    shape = CircleShape,
                    modifier = Modifier.size(42.dp),
                    color = neonViolet.copy(alpha = 0.3f),
                    border = BorderStroke(1.dp, neonViolet)
                ) {
                    Box(contentAlignment = Alignment.Center) {
                        Icon(Icons.Default.Person, contentDescription = null, tint = neonCyan, modifier = Modifier.size(24.dp))
                    }
                }
                Column(
                    verticalArrangement = Arrangement.spacedBy(2.dp)
                ) {
                    if (!article.creator.isNullOrBlank()) {
                        Text(
                            text = article.creator,
                            style = MaterialTheme.typography.bodyMedium,
                            fontWeight = FontWeight.SemiBold,
                            color = textColor
                        )
                    }
                    Text(
                        text = article.sourceName,
                        style = MaterialTheme.typography.bodySmall,
                        fontWeight = FontWeight.Medium,
                        color = neonCyan
                    )
                    val dateAndReadingTime = buildString {
                        if (!article.pubDate.isNullOrBlank()) {
                            append("${article.pubDate} • ")
                        }
                        append(article.readingTime)
                    }
                    Text(
                        text = dateAndReadingTime,
                        style = MaterialTheme.typography.bodySmall,
                        color = subtitleColor
                    )
                }
            }
        }
    }
}

@Composable
fun ActionControlCenter(
    articleUrl: String,
    articleTitle: String,
    isBookmarked: Boolean,
    isAiVoicePlaying: Boolean,
    playbackProgress: Float,
    surfaceGlass: Color,
    borderColor: Color,
    neonCyan: Color,
    textColor: Color,
    onReadFullNewsClick: (String) -> Unit,
    onListenClick: () -> Unit,
    onBookmarkClick: () -> Unit,
    onShareClick: (String, String) -> Unit
) {
    val progressPercentage = (playbackProgress * 100).toInt().coerceIn(0, 100)

    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(containerColor = surfaceGlass),
        border = BorderStroke(1.dp, borderColor),
        elevation = CardDefaults.cardElevation(defaultElevation = 8.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(14.dp)
        ) {
            // Primary Action CTA: Listen (TTS Audio Player)
            Button(
                onClick = onListenClick,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(48.dp),
                shape = RoundedCornerShape(24.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = if (isAiVoicePlaying) neonCyan.copy(alpha = 0.85f) else neonCyan
                ),
                elevation = ButtonDefaults.buttonElevation(defaultElevation = 6.dp)
            ) {
                Row(
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = if (isAiVoicePlaying) Icons.Default.Pause else Icons.Default.PlayArrow,
                        contentDescription = "Listen AI Voice",
                        tint = Color.Black,
                        modifier = Modifier.size(20.dp)
                    )
                    Text(
                        text = if (isAiVoicePlaying) "Playing $progressPercentage%" else "Listen AI Voice",
                        color = Color.Black,
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold
                    )
                }
            }

            HorizontalDivider(
                thickness = 1.dp,
                color = borderColor.copy(alpha = 0.5f)
            )

            // Secondary Quick Actions Row: Read Full News, Bookmark, Share
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly,
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Read Full News Action
                if (articleUrl.isNotBlank()) {
                    Surface(
                        onClick = { onReadFullNewsClick(articleUrl) },
                        shape = RoundedCornerShape(16.dp),
                        color = Color.Transparent,
                        border = BorderStroke(1.dp, borderColor)
                    ) {
                        Row(
                            modifier = Modifier.padding(horizontal = 12.dp, vertical = 8.dp),
                            horizontalArrangement = Arrangement.spacedBy(6.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(
                                imageVector = Icons.Default.PlayArrow,
                                contentDescription = null,
                                tint = neonCyan,
                                modifier = Modifier.size(18.dp)
                            )
                            Text(
                                text = "Read Full News",
                                style = MaterialTheme.typography.labelMedium,
                                color = textColor,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }
                }

                // Bookmark Action
                Surface(
                    onClick = onBookmarkClick,
                    shape = RoundedCornerShape(16.dp),
                    color = if (isBookmarked) neonCyan.copy(alpha = 0.2f) else Color.Transparent,
                    border = BorderStroke(1.dp, if (isBookmarked) neonCyan else borderColor)
                ) {
                    Row(
                        modifier = Modifier.padding(horizontal = 12.dp, vertical = 8.dp),
                        horizontalArrangement = Arrangement.spacedBy(6.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            imageVector = if (isBookmarked) Icons.Default.Bookmark else Icons.Default.BookmarkBorder,
                            contentDescription = "Bookmark",
                            tint = if (isBookmarked) neonCyan else textColor,
                            modifier = Modifier.size(18.dp)
                        )
                        Text(
                            text = if (isBookmarked) "Bookmarked" else "Bookmark",
                            style = MaterialTheme.typography.labelMedium,
                            color = if (isBookmarked) neonCyan else textColor,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }

                // Share Action
                Surface(
                    onClick = { onShareClick(articleTitle, articleUrl) },
                    shape = RoundedCornerShape(16.dp),
                    color = Color.Transparent,
                    border = BorderStroke(1.dp, borderColor)
                ) {
                    Row(
                        modifier = Modifier.padding(horizontal = 12.dp, vertical = 8.dp),
                        horizontalArrangement = Arrangement.spacedBy(6.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            imageVector = Icons.Default.Share,
                            contentDescription = "Share",
                            tint = textColor,
                            modifier = Modifier.size(18.dp)
                        )
                        Text(
                            text = "Share",
                            style = MaterialTheme.typography.labelMedium,
                            color = textColor,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun ArticleContentBody(
    article: Article,
    textScale: Float,
    borderColor: Color,
    textColor: Color
) {
    Column(
        modifier = Modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        if (article.description.isNotBlank()) {
            Text(
                text = article.description,
                style = MaterialTheme.typography.bodyLarge.copy(
                    fontSize = (14.5 * textScale).sp,
                    lineHeight = (23 * textScale).sp,
                    color = textColor
                )
            )
        }

        if (!article.imageUrl.isNullOrBlank()) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                AsyncImage(
                    model = article.imageUrl,
                    contentDescription = "Article Illustration",
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(220.dp)
                        .clip(RoundedCornerShape(16.dp))
                        .border(1.dp, borderColor, RoundedCornerShape(16.dp)),
                    contentScale = ContentScale.Crop
                )
            }
        }
    }
}
