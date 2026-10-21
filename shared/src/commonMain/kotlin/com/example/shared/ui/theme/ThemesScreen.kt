package com.example.shared.ui.theme

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
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
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CheckboxDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

/**
 * Themes screen allowing users to select from futuristic and eye-catching themes,
 * grouped by Light Themes and Dark Themes.
 *
 * @param onBackClick Callback invoked when the user clicks the back navigation button.
 */
@Composable
fun ThemesScreen(
    onBackClick: () -> Unit
) {
    val themes = ThemeManager.themes
    val currentTheme = ThemeManager.currentTheme

    val lightThemes = themes.filter { it.isLight }
    val darkThemes = themes.filter { !it.isLight }

    Scaffold(
        containerColor = currentTheme.backgroundColor,
        contentWindowInsets = WindowInsets(0, 0, 0, 0)
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .statusBarsPadding()
        ) {
            // Top App Bar
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 12.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    Surface(
                        shape = CircleShape,
                        color = currentTheme.cardColor,
                        border = BorderStroke(1.dp, currentTheme.accentColor.copy(alpha = 0.5f))
                    ) {
                        IconButton(onClick = onBackClick) {
                            Icon(
                                imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                                contentDescription = "Back",
                                tint = if (currentTheme.isLight) Color.DarkGray else Color.White
                            )
                        }
                    }

                    Text(
                        text = "Themes",
                        style = MaterialTheme.typography.headlineMedium.copy(
                            fontWeight = FontWeight.Bold,
                            color = if (currentTheme.isLight) Color.Black else Color.White,
                            letterSpacing = 1.5.sp
                        )
                    )
                }
            }

            // Themes List Grouped by Light & Dark
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .weight(1f),
                contentPadding = PaddingValues(horizontal = 20.dp, vertical = 12.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                // Light Themes Section Header
                item {
                    Text(
                        text = "Light Themes",
                        style = MaterialTheme.typography.titleMedium.copy(
                            fontWeight = FontWeight.Bold,
                            color = currentTheme.accentColor,
                            letterSpacing = 1.2.sp
                        ),
                        modifier = Modifier.padding(bottom = 4.dp)
                    )
                }

                items(lightThemes, key = { it.id }) { theme ->
                    val isSelected = theme.id == currentTheme.id
                    ThemeCard(
                        theme = theme,
                        isSelected = isSelected,
                        onSelect = { ThemeManager.setTheme(theme.id) }
                    )
                }

                // Dark Themes Section Header
                item {
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = "Dark Themes",
                        style = MaterialTheme.typography.titleMedium.copy(
                            fontWeight = FontWeight.Bold,
                            color = currentTheme.accentColor,
                            letterSpacing = 1.2.sp
                        ),
                        modifier = Modifier.padding(bottom = 4.dp)
                    )
                }

                items(darkThemes, key = { it.id }) { theme ->
                    val isSelected = theme.id == currentTheme.id
                    ThemeCard(
                        theme = theme,
                        isSelected = isSelected,
                        onSelect = { ThemeManager.setTheme(theme.id) }
                    )
                }
            }
        }
    }
}

@Composable
fun ThemeCard(
    theme: AppThemeOption,
    isSelected: Boolean,
    onSelect: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onSelect),
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(containerColor = theme.cardColor),
        border = BorderStroke(
            width = if (isSelected) 2.dp else 1.dp,
            color = if (isSelected) theme.accentColor else theme.accentColor.copy(alpha = 0.3f)
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = if (isSelected) 8.dp else 2.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(20.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Column(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    // Color swatch preview
                    Box(
                        modifier = Modifier
                            .size(24.dp)
                            .clip(CircleShape)
                            .background(theme.accentColor),
                        contentAlignment = Alignment.Center
                    ) {
                        Box(
                            modifier = Modifier
                                .size(10.dp)
                                .clip(CircleShape)
                                .background(theme.backgroundColor)
                        )
                    }

                    Text(
                        text = theme.name,
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        color = if (theme.isLight) Color.Black else Color.White
                    )
                }

                Text(
                    text = theme.description,
                    style = MaterialTheme.typography.bodyMedium,
                    color = if (theme.isLight) Color.Gray else Color.LightGray
                )
            }

            Spacer(modifier = Modifier.width(16.dp))

            Checkbox(
                checked = isSelected,
                onCheckedChange = { onSelect() },
                colors = CheckboxDefaults.colors(
                    checkedColor = theme.accentColor,
                    uncheckedColor = if (theme.isLight) Color.Gray else Color.LightGray,
                    checkmarkColor = theme.backgroundColor
                )
            )
        }
    }
}
