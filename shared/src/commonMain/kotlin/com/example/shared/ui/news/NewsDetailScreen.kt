package com.example.shared.ui.news

import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
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
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Share
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Badge
import androidx.compose.material3.BadgedBox
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
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
import androidx.compose.ui.draw.scale
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil3.compose.AsyncImage
import com.example.shared.domain.model.Article
import com.example.shared.ui.theme.*
import com.example.shared.util.rememberTextToSpeechPlayer
import kotlinx.coroutines.delay
import kotlin.time.Duration.Companion.seconds

@Composable
fun NewsDetailScreen(
    article: Article,
    onBackClick: () -> Unit
) {
    // State management
    var textScale by remember { mutableStateOf(1f) }
    var isBookmarked by remember { mutableStateOf(false) }
    var isAiVoicePlaying by remember { mutableStateOf(false) }
    var playbackProgress by remember { mutableStateOf(0f) }
    var showAiSummaryDialog by remember { mutableStateOf(false) }
    var commentCount by remember { mutableStateOf(42) }
    var isShared by remember { mutableStateOf(false) }

    val ttsPlayer = rememberTextToSpeechPlayer(
        onDone = {
            isAiVoicePlaying = false
            playbackProgress = 0f
        }
    )

    // Handle AI voice playback simulation & TTS speech
    LaunchedEffect(isAiVoicePlaying) {
        if (isAiVoicePlaying) {
            val fullText = buildString {
                append("${article.title}. ")
                if (article.description.isNotBlank()) {
                    append(article.description)
                }
            }
            ttsPlayer.speak(fullText)
            playbackProgress = 0f

            val wordCount = fullText.split("\\s+".toRegex()).size
            val estimatedDurationMs = (wordCount * 300L).coerceIn(4000L, 60000L)
            val stepTime = estimatedDurationMs / 50

            while (isAiVoicePlaying && playbackProgress < 1f) {
                delay(stepTime)
                playbackProgress += 0.02f
            }
        } else {
            ttsPlayer.stop()
            playbackProgress = 0f
        }
    }

    Scaffold(
        containerColor = DarkBackground,
        bottomBar = {
            FloatingBottomDeck(
                isBookmarked = isBookmarked,
                commentCount = commentCount,
                onAiSummaryClick = { showAiSummaryDialog = true },
                onBookmarkClick = { isBookmarked = !isBookmarked },
                onShareClick = { isShared = true },
                onCommentClick = { commentCount++ }
            )
        }
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
                    DetailHeroSection(imageUrl = article.imageUrl, title = article.title)
                    DetailTopBar(
                        onBackClick = onBackClick,
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
                        .background(DarkBackground)
                        .padding(horizontal = 24.dp),
                    verticalArrangement = Arrangement.spacedBy(20.dp)
                ) {
                    Spacer(modifier = Modifier.height(8.dp))

                    DetailHeaderContent(
                        title = article.title,
                        textScale = textScale,
                        isAiVoicePlaying = isAiVoicePlaying,
                        playbackProgress = playbackProgress,
                        onListenClick = {
                            isAiVoicePlaying = !isAiVoicePlaying
                            if (!isAiVoicePlaying) {
                                playbackProgress = 0f
                                ttsPlayer.stop()
                            }
                        }
                    )

                    AiKeyTakeawaysCard(textScale = textScale)

                    ArticleContentBody(
                        article = article,
                        textScale = textScale
                    )

                    Spacer(modifier = Modifier.height(100.dp))
                }
            }
        }
    }

    if (showAiSummaryDialog) {
        AiSummaryDialog(onDismiss = { showAiSummaryDialog = false })
    }

    if (isShared) {
        LaunchedEffect(isShared) {
            delay(2.seconds)
            isShared = false
        }
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(bottom = 100.dp),
            contentAlignment = Alignment.Center
        ) {
            Surface(
                shape = RoundedCornerShape(16.dp),
                color = NeonViolet,
                shadowElevation = 8.dp
            ) {
                Text(
                    text = "🔗 Article link copied to clipboard!",
                    color = Color.White,
                    modifier = Modifier.padding(horizontal = 16.dp, vertical = 10.dp),
                    fontWeight = FontWeight.Bold
                )
            }
        }
    }
}

// --- Modular Components ---

