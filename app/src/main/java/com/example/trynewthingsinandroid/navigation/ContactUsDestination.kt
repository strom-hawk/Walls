package com.example.trynewthingsinandroid.navigation

import android.content.Intent
import android.widget.Toast
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.shared.ui.profile.ContactUsScreen
import com.example.trynewthingsinandroid.R
import com.example.trynewthingsinandroid.presentation.ContactUsScreenUIEffect
import com.example.trynewthingsinandroid.presentation.ContactUsScreenViewModel

/**
 * Navigation destination wrapper composable for the Contact Us screen.
 *
 * @param navController Controller managing app navigation.
 * @param viewModel ViewModel handling contact us state and side effects.
 */
@Composable
fun ContactUsDestination(
    navController: NavController,
    viewModel: ContactUsScreenViewModel = viewModel()
) {
    val context = LocalContext.current
    val uiState by viewModel.uiState.collectAsState()

    LaunchedEffect(Unit) {
        viewModel.uiEffect.collect { effect ->
            when (effect) {
                is ContactUsScreenUIEffect.OpenEmailClient -> {
                    val intent = Intent(Intent.ACTION_SENDTO).apply {
                        data = android.net.Uri.parse("mailto:${effect.email}")
                        putExtra(Intent.EXTRA_SUBJECT, effect.subject)
                        putExtra(Intent.EXTRA_TEXT, effect.message)
                    }
                    try {
                        context.startActivity(intent)
                    } catch (_: Exception) {
                        Toast.makeText(context, context.getString(R.string.no_email_app_found), Toast.LENGTH_SHORT).show()
                    }
                }
                is ContactUsScreenUIEffect.NavigateBack -> {
                    navController.popBackStack()
                }
            }
        }
    }

    ContactUsScreen(
        uiState = uiState,
        onEvent = { event -> viewModel.onEvent(event) }
    )
}
