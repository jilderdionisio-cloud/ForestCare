package com.plant.forestcare.ui.reminders

data class RemindersUiState(
    val isLoading: Boolean = true,
    val reminders: List<ReminderItemUi> = emptyList(),
    val errorMessage: String? = null
) {
    val isEmpty: Boolean
        get() = !isLoading && errorMessage == null && reminders.isEmpty()
}

data class ReminderItemUi(
    val id: String,
    val title: String,
    val type: String,
    val dueText: String,
    val isDue: Boolean
)