@Composable
fun DetailTopBar(
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
            color = SurfaceGlass,
            border = BorderStroke(1.dp, BorderColor)
        ) {
            IconButton(onClick = onBackClick) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                    contentDescription = "Back",
                    tint = Color.White
                )
            }
        }

        Row(
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Surface(
                shape = RoundedCornerShape(16.dp),
                color = SurfaceGlass,
                border = BorderStroke(1.dp, NeonCyan.copy(alpha = 0.6f))
            ) {
                Text(
                    text = "⚡ QUANTUM TECH",
                    color = NeonCyan,
                    style = MaterialTheme.typography.labelSmall,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp)
                )
            }

            Surface(
                shape = RoundedCornerShape(16.dp),
                color = SurfaceGlass,
                border = BorderStroke(1.dp, BorderColor)
            ) {
                Row(
                    modifier = Modifier.padding(horizontal = 4.dp, vertical = 2.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    IconButton(onClick = onTextScaleDecrease, modifier = Modifier.size(32.dp)) {
                        Text("A-", color = Color.White, fontSize = 11.sp, fontWeight = FontWeight.Bold)
                    }
                    VerticalDivider(modifier = Modifier.height(16.dp), color = BorderColor)
                    IconButton(onClick = onTextScaleIncrease, modifier = Modifier.size(32.dp)) {
                        Text("A+", color = NeonCyan, fontSize = 11.sp, fontWeight = FontWeight.Bold)
                    }
                }
            }
        }
    }
}

@Composable
fun DetailHeroSection(imageUrl: String?, title: String) {
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
                                colors = listOf(NeonViolet.copy(alpha = 0.3f), Color.Transparent),
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
                            DarkBackground
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
    title: String,
    textScale: Float,
    isAiVoicePlaying: Boolean,
    playbackProgress: Float,
    onListenClick: () -> Unit
) {
    Column(
        modifier = Modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Text(
            text = title,
            style = MaterialTheme.typography.headlineMedium.copy(
                fontSize = (26 * textScale).sp,
                fontWeight = FontWeight.Bold,
                color = Color.White,
                lineHeight = (34 * textScale).sp
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
                    color = NeonViolet.copy(alpha = 0.3f),
                    border = BorderStroke(1.dp, NeonViolet)
                ) {
                    Box(contentAlignment = Alignment.Center) {
                        Icon(Icons.Default.Person, contentDescription = null, tint = NeonCyan, modifier = Modifier.size(24.dp))
                    }
                }
                Column {
                    Text(
                        text = "Dr. Elian Vance",
                        style = MaterialTheme.typography.bodyMedium,
                        fontWeight = FontWeight.SemiBold,
                        color = Color.White
                    )
                    Text(
                        text = "Oct 15, 2026 • 4 min read",
                        style = MaterialTheme.typography.bodySmall,
                        color = Color.Gray
                    )
                }
            }

            Surface(
                onClick = onListenClick,
                shape = CircleShape,
                color = if (isAiVoicePlaying) NeonCyan.copy(alpha = 0.2f) else SurfaceGlass,
                border = BorderStroke(1.5.dp, if (isAiVoicePlaying) NeonCyan else NeonViolet),
                shadowElevation = 6.dp
            ) {
                Row(
                    modifier = Modifier.padding(horizontal = 14.dp, vertical = 8.dp),
                    horizontalArrangement = Arrangement.spacedBy(6.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = Icons.Default.PlayArrow,
                        contentDescription = "Listen AI Voice",
                        tint = if (isAiVoicePlaying) NeonCyan else Color.White,
                        modifier = Modifier.size(16.dp)
                    )
                    Text(
                        text = if (isAiVoicePlaying) "Playing ${(playbackProgress * 100).toInt()}%" else "Listen",
                        style = MaterialTheme.typography.labelMedium,
                        color = if (isAiVoicePlaying) NeonCyan else Color.White,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        }
    }
}

@Composable
fun AiKeyTakeawaysCard(textScale: Float) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(containerColor = SurfaceGlass),
        border = BorderStroke(1.dp, Brush.linearGradient(listOf(NeonCyan, NeonViolet)))
    ) {
        Column(
            modifier = Modifier.padding(20.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Row(
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    imageVector = Icons.Default.Star,
                    contentDescription = null,
                    tint = NeonCyan,
                    modifier = Modifier.size(20.dp)
                )
                Text(
                    text = "⚡ AI Key Takeaways",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    color = NeonCyan
                )
            }

            val summaryPoints = listOf(
                "Breakthrough neural-core architecture increases multi-agent processing efficiency by 340%.",
                "Quantum encryption layer successfully tested against zero-day quantum simulation attacks.",
                "Commercial deployment slated for Q1 2027 across decentralized cloud grids."
            )

            summaryPoints.forEach { point ->
                Row(
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    verticalAlignment = Alignment.Top
                ) {
                    Box(
                        modifier = Modifier
                            .padding(top = 6.dp)
                            .size(6.dp)
                            .background(NeonViolet, CircleShape)
                    )
                    Text(
                        text = point,
                        style = MaterialTheme.typography.bodyMedium.copy(fontSize = (14 * textScale).sp),
                        color = Color.LightGray,
                        lineHeight = (20 * textScale).sp
                    )
                }
            }
        }
    }
}

