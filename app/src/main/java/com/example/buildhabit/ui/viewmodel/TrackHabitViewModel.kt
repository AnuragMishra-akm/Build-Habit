package com.example.buildhabit.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.buildhabit.data.local.entity.Habit
import com.example.buildhabit.domain.repository.HabitRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import java.time.LocalDate

data class TrackHabitUiState(
    val habits: List<Habit> = emptyList(),
    val completedHabitIds: Set<Long> = emptySet(),
    val searchQuery: String = "",
    val completionPercentage: Int = 0
)

class TrackHabitViewModel(
    private val repository: HabitRepository
) : ViewModel() {

    private val _searchQuery = MutableStateFlow("")

    val uiState: StateFlow<TrackHabitUiState> = combine(
        repository.getAllHabitsFlow(),
        repository.getAllCompletionsFlow(),
        _searchQuery
    ) { habits, completions, query ->
        val today = LocalDate.now().toString()
        val completedToday = completions.filter { it.completedDate == today }.map { it.habitId }.toSet()
        val filteredHabits = if (query.isBlank()) {
            habits
        } else {
            habits.filter { it.name.contains(query, ignoreCase = true) }
        }

        TrackHabitUiState(
            habits = filteredHabits,
            completedHabitIds = completedToday,
            searchQuery = query,
            completionPercentage = if (habits.isNotEmpty()) (completedToday.size * 100) / habits.size else 0
        )
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = TrackHabitUiState()
    )

    fun onSearchQueryChange(newQuery: String) {
        _searchQuery.value = newQuery
    }

    fun toggleCompletion(habitId: Long) {
        viewModelScope.launch {
            repository.toggleHabitCompletion(habitId, LocalDate.now().toString())
        }
    }

    fun deleteHabit(habit: Habit) {
        viewModelScope.launch {
            repository.deleteHabit(habit)
        }
    }
}
