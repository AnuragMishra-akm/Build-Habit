package com.example.buildhabit.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.buildhabit.data.local.entity.Habit
import com.example.buildhabit.data.local.entity.HabitCompletion
import com.example.buildhabit.domain.repository.HabitRepository
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import java.time.LocalDate

data class HomeUiState(
    val habits: List<Habit> = emptyList(),
    val completions: List<HabitCompletion> = emptyList(),
    val totalHabits: Int = 0,
    val completedTodayCount: Int = 0,
    val completionRate: Float = 0f,
    val currentStreak: Int = 0
)

class HomeViewModel(
    private val repository: HabitRepository
) : ViewModel() {

    val uiState: StateFlow<HomeUiState> = combine(
        repository.getAllHabitsFlow(),
        repository.getAllCompletionsFlow()
    ) { habits, completions ->
        val today = LocalDate.now().toString()
        val completedToday = completions.filter { it.completedDate == today }.map { it.habitId }.toSet()
        
        HomeUiState(
            habits = habits.take(3), // Top 3 habits
            completions = completions,
            totalHabits = habits.size,
            completedTodayCount = completedToday.size,
            completionRate = if (habits.isNotEmpty()) completedToday.size.toFloat() / habits.size else 0f,
            currentStreak = habits.maxOfOrNull { it.currentStreak } ?: 0
        )
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = HomeUiState()
    )

    fun toggleCompletion(habitId: Long) {
        viewModelScope.launch {
            repository.toggleHabitCompletion(habitId, LocalDate.now().toString())
        }
    }
}