@Composable
fun ArticleContentBody(article: Article, textScale: Float) {
    Column(
        modifier = Modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        if (article.description.isNotBlank()) {
            Text(
                text = article.description,
                style = MaterialTheme.typography.bodyLarge.copy(
                    fontSize = (16 * textScale).sp,
                    lineHeight = (26 * textScale).sp,
                    color = Color(0xFFE2E8F0)
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
                        .border(1.dp, BorderColor, RoundedCornerShape(16.dp)),
                    contentScale = ContentScale.Crop
                )
                Text(
                    text = "Source Link: ${article.link}",
                    style = MaterialTheme.typography.bodyMedium.copy(fontSize = (14 * textScale).sp),
                    color = NeonCyan
                )
            }
        }
    }
}

@Composable
fun FloatingBottomDeck(
    isBookmarked: Boolean,
    commentCount: Int,
    onAiSummaryClick: () -> Unit,
    onBookmarkClick: () -> Unit,
    onShareClick: () -> Unit,
    onCommentClick: () -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 24.dp, vertical = 16.dp),
        contentAlignment = Alignment.Center
    ) {
        Surface(
            modifier = Modifier
                .height(64.dp)
                .fillMaxWidth(0.9f)
                .border(1.dp, Brush.horizontalGradient(listOf(NeonCyan, NeonViolet)), CircleShape),
            shape = CircleShape,
            color = Color(0xFF121826).copy(alpha = 0.85f),
            shadowElevation = 12.dp
        ) {
            Row(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 20.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(onClick = onAiSummaryClick) {
                    Icon(imageVector = Icons.Default.Info, contentDescription = "AI Summarize", tint = NeonCyan)
                }

                IconButton(onClick = onBookmarkClick) {
                    val animScale by animateFloatAsState(
                        targetValue = if (isBookmarked) 1.3f else 1f,
                        animationSpec = spring(dampingRatio = Spring.DampingRatioMediumBouncy)
                    )
                    Icon(
                        imageVector = if (isBookmarked) Icons.Default.Favorite else Icons.Default.FavoriteBorder,
                        contentDescription = "Bookmark",
                        tint = if (isBookmarked) NeonViolet else Color.White,
                        modifier = Modifier.scale(animScale)
                    )
                }

                IconButton(onClick = onShareClick) {
                    Icon(imageVector = Icons.Default.Share, contentDescription = "Share", tint = Color.White)
                }

                BadgedBox(
                    badge = {
                        Badge(containerColor = NeonCyan, contentColor = DarkBackground) {
                            Text("$commentCount", fontWeight = FontWeight.Bold)
                        }
                    }
                ) {
                    IconButton(onClick = onCommentClick) {
                        Icon(imageVector = Icons.Default.Notifications, contentDescription = "Comments", tint = Color.White)
                    }
                }
            }
        }
    }
}

@Composable
fun AiSummaryDialog(onDismiss: () -> Unit) {
    AlertDialog(
        onDismissRequest = onDismiss,
        containerColor = Color(0xFF121B2E),
        shape = RoundedCornerShape(24.dp),
        title = {
            Row(
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(Icons.Default.Star, contentDescription = null, tint = NeonCyan)
                Text("AI Executive Briefing", color = Color.White, fontWeight = FontWeight.Bold)
            }
        },
        text = {
            Text(
                text = "This article details groundbreaking advancements in quantum-neural integration. Key takeaways highlight a 340% increase in multi-agent efficiency and secure zero-day resistance, marking a major milestone for Q1 2027 deployments.",
                color = Color.LightGray,
                lineHeight = 22.sp
            )
        },
        confirmButton = {
            Button(
                onClick = onDismiss,
                colors = ButtonDefaults.buttonColors(containerColor = NeonCyan)
            ) {
                Text("Got it", color = DarkBackground, fontWeight = FontWeight.Bold)
            }
        }
    )
}
