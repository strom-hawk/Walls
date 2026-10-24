package com.example.trynewthingsinandroid.navigation

import android.widget.Toast
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.shared.ui.profile.EditProfileScreen
import com.example.trynewthingsinandroid.R
import com.example.trynewthingsinandroid.presentation.EditProfileScreenUIEvent
import com.example.trynewthingsinandroid.presentation.EditProfileScreenUIEffect
import com.example.trynewthingsinandroid.presentation.EditProfileScreenViewModel

/**
 * Navigation destination wrapper composable for the Edit Profile screen.
 *
 * @param navController Controller managing app navigation.
 * @param viewModel ViewModel handling edit profile state and persistence.
 */
@Composable
fun EditProfileDestination(
    navController: NavController,
    viewModel: EditProfileScreenViewModel = viewModel()
) {
    val context = LocalContext.current
    val uiState by viewModel.uiState.collectAsState()

    LaunchedEffect(Unit) {
        viewModel.uiEffect.collect { effect ->
            when (effect) {
                is EditProfileScreenUIEffect.OnProfileUpdated -> {
                    Toast.makeText(context, context.getString(R.string.profile_updated_successfully), Toast.LENGTH_SHORT).show()
                    navController.popBackStack()
                }
            }
        }
    }

    EditProfileScreen(
        uiState = uiState,
        onSave = { name, email, avatar ->
            viewModel.onEvent(EditProfileScreenUIEvent.OnSaveClicked(name, email, avatar))
        },
        onBackClick = {
            navController.popBackStack()
        }
    )
}
