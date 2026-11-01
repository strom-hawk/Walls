package com.example.trynewthingsinandroid.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.shared.presentation.ContactUsScreenUIEvent
import com.example.shared.presentation.ContactUsUiState
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

/**
 * ViewModel managing state and UI effects for the Contact Us screen.
 */
class ContactUsScreenViewModel : ViewModel() {
    private val _uiState = MutableStateFlow(ContactUsUiState())
    val uiState: StateFlow<ContactUsUiState> = _uiState.asStateFlow()

    private val _uiEffect = MutableSharedFlow<ContactUsScreenUIEffect>()
    val uiEffect: SharedFlow<ContactUsScreenUIEffect> = _uiEffect.asSharedFlow()

    /**
     * Processes user UI events dispatched from the Contact Us screen.
     *
     * @param event The [ContactUsScreenUIEvent] to process.
     */
    fun onEvent(event: ContactUsScreenUIEvent) {
        when (event) {
            is ContactUsScreenUIEvent.OnEmailClicked -> {
                viewModelScope.launch {
                    _uiEffect.emit(ContactUsScreenUIEffect.OpenEmailClient(event.email, event.subject, event.message))
                }
            }
            is ContactUsScreenUIEvent.OnBackClicked -> {
                viewModelScope.launch {
                    _uiEffect.emit(ContactUsScreenUIEffect.NavigateBack)
                }
            }
        }
    }
}
