package com.example.shared.ui.profile

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
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.LocalFireDepartment
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.VerticalDivider
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.shared.domain.model.Avatar
import com.example.shared.domain.model.StreakInfo
import com.example.shared.domain.usecase.GetProfileOptionsUseCase
import com.example.shared.ui.theme.ThemeManager

/**
 * User Profile Screen displaying user information, profile settings/options, and app version.
 *
 * @param userName Current user name displayed in header card.
 * @param userEmail Current user email displayed in header card.
 * @param avatarId Current avatar identifier.
 * @param streakInfo User reading streak and star rank metrics.
 * @param onHeaderClick Callback when user header card is tapped.
 * @param onItemClick Callback invoked when a profile option item is tapped.
 * @param onThemeClick Callback invoked when the Theme option is tapped.
 * @param onBookmarkedNewsClick Callback invoked when Bookmarked news is tapped.
 * @param onContactUsClick Callback invoked when Contact us is tapped.
 * @param getProfileOptionsUseCase Use case providing the list of profile option items.
 */
@Composable
fun ProfileScreen(
    userName: String = "Samachar Reader",
    userEmail: String = "reader@samachar.ai",
    avatarId: String = "default",
    streakInfo: StreakInfo = StreakInfo(),
    onHeaderClick: () -> Unit = {},
    onItemClick: (String) -> Unit,
    onThemeClick: () -> Unit = {},
    onBookmarkedNewsClick: () -> Unit = {},
    onContactUsClick: () -> Unit = {},
    getProfileOptionsUseCase: GetProfileOptionsUseCase = remember { GetProfileOptionsUseCase() }
) {
    val options = remember(getProfileOptionsUseCase) { getProfileOptionsUseCase() }

    val currentTheme = ThemeManager.currentTheme
    val darkBackground = currentTheme.backgroundColor
    val neonCyan = currentTheme.accentColor
    val neonViolet = currentTheme.accentColor.copy(alpha = 0.7f)
    val surfaceGlass = currentTheme.cardColor
    val borderColor = currentTheme.accentColor.copy(alpha = 0.3f)
    val textColor = if (currentTheme.isLight) Color.Black else Color.White
    val subtitleColor = if (currentTheme.isLight) Color.DarkGray else Color.LightGray

    Scaffold(
        containerColor = darkBackground,
        contentWindowInsets = WindowInsets(0, 0, 0, 0)
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .drawBehind {
                    // Header radial gradient glow
                    drawCircle(
                        brush = Brush.radialGradient(
                            colors = listOf(neonCyan.copy(alpha = 0.15f), Color.Transparent),
                            center = Offset(size.width / 2, 0f),
                            radius = size.width * 0.8f
                        ),
                        radius = size.width * 0.8f
                    )
                }
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .statusBarsPadding()
            ) {
                // Header
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 20.dp, vertical = 16.dp),
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    Text(
                        text = "PROFILE",
                        style = MaterialTheme.typography.titleLarge.copy(
                            fontWeight = FontWeight.Bold,
                            color = textColor,
                            letterSpacing = 1.5.sp
                        )
                    )

                    // User Header Card
                    UserHeaderCard(
                        userName = userName,
                        userEmail = userEmail,
                        avatarId = avatarId,
                        surfaceGlass = surfaceGlass,
                        borderColor = borderColor,
                        neonViolet = neonViolet,
                        neonCyan = neonCyan,
                        textColor = textColor,
                        subtitleColor = subtitleColor,
                        onClick = onHeaderClick
                    )

                    // Quick Stats Row with real streak data
                    ProfileStatsRow(
                        streakInfo = streakInfo,
                        surfaceGlass = surfaceGlass,
                        borderColor = borderColor,
                        neonCyan = neonCyan,
                        textColor = textColor,
                        subtitleColor = subtitleColor
                    )
                }

                // Options List (takes remaining space)
                LazyColumn(
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f),
                    contentPadding = PaddingValues(horizontal = 20.dp, vertical = 8.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    items(options) { item ->
                        ProfileOptionCard(
                            item = item,
                            surfaceGlass = surfaceGlass,
                            borderColor = borderColor,
                            neonCyan = neonCyan,
                            neonViolet = neonViolet,
                            textColor = textColor,
                            onClick = {
                                if (item.title == "Theme") {
                                    onThemeClick()
                                } else if (item.title == "Bookmarked news") {
                                    onBookmarkedNewsClick()
                                } else if (item.title == "Contact us") {
                                    onContactUsClick()
                                } else {
                                    onItemClick(item.title)
                                }
                            }
                        )
                    }
                }

                // Version anchored at the very bottom
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 16.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "Version 1.0.0",
                        style = MaterialTheme.typography.bodyMedium,
                        color = subtitleColor,
                        fontWeight = FontWeight.Medium
                    )
                }
            }
        }
    }
}

