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
import com.example.shared.ui.news.NewsDetailScreen
import com.example.trynewthingsinandroid.presentation.NewsDetailUIEffect
import com.example.trynewthingsinandroid.presentation.NewsDetailsScreenViewModel

/**
 * Navigation destination wrapper composable for the News Detail screen.
 *
 * @param navController Controller managing app navigation.
 * @param viewModel ViewModel handling edit profile state and persistence.
 */
@Composable
fun NewsDetailDestination(
    navController: NavController,
    viewModel: NewsDetailsScreenViewModel = viewModel()
) {
    val context = LocalContext.current
    val uiState by viewModel.uiState.collectAsState()

    LaunchedEffect(Unit) {
        viewModel.uiEffect.collect { effect ->
            when (effect) {
                is NewsDetailUIEffect.ShowToast -> {
                    Toast.makeText(context, effect.message, Toast.LENGTH_SHORT).show()
                }
                is NewsDetailUIEffect.NavigateToWebView -> {
                    navController.navigate(Screen.NewsWebView.createRoute(effect.url))
                }
                is NewsDetailUIEffect.ShareArticle -> {
                    val sendIntent = Intent().apply {
                        action = Intent.ACTION_SEND
                        putExtra(Intent.EXTRA_SUBJECT, effect.title)
                        putExtra(Intent.EXTRA_TEXT, "${effect.title}\n${effect.url}")
                        type = "text/plain"
                    }
                    val shareIntent = Intent.createChooser(sendIntent, null)
                    context.startActivity(shareIntent)
                }
                is NewsDetailUIEffect.NavigateBack -> {
                    navController.popBackStack()
                }
            }
        }
    }

    NewsDetailScreen(
        uiState = uiState,
        onEvent = { event -> viewModel.onEvent(event) }
    )
}
