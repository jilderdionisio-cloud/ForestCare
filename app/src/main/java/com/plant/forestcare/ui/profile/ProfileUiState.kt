package com.plant.forestcare.ui.profile

data class ProfileUiState(
    val isLoading: Boolean = false,
    val title: String = "Profile",
    val userName: String = "Elena Green",
    val email: String = "elena.grower@plantcare.com",
    val activePlants: Int = 12,
    val healthScore: Int = 85,
    val errorMessage: String? = null
) {
    val isEmpty: Boolean
        get() = !isLoading && errorMessage == null && title.isBlank()
}