/**
 * User Header Card displaying avatar, name, email, pro badge, and edit prompt.
 */
@Composable
fun UserHeaderCard(
    userName: String,
    userEmail: String,
    avatarId: String,
    surfaceGlass: Color,
    borderColor: Color,
    neonViolet: Color,
    neonCyan: Color,
    textColor: Color,
    subtitleColor: Color,
    onClick: () -> Unit
) {
    val avatar = Avatar.fromId(avatarId)

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick),
        shape = RoundedCornerShape(24.dp),
        colors = CardDefaults.cardColors(containerColor = surfaceGlass),
        border = BorderStroke(1.5.dp, Brush.linearGradient(listOf(neonCyan, neonViolet))),
        elevation = CardDefaults.cardElevation(defaultElevation = 8.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(20.dp),
            verticalArrangement = Arrangement.spacedBy(14.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                Surface(
                    shape = CircleShape,
                    color = neonViolet.copy(alpha = 0.25f),
                    border = BorderStroke(2.dp, neonCyan),
                    modifier = Modifier.size(60.dp)
                ) {
                    Box(contentAlignment = Alignment.Center) {
                        Icon(
                            imageVector = avatar.icon,
                            contentDescription = null,
                            tint = neonCyan,
                            modifier = Modifier.size(34.dp)
                        )
                    }
                }

                Column(modifier = Modifier.weight(1f)) {
                    Row(
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = userName,
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold,
                            color = textColor
                        )
                        Surface(
                            shape = RoundedCornerShape(10.dp),
                            color = neonCyan.copy(alpha = 0.2f),
                            border = BorderStroke(1.dp, neonCyan.copy(alpha = 0.6f))
                        ) {
                            Text(
                                text = "⚡ PRO",
                                color = neonCyan,
                                style = MaterialTheme.typography.labelSmall,
                                fontWeight = FontWeight.Bold,
                                modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                            )
                        }
                    }
                    Spacer(modifier = Modifier.height(2.dp))
                    Text(
                        text = userEmail,
                        style = MaterialTheme.typography.bodyMedium,
                        color = subtitleColor
                    )
                }
            }

            HorizontalDivider(
                thickness = 1.dp,
                color = borderColor.copy(alpha = 0.4f)
            )

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Tap to edit profile & avatar",
                    style = MaterialTheme.typography.labelMedium,
                    color = neonCyan,
                    fontWeight = FontWeight.SemiBold
                )
                Surface(
                    shape = CircleShape,
                    color = neonCyan.copy(alpha = 0.15f),
                    modifier = Modifier.size(28.dp)
                ) {
                    Box(contentAlignment = Alignment.Center) {
                        Icon(
                            imageVector = Icons.Default.Edit,
                            contentDescription = "Edit Profile",
                            tint = neonCyan,
                            modifier = Modifier.size(14.dp)
                        )
                    }
                }
            }
        }
    }
}

/**
 * Quick stats summary bar showing real reading streak, star level, and title.
 */
