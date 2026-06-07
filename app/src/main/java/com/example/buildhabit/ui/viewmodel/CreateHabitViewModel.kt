package com.example.buildhabit.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.buildhabit.data.local.entity.Habit
import com.example.buildhabit.domain.repository.HabitRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

data class CreateHabitUiState(
    val name: String = "",
    val isDropdownExpanded: Boolean = false,
    val reminderTime: String = "09:00 PM",
    val selectedColor: String = "#2563EB",
    val isSaved: Boolean = false,
    val insertedId: Long? = null,
    val errorMessage: String? = null
)

val defaultHabits = listOf(
    "Exercise",
    "Read Books",
    "Meditation",
    "Drink Water",
    "Sleep Early",
    "Morning Run",
    "Deep Work",
    "Journaling"
)

class CreateHabitViewModel(
    private val repository: HabitRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(CreateHabitUiState())
    val uiState: StateFlow<CreateHabitUiState> = _uiState.asStateFlow()

    fun onNameChange(newName: String) {
        _uiState.value = _uiState.value.copy(name = newName, errorMessage = null)
    }

    fun onDropdownToggle(expanded: Boolean) {
        _uiState.value = _uiState.value.copy(isDropdownExpanded = expanded)
    }

    fun onReminderTimeChange(newTime: String) {
        _uiState.value = _uiState.value.copy(reminderTime = newTime)
    }

    fun onColorChange(newColor: String) {
        _uiState.value = _uiState.value.copy(selectedColor = newColor)
    }

    fun saveHabit() {
        val currentState = _uiState.value
        if (currentState.name.isBlank()) {
            _uiState.value = _uiState.value.copy(errorMessage = "Please enter a habit name")
            return
        }

        viewModelScope.launch {
            val id = repository.insertHabit(
                Habit(
                    name = currentState.name,
                    reminderTime = currentState.reminderTime,
                    colorHex = currentState.selectedColor
                )
            )
            _uiState.value = _uiState.value.copy(isSaved = true, insertedId = id)
        }
    }
}
