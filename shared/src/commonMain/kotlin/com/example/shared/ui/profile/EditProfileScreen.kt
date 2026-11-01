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
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Face
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Shield
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.shared.domain.model.Avatar
import com.example.shared.presentation.EditProfileUiState
import com.example.shared.ui.theme.*

/**
 * Edit Profile screen allowing users to update their Name, Email, and Avatar with keyboard IME padding.
 *
 * @param uiState Current UI State for Edit Profile.
 * @param onSave Callback when profile is saved (name, email, avatarId).
 * @param onBackClick Callback when toolbar back button is clicked.
 */
@Composable
fun EditProfileScreen(
    uiState: EditProfileUiState,
    onSave: (String, String, String) -> Unit,
    onBackClick: () -> Unit
) {
    var name by remember(uiState.name) { mutableStateOf(uiState.name) }
    var email by remember(uiState.email) { mutableStateOf(uiState.email) }
    var selectedAvatar by remember(uiState.avatar) { mutableStateOf(uiState.avatar) }

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
            EditProfileTopBar(
                textColor = textColor,
                surfaceGlass = surfaceGlass,
                borderColor = borderColor,
                onBackClick = onBackClick
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .verticalScroll(rememberScrollState())
                .imePadding()
                .padding(Spacing24),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(20.dp)
        ) {
            Spacer(modifier = Modifier.height(Spacing8))

            // Avatar Selector Sub-component
            AvatarSelectionSection(
                selectedAvatar = selectedAvatar,
                availableAvatars = uiState.availableAvatars,
                textColor = textColor,
                neonCyan = neonCyan,
                surfaceGlass = surfaceGlass,
                borderColor = borderColor,
                onAvatarSelected = { selectedAvatar = it }
            )

            Spacer(modifier = Modifier.height(Spacing8))

            // Form Sub-component
            ProfileDetailsFormCard(
                name = name,
                email = email,
                onNameChange = { name = it },
                onEmailChange = { email = it },
                onSaveClick = {
                    if (name.isNotBlank() && email.isNotBlank()) {
                        onSave(name, email, selectedAvatar.id)
                    }
                },
                darkBackground = darkBackground,
                neonCyan = neonCyan,
                surfaceGlass = surfaceGlass,
                borderColor = borderColor,
                textColor = textColor,
                subtitleColor = subtitleColor
            )
        }
    }
}

@Composable
fun EditProfileTopBar(
    textColor: Color,
    surfaceGlass: Color,
    borderColor: Color,
    onBackClick: () -> Unit
) {
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
            text = "Edit Profile",
            style = MaterialTheme.typography.titleLarge.copy(
                fontWeight = FontWeight.Bold,
                color = textColor,
                letterSpacing = 1.sp
            )
        )
    }
}

@Composable
fun AvatarSelectionSection(
    selectedAvatar: Avatar,
    availableAvatars: List<Avatar>,
    textColor: Color,
    neonCyan: Color,
    surfaceGlass: Color,
    borderColor: Color,
    onAvatarSelected: (Avatar) -> Unit
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Text(
            text = "Choose Avatar",
            style = MaterialTheme.typography.titleMedium.copy(
                fontWeight = FontWeight.Bold,
                color = textColor
            )
        )

        Row(
            horizontalArrangement = Arrangement.spacedBy(14.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            availableAvatars.forEach { avatar ->
                val isSelected = avatar == selectedAvatar
                AvatarItem(
                    avatar = avatar,
                    isSelected = isSelected,
                    neonCyan = neonCyan,
                    surfaceGlass = surfaceGlass,
                    borderColor = borderColor,
                    textColor = textColor,
                    onSelect = { onAvatarSelected(avatar) }
                )
            }
        }
    }
}

@Composable
fun AvatarItem(
    avatar: Avatar,
    isSelected: Boolean,
    neonCyan: Color,
    surfaceGlass: Color,
    borderColor: Color,
    textColor: Color,
    onSelect: () -> Unit
) {
    Surface(
        shape = CircleShape,
        color = if (isSelected) neonCyan.copy(alpha = 0.3f) else surfaceGlass,
        border = BorderStroke(
            width = if (isSelected) 2.dp else Spacing1,
            color = if (isSelected) neonCyan else borderColor
        ),
        modifier = Modifier
            .size(52.dp)
            .clickable(onClick = onSelect)
    ) {
        Box(contentAlignment = Alignment.Center) {
            Icon(
                imageVector = avatar.icon,
                contentDescription = avatar.id,
                tint = if (isSelected) neonCyan else textColor,
                modifier = Modifier.size(26.dp)
            )
            if (isSelected) {
                Surface(
                    shape = CircleShape,
                    color = neonCyan,
                    modifier = Modifier
                        .size(16.dp)
                        .align(Alignment.BottomEnd)
                ) {
                    Box(contentAlignment = Alignment.Center) {
                        Icon(
                            imageVector = Icons.Default.Check,
                            contentDescription = null,
                            tint = Color.Black,
                            modifier = Modifier.size(10.dp)
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun ProfileDetailsFormCard(
    name: String,
    email: String,
    onNameChange: (String) -> Unit,
    onEmailChange: (String) -> Unit,
    onSaveClick: () -> Unit,
    darkBackground: Color,
    neonCyan: Color,
    surfaceGlass: Color,
    borderColor: Color,
    textColor: Color,
    subtitleColor: Color
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(24.dp),
        colors = CardDefaults.cardColors(containerColor = surfaceGlass),
        border = BorderStroke(Spacing1, borderColor),
        elevation = CardDefaults.cardElevation(defaultElevation = 8.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(24.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Text(
                text = "Profile Details",
                style = MaterialTheme.typography.titleMedium.copy(
                    fontWeight = FontWeight.Bold,
                    color = neonCyan
                )
            )

            Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
                Text("Name", style = MaterialTheme.typography.bodySmall, color = subtitleColor)
                OutlinedTextField(
                    value = name,
                    onValueChange = onNameChange,
                    placeholder = { Text("Enter your name", color = subtitleColor) },
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(16.dp),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedContainerColor = darkBackground.copy(alpha = 0.5f),
                        unfocusedContainerColor = darkBackground.copy(alpha = 0.3f),
                        focusedBorderColor = neonCyan,
                        unfocusedBorderColor = borderColor,
                        focusedTextColor = textColor,
                        unfocusedTextColor = textColor
                    ),
                    singleLine = true
                )
            }

            Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
                Text("Email", style = MaterialTheme.typography.bodySmall, color = subtitleColor)
                OutlinedTextField(
                    value = email,
                    onValueChange = onEmailChange,
                    placeholder = { Text("Enter your email", color = subtitleColor) },
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(16.dp),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedContainerColor = darkBackground.copy(alpha = 0.5f),
                        unfocusedContainerColor = darkBackground.copy(alpha = 0.3f),
                        focusedBorderColor = neonCyan,
                        unfocusedBorderColor = borderColor,
                        focusedTextColor = textColor,
                        unfocusedTextColor = textColor
                    ),
                    singleLine = true
                )
            }

            Spacer(modifier = Modifier.height(8.dp))

            Button(
                onClick = onSaveClick,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp),
                shape = RoundedCornerShape(25.dp),
                colors = ButtonDefaults.buttonColors(containerColor = neonCyan),
                elevation = ButtonDefaults.buttonElevation(defaultElevation = 6.dp)
            ) {
                Text(
                    text = "Save Profile",
                    color = Color.Black,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold
                )
            }
        }
    }
}