@Composable
fun ProfileStatsRow(
    streakInfo: StreakInfo,
    surfaceGlass: Color,
    borderColor: Color,
    neonCyan: Color,
    textColor: Color,
    subtitleColor: Color
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(18.dp),
        colors = CardDefaults.cardColors(containerColor = surfaceGlass),
        border = BorderStroke(1.dp, borderColor),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 12.dp, horizontal = 16.dp),
            horizontalArrangement = Arrangement.SpaceAround,
            verticalAlignment = Alignment.CenterVertically
        ) {
            StatItem(
                icon = Icons.Default.LocalFireDepartment,
                value = "${streakInfo.streakCount} Days",
                label = "Reading Streak",
                neonCyan = neonCyan,
                textColor = textColor,
                subtitleColor = subtitleColor
            )
            VerticalDivider(modifier = Modifier.height(28.dp), color = borderColor.copy(alpha = 0.5f))
            StatItem(
                icon = Icons.Default.Star,
                value = "Level ${streakInfo.starRating}",
                label = streakInfo.readerTitle,
                neonCyan = neonCyan,
                textColor = textColor,
                subtitleColor = subtitleColor
            )
            VerticalDivider(modifier = Modifier.height(28.dp), color = borderColor.copy(alpha = 0.5f))
            StatItem(
                icon = Icons.Default.Person,
                value = "Active",
                label = "AI Voice Synced",
                neonCyan = neonCyan,
                textColor = textColor,
                subtitleColor = subtitleColor
            )
        }
    }
}

@Composable
fun StatItem(
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    value: String,
    label: String,
    neonCyan: Color,
    textColor: Color,
    subtitleColor: Color
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(2.dp)
    ) {
        Icon(
            imageVector = icon,
            contentDescription = null,
            tint = neonCyan,
            modifier = Modifier.size(18.dp)
        )
        Text(
            text = value,
            style = MaterialTheme.typography.titleSmall,
            fontWeight = FontWeight.Bold,
            color = textColor
        )
        Text(
            text = label,
            style = MaterialTheme.typography.labelSmall,
            color = subtitleColor
        )
    }
}

/**
 * Card item representing an individual profile option with gradient border and glowing icon badge.
 */
@Composable
fun ProfileOptionCard(
    item: ProfileOptionItem,
    surfaceGlass: Color,
    borderColor: Color,
    neonCyan: Color,
    neonViolet: Color,
    textColor: Color,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick),
        shape = RoundedCornerShape(18.dp),
        colors = CardDefaults.cardColors(containerColor = surfaceGlass),
        border = BorderStroke(1.dp, Brush.linearGradient(listOf(neonCyan, neonViolet.copy(alpha = 0.5f)))),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 14.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(14.dp)
            ) {
                Surface(
                    shape = CircleShape,
                    color = neonCyan.copy(alpha = 0.15f),
                    border = BorderStroke(1.dp, neonCyan.copy(alpha = 0.5f)),
                    modifier = Modifier.size(40.dp)
                ) {
                    Box(contentAlignment = Alignment.Center) {
                        Icon(
                            imageVector = item.icon,
                            contentDescription = null,
                            tint = neonCyan,
                            modifier = Modifier.size(20.dp)
                        )
                    }
                }

                Text(
                    text = item.title,
                    style = MaterialTheme.typography.bodyLarge,
                    fontWeight = FontWeight.SemiBold,
                    color = textColor
                )
            }

            Surface(
                shape = CircleShape,
                color = surfaceGlass,
                border = BorderStroke(1.dp, borderColor.copy(alpha = 0.5f)),
                modifier = Modifier.size(28.dp)
            ) {
                Box(contentAlignment = Alignment.Center) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.KeyboardArrowRight,
                        contentDescription = null,
                        tint = textColor.copy(alpha = 0.7f),
                        modifier = Modifier.size(16.dp)
                    )
                }
            }
        }
    }
}
