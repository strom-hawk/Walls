package com.example.shared.ui.news

import androidx.compose.animation.*
import androidx.compose.animation.core.*
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.draw.scale
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.*
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil3.compose.AsyncImage
import com.example.shared.domain.model.Article
import kotlinx.coroutines.delay
import kotlin.time.Duration.Companion.seconds

@Composable
fun NewsDetailScreen(
    article: Article,
    onBackClick: () -> Unit
) {
    // Interactive states
    var textScale by remember { mutableStateOf(1f) } // A-/A+ control
    var isBookmarked by remember { mutableStateOf(false) }
    var isAiVoicePlaying by remember { mutableStateOf(false) }
    var showAiSummaryDialog by remember { mutableStateOf(false) }
    var commentCount by remember { mutableStateOf(42) }
    var isShared by remember { mutableStateOf(false) }

    // Futuristic Dark Theme Colors
    val neonCyan = Color(0xFF00E5FF)
    val neonViolet = Color(0xFF7C4DFF)
    val darkBackground = Color(0xFF0A0E1A)
    val surfaceGlass = Color(0xFF161D31).copy(alpha = 0.75f)
    val borderColor = Color(0xFF2A3655).copy(alpha = 0.6f)

    Scaffold(
        containerColor = darkBackground,
        bottomBar = {
            // 5. Bottom Navigation Bar (Glass Floating Deck)
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
                        .border(1.dp, Brush.horizontalGradient(listOf(neonCyan, neonViolet)), CircleShape),
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
                        // AI Summarize Action
                        IconButton(onClick = { showAiSummaryDialog = true }) {
                            Icon(
                                imageVector = Icons.Default.Info,
                                contentDescription = "AI Summarize",
                                tint = neonCyan
                            )
                        }

                        // Bookmark Animated Toggle
                        IconButton(onClick = { isBookmarked = !isBookmarked }) {
                            val animScale by animateFloatAsState(
                                targetValue = if (isBookmarked) 1.3f else 1f,
                                animationSpec = spring(dampingRatio = Spring.DampingRatioMediumBouncy)
                            )
                            Icon(
                                imageVector = if (isBookmarked) Icons.Default.Favorite else Icons.Default.FavoriteBorder,
                                contentDescription = "Bookmark",
                                tint = if (isBookmarked) neonViolet else Color.White,
                                modifier = Modifier.scale(animScale)
                            )
                        }

                        // Share Action
                        IconButton(onClick = { isShared = true }) {
                            Icon(
                                imageVector = Icons.Default.Share,
                                contentDescription = "Share",
                                tint = Color.White
                            )
                        }

                        // Comment Count Badge
                        BadgedBox(
                            badge = {
                                Badge(
                                    containerColor = neonCyan,
                                    contentColor = darkBackground
                                ) {
                                    Text("$commentCount", fontWeight = FontWeight.Bold)
                                }
                            }
                        ) {
                            IconButton(onClick = { commentCount++ }) {
                                Icon(
                                    imageVector = Icons.Default.Notifications,
                                    contentDescription = "Comments",
                                    tint = Color.White
                                )
                            }
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
            val scrollState = rememberScrollState()

            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .verticalScroll(scrollState)
            ) {
                // 2. Hero Section with Immersive Image & Ambient Lighting
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(380.dp)
                ) {
                    if (!article.imageUrl.isNullOrBlank()) {
                        AsyncImage(
                            model = article.imageUrl,
                            contentDescription = article.title,
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

                    // Gradient Scrim Overlay for Readability
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

                    // 1. Top Bar: Floating Translucent Back Button, Category Badge, and Text-Size Controls
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .statusBarsPadding()
                            .padding(horizontal = 16.dp, vertical = 12.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        // Back Button
                        Surface(
                            shape = CircleShape,
                            color = surfaceGlass,
                            border = BorderStroke(1.dp, borderColor)
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
                            // Category Badge with Neon Tint
                            Surface(
                                shape = RoundedCornerShape(16.dp),
                                color = surfaceGlass,
                                border = BorderStroke(1.dp, neonCyan.copy(alpha = 0.6f))
                            ) {
                                Text(
                                    text = "⚡ QUANTUM TECH",
                                    color = neonCyan,
                                    style = MaterialTheme.typography.labelSmall,
                                    fontWeight = FontWeight.Bold,
                                    modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp)
                                )
                            }

                            // Text-Size Controls (A-/A+)
                            Surface(
                                shape = RoundedCornerShape(16.dp),
                                color = surfaceGlass,
                                border = BorderStroke(1.dp, borderColor)
                            ) {
                                Row(
                                    modifier = Modifier.padding(horizontal = 4.dp, vertical = 2.dp),
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    IconButton(
                                        onClick = { if (textScale > 0.8f) textScale -= 0.1f },
                                        modifier = Modifier.size(32.dp)
                                    ) {
                                        Text("A-", color = Color.White, fontSize = 11.sp, fontWeight = FontWeight.Bold)
                                    }
                                    VerticalDivider(modifier = Modifier.height(16.dp), color = borderColor)
                                    IconButton(
                                        onClick = { if (textScale < 1.4f) textScale += 0.1f },
                                        modifier = Modifier.size(32.dp)
                                    ) {
                                        Text("A+", color = neonCyan, fontSize = 11.sp, fontWeight = FontWeight.Bold)
                                    }
                                }
                            }
                        }
                    }
                }

                // Content Details Container
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

                    // Article Title (Bold Ultra-Clean Typography, 24dp+)
                    Text(
                        text = article.title,
                        style = MaterialTheme.typography.headlineMedium.copy(
                            fontSize = (26 * textScale).sp,
                            fontWeight = FontWeight.Bold,
                            color = Color.White,
                            lineHeight = (34 * textScale).sp
                        )
                    )

                    // Author Info, Date, Reading Time & Glowing AI Voice Pill
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

                        // Glowing "Listen (AI Voice)" Pill Button
                        Surface(
                            onClick = { isAiVoicePlaying = !isAiVoicePlaying },
                            shape = CircleShape,
                            color = if (isAiVoicePlaying) neonCyan.copy(alpha = 0.2f) else surfaceGlass,
                            border = BorderStroke(1.5.dp, if (isAiVoicePlaying) neonCyan else neonViolet),
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
                                    tint = if (isAiVoicePlaying) neonCyan else Color.White,
                                    modifier = Modifier.size(16.dp)
                                )
                                Text(
                                    text = if (isAiVoicePlaying) "Playing..." else "Listen",
                                    style = MaterialTheme.typography.labelMedium,
                                    color = if (isAiVoicePlaying) neonCyan else Color.White,
                                    fontWeight = FontWeight.Bold
                                )
                            }
                        }
                    }

                    // 3. Futuristic AI Summary Card
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(20.dp),
                        colors = CardDefaults.cardColors(containerColor = surfaceGlass),
                        border = BorderStroke(1.dp, Brush.linearGradient(listOf(neonCyan, neonViolet)))
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
                                    tint = neonCyan,
                                    modifier = Modifier.size(20.dp)
                                )
                                Text(
                                    text = "⚡ AI Key Takeaways",
                                    style = MaterialTheme.typography.titleMedium,
                                    fontWeight = FontWeight.Bold,
                                    color = neonCyan
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
                                            .background(neonViolet, CircleShape)
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

                    // 4. Article Content Body
                    Column(
                        modifier = Modifier.fillMaxWidth(),
                        verticalArrangement = Arrangement.spacedBy(16.dp)
                    ) {
                        Text(
                            text = article.description.ifBlank {
                                "As quantum computing intersects with advanced neural networks, a paradigm shift is occurring across global infrastructure. Researchers have successfully stabilized quantum entanglement states for over 72 hours, unlocking unprecedented computational capabilities."
                            },
                            style = MaterialTheme.typography.bodyLarge.copy(
                                fontSize = (16 * textScale).sp,
                                lineHeight = (26 * textScale).sp,
                                color = Color(0xFFE2E8F0)
                            )
                        )

                        // Styled Blockquote with Neon Left-Side Accent Border
                        Surface(
                            modifier = Modifier.fillMaxWidth(),
                            shape = RoundedCornerShape(topEnd = 12.dp, bottomEnd = 12.dp),
                            color = Color(0xFF131B2E),
                            border = BorderStroke(
                                width = 0.5.dp,
                                color = Color.White.copy(alpha = 0.08f)
                            )
                        ) {
                            Row(modifier = Modifier.fillMaxWidth()) {
                                Box(
                                    modifier = Modifier
                                        .width(4.dp)
                                        .fillMaxHeight()
                                        .background(Brush.verticalGradient(listOf(neonCyan, neonViolet)))
                                )
                                Text(
                                    text = "\"We are witnessing the dawn of self-evolving neural frameworks capable of autonomous problem solving at cosmic scale.\" — Dr. V. Nexus",
                                    style = MaterialTheme.typography.bodyMedium.copy(
                                        fontSize = (15 * textScale).sp,
                                        fontStyle = androidx.compose.ui.text.font.FontStyle.Italic,
                                        color = Color.White
                                    ),
                                    modifier = Modifier.padding(16.dp)
                                )
                            }
                        }

                        Text(
                            text = "Further analysis reveals that energy consumption has been reduced by 45% through neuromorphic caching algorithms. Industry leaders are already drafting interoperability standards to ensure seamless integration with legacy silicon networks.",
                            style = MaterialTheme.typography.bodyLarge.copy(
                                fontSize = (16 * textScale).sp,
                                lineHeight = (26 * textScale).sp,
                                color = Color(0xFFE2E8F0)
                            )
                        )

                        // Embedded Crisp Image with Rounded Corners (16dp Radius) & Caption
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
                                Text(
                                    text = "Fig 1.1: Holographic visualization of quantum neural grid in active test mode.",
                                    style = MaterialTheme.typography.bodySmall,
                                    color = Color.Gray
                                )
                            }
                        }

                        Text(
                            text = "Source Link: ${article.link}",
                            style = MaterialTheme.typography.bodyMedium.copy(fontSize = (14 * textScale).sp),
                            color = neonCyan
                        )

                        Spacer(modifier = Modifier.height(100.dp))
                    }
                }
            }
        }
    }

    // AI Summary Dialog Modal
    if (showAiSummaryDialog) {
        AlertDialog(
            onDismissRequest = { showAiSummaryDialog = false },
            containerColor = Color(0xFF121B2E),
            shape = RoundedCornerShape(24.dp),
            title = {
                Row(
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(Icons.Default.Star, contentDescription = null, tint = neonCyan)
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
                    onClick = { showAiSummaryDialog = false },
                    colors = ButtonDefaults.buttonColors(containerColor = neonCyan)
                ) {
                    Text("Got it", color = darkBackground, fontWeight = FontWeight.Bold)
                }
            }
        )
    }

    // Share Feedback Snackbar Simulation
    if (isShared) {
        LaunchedEffect(isShared) {
            delay(2.seconds)
            isShared = false
        }
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(bottom = 100.dp),
            contentAlignment = Alignment.BottomCenter
        ) {
            Surface(
                shape = RoundedCornerShape(16.dp),
                color = neonViolet,
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
