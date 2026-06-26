package com.plant.forestcare.ui.profile

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class ProfileViewModel : ViewModel() {
    private val _uiState = MutableStateFlow(ProfileUiState())
    val uiState: StateFlow<ProfileUiState> = _uiState.asStateFlow()

    fun updateProfile(name: String, email: String) {
        _uiState.value = _uiState.value.copy(
            userName = name.ifBlank { _uiState.value.userName },
            email = email.ifBlank { _uiState.value.email }
        )
    }
}
