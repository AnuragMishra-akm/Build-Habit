package com.example.buildhabit.domain.repository

import com.example.buildhabit.data.local.entity.Habit
import com.example.buildhabit.data.local.entity.HabitCompletion
import kotlinx.coroutines.flow.Flow

interface HabitRepository {
    fun getAllHabitsFlow(): Flow<List<Habit>>
    suspend fun getAllHabits(): List<Habit>
    suspend fun getHabitById(id: Long): Habit?
    fun getHabitByIdFlow(id: Long): Flow<Habit?>
    suspend fun insertHabit(habit: Habit): Long
    suspend fun updateHabit(habit: Habit)
    suspend fun deleteHabit(habit: Habit)

    fun getCompletionsForHabitFlow(habitId: Long): Flow<List<HabitCompletion>>
    suspend fun getCompletionsForHabit(habitId: Long): List<HabitCompletion>
    suspend fun toggleHabitCompletion(habitId: Long, date: String)
    suspend fun isHabitCompletedOnDate(habitId: Long, date: String): Boolean
    
    fun getAllCompletionsFlow(): Flow<List<HabitCompletion>>
}
