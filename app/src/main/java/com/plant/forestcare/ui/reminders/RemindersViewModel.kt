package com.plant.forestcare.ui.reminders

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.plant.forestcare.data.PlantRepository
import com.plant.forestcare.data.local.ReminderEntity
import dagger.hilt.android.lifecycle.HiltViewModel
import java.util.concurrent.TimeUnit
import javax.inject.Inject
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

@HiltViewModel
class RemindersViewModel @Inject constructor(
    private val repository: PlantRepository
) : ViewModel() {

    val uiState: StateFlow<RemindersUiState> = repository.getPendingReminders()
        .map { reminders ->
            RemindersUiState(
                isLoading = false,
                reminders = reminders.map { it.toUi() }
            )
        }
        .catch { error ->
            emit(
                RemindersUiState(
                    isLoading = false,
                    errorMessage = error.message ?: "No se pudieron cargar los recordatorios"
                )
            )
        }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000),
            initialValue = RemindersUiState()
        )

    fun completeReminder(reminderId: String) {
        viewModelScope.launch {
            runCatching { repository.completeReminder(reminderId) }
        }
    }

    fun postponeReminder(reminderId: String) {
        viewModelScope.launch {
            runCatching { repository.postponeReminder(reminderId) }
        }
    }

    fun skipReminder(reminderId: String) {
        viewModelScope.launch {
            runCatching { repository.skipReminder(reminderId) }
        }
    }
}

private fun ReminderEntity.toUi(): ReminderItemUi {
    return ReminderItemUi(
        id = id,
        title = title,
        type = type.toReadableType(),
        dueText = scheduledAt.toRelativeText(),
        isDue = scheduledAt <= System.currentTimeMillis()
    )
}

private fun String.toReadableType(): String {
    return when (this) {
        "watering" -> "Riego"
        "weekly_review" -> "Revisión"
        "revision_enfermedad" -> "Revisión"
        else -> replaceFirstChar { it.uppercase() }
    }
}

private fun Long.toRelativeText(): String {
    val diff = this - System.currentTimeMillis()
    if (diff <= 0L) return "Hoy"
    val days = TimeUnit.MILLISECONDS.toDays(diff).coerceAtLeast(1)
    return if (days == 1L) "Mañana" else "En $days días"
}
