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
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.shared.domain.usecase.GetProfileOptionsUseCase
import com.example.shared.ui.theme.ThemeManager

/**
 * User Profile Screen displaying user information, profile settings/options, and app version.
 *
 * @param onItemClick Callback invoked when a profile option item is tapped.
 * @param onThemeClick Callback invoked when the Theme option is tapped.
 * @param getProfileOptionsUseCase Use case providing the list of profile option items.
 */
@Composable
fun ProfileScreen(
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
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
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
                Surface(
                    shape = RoundedCornerShape(20.dp),
                    color = surfaceGlass,
                    border = BorderStroke(1.dp, borderColor),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Row(
                        modifier = Modifier.padding(16.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(16.dp)
                    ) {
                        Surface(
                            shape = CircleShape,
                            color = neonViolet.copy(alpha = 0.25f),
                            border = BorderStroke(1.5.dp, neonCyan),
                            modifier = Modifier.size(56.dp)
                        ) {
                            Box(contentAlignment = Alignment.Center) {
                                Icon(
                                    imageVector = Icons.Default.Person,
                                    contentDescription = null,
                                    tint = neonCyan,
                                    modifier = Modifier.size(32.dp)
                                )
                            }
                        }

                        Column {
                            Text(
                                text = "Samachar Reader",
                                style = MaterialTheme.typography.titleMedium,
                                fontWeight = FontWeight.Bold,
                                color = textColor
                            )
                            Spacer(modifier = Modifier.height(2.dp))
                            Text(
                                text = "reader@samachar.ai",
                                style = MaterialTheme.typography.bodyMedium,
                                color = neonCyan
                            )
                        }
                    }
                }
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

/**
 * Card item representing an individual profile option.
 *
 * @param item The profile option data containing title and icon.
 * @param onClick Callback when the card is clicked.
 */
@Composable
fun ProfileOptionCard(
    item: ProfileOptionItem,
    surfaceGlass: Color,
    borderColor: Color,
    neonCyan: Color,
    textColor: Color,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = surfaceGlass),
        border = BorderStroke(1.dp, borderColor)
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
                    modifier = Modifier.size(38.dp)
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

            Icon(
                imageVector = Icons.AutoMirrored.Filled.KeyboardArrowRight,
                contentDescription = null,
                tint = textColor.copy(alpha = 0.6f),
                modifier = Modifier.size(20.dp)
            )
        }
    }
}
