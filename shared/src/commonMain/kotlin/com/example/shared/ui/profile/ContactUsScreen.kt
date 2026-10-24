package com.example.shared.ui.profile

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Email
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.shared.ui.theme.*

/**
 * Contact Us screen displaying support details and email contact information.
 *
 * @param onBackClick Callback when the toolbar back button is clicked.
 * @param onEmailClick Callback when clicking the email address.
 */
@Composable
fun ContactUsScreen(
    onBackClick: () -> Unit,
    onEmailClick: (String, String, String) -> Unit
) {
    val currentTheme = ThemeManager.currentTheme
    val darkBackground = currentTheme.backgroundColor
    val neonCyan = currentTheme.accentColor
    val surfaceGlass = currentTheme.cardColor
    val borderColor = currentTheme.accentColor.copy(alpha = 0.3f)
    val textColor = if (currentTheme.isLight) Color.Black else Color.White
    val subtitleColor = if (currentTheme.isLight) Color.DarkGray else Color.LightGray

    Scaffold(
        containerColor = darkBackground,
        contentWindowInsets = WindowInsets(0, 0, 0, 0),
        topBar = {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .statusBarsPadding()
                    .padding(horizontal = Spacing16, vertical = Spacing12),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(Spacing16)
            ) {
                Surface(
                    shape = CircleShape,
                    color = surfaceGlass,
                    border = BorderStroke(Spacing1, borderColor)
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
                    text = "Contact Us",
                    style = MaterialTheme.typography.titleLarge.copy(
                        fontWeight = FontWeight.Bold,
                        color = textColor,
                        letterSpacing = 1.sp
                    )
                )
            }
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .verticalScroll(rememberScrollState())
                .padding(Spacing24),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(24.dp)
        ) {
            Spacer(modifier = Modifier.height(Spacing24))

            // Header Icon Badge
            Surface(
                shape = CircleShape,
                color = neonCyan.copy(alpha = 0.15f),
                border = BorderStroke(1.5.dp, neonCyan),
                modifier = Modifier.size(Spacing72)
            ) {
                Box(contentAlignment = Alignment.Center) {
                    Icon(
                        imageVector = Icons.Default.Email,
                        contentDescription = null,
                        tint = neonCyan,
                        modifier = Modifier.size(36.dp)
                    )
                }
            }

            Text(
                text = "We are happy to serve you.\nYour feedback is very important for us!",
                style = MaterialTheme.typography.titleMedium.copy(
                    fontWeight = FontWeight.Bold,
                    color = textColor,
                    lineHeight = 24.sp
                ),
                textAlign = TextAlign.Center
            )

            Spacer(modifier = Modifier.height(Spacing12))

            // Direct Email Card
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { onEmailClick("sauravsumanxs@gmail.com", "Samachar Feedback", "") },
                shape = RoundedCornerShape(20.dp),
                colors = CardDefaults.cardColors(containerColor = surfaceGlass),
                border = BorderStroke(1.dp, borderColor),
                elevation = CardDefaults.cardElevation(defaultElevation = 6.dp)
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(24.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Text(
                        text = "Kindly mail us directly at",
                        style = MaterialTheme.typography.bodyLarge,
                        color = subtitleColor
                    )
                    Text(
                        text = "sauravsumanxs@gmail.com",
                        style = MaterialTheme.typography.titleMedium.copy(
                            fontWeight = FontWeight.Bold,
                            color = neonCyan
                        )
                    )
                }
            }
        }
    }
}
