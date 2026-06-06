package com.example.buildhabit.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.buildhabit.domain.repository.HabitRepository

class ViewModelFactory(
    private val repository: HabitRepository,
    private val habitId: Long = -1L
) : ViewModelProvider.Factory {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return when {
            modelClass.isAssignableFrom(HomeViewModel::class.java) -> {
                HomeViewModel(repository) as T
            }
            modelClass.isAssignableFrom(CreateHabitViewModel::class.java) -> {
                CreateHabitViewModel(repository) as T
            }
            modelClass.isAssignableFrom(TrackHabitViewModel::class.java) -> {
                TrackHabitViewModel(repository) as T
            }
            modelClass.isAssignableFrom(HabitDetailViewModel::class.java) -> {
                HabitDetailViewModel(repository, habitId) as T
            }
            else -> throw IllegalArgumentException("Unknown ViewModel class: ${modelClass.name}")
        }
    }
}
