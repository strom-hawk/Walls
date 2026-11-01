package com.example.trynewthingsinandroid.presentation

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.shared.domain.model.Avatar
import com.example.shared.presentation.EditProfileUiState
import com.example.trynewthingsinandroid.data.local.UserProfilePreferences
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

/**
 * ViewModel managing business logic and persistence for editing user profile details using MVI.
 */
class EditProfileScreenViewModel(application: Application) : AndroidViewModel(application) {
    private val profilePrefs = UserProfilePreferences(application)

    private val _uiState = MutableStateFlow(EditProfileUiState())
    val uiState: StateFlow<EditProfileUiState> = _uiState.asStateFlow()

    private val _uiEffect = MutableSharedFlow<EditProfileScreenUIEffect>()
    val uiEffect: SharedFlow<EditProfileScreenUIEffect> = _uiEffect.asSharedFlow()

    init {
        loadProfile()
    }

    private fun loadProfile() {
        val savedAvatarId = profilePrefs.getAvatarId()
        _uiState.value = EditProfileUiState(
            name = profilePrefs.getUserName(),
            email = profilePrefs.getUserEmail(),
            avatar = Avatar.fromId(savedAvatarId)
        )
    }

    private fun saveProfile(name: String, email: String, avatarId: String) {
        profilePrefs.saveProfile(name, email, avatarId)
        _uiState.value = EditProfileUiState(
            name = name,
            email = email,
            avatar = Avatar.fromId(avatarId)
        )
        viewModelScope.launch {
            _uiEffect.emit(EditProfileScreenUIEffect.OnProfileUpdated)
        }
    }

    /**
     * Processes user UI events dispatched from the Edit Profile screen.
     *
     * @param event The [EditProfileScreenUIEvent] to process.
     */
    fun onEvent(event: EditProfileScreenUIEvent) {
        when (event) {
            is EditProfileScreenUIEvent.OnSaveClicked -> {
                saveProfile(event.name, event.email, event.avatarId)
            }
        }
    }
}
