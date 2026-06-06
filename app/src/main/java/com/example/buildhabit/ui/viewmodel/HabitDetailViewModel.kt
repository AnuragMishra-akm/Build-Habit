package com.example.buildhabit.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.buildhabit.data.local.entity.Habit
import com.example.buildhabit.data.local.entity.HabitCompletion
import com.example.buildhabit.domain.repository.HabitRepository
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import java.time.LocalDate

data class HabitDetailUiState(
    val habit: Habit? = null,
    val completions: List<HabitCompletion> = emptyList(),
    val isCompletedToday: Boolean = false
)

@OptIn(ExperimentalCoroutinesApi::class)
class HabitDetailViewModel(
    private val repository: HabitRepository,
    private val habitId: Long
) : ViewModel() {

    private val _habitFlow = repository.getHabitByIdFlow(habitId)

    val uiState: StateFlow<HabitDetailUiState> = _habitFlow
        .filterNotNull()
        .flatMapLatest { habit ->
            repository.getCompletionsForHabitFlow(habit.id).map { completions ->
                val today = LocalDate.now().toString()
                HabitDetailUiState(
                    habit = habit,
                    completions = completions,
                    isCompletedToday = completions.any { it.completedDate == today }
                )
            }
        }.stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = HabitDetailUiState()
        )

    fun toggleCompletion() {
        viewModelScope.launch {
            repository.toggleHabitCompletion(habitId, LocalDate.now().toString())
        }
    }

    fun updateHabit(name: String, reminderTime: String, colorHex: String) {
        viewModelScope.launch {
            val currentHabit = uiState.value.habit ?: return@launch
            repository.updateHabit(
                currentHabit.copy(
                    name = name,
                    reminderTime = reminderTime,
                    colorHex = colorHex
                )
            )
        }
    }

    fun deleteHabit() {
        viewModelScope.launch {
            val currentHabit = uiState.value.habit ?: return@launch
            repository.deleteHabit(currentHabit)
        }
    }
}
