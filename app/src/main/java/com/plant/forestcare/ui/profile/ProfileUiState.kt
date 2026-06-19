package com.plant.forestcare.ui.profile

data class ProfileUiState(
    val isLoading: Boolean = false,
    val title: String = "Perfil",
    val subtitle: String = "Tu espacio de cuidado personal",
    val preferencesTitle: String = "Preferencias",
    val preferencesDescription: String = "Próximamente podrás ajustar notificaciones, unidades y sincronización de cuenta.",
    val errorMessage: String? = null
) {
    val isEmpty: Boolean
        get() = !isLoading && errorMessage == null && title.isBlank()
}
