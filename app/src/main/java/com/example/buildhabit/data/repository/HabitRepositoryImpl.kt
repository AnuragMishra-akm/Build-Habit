package com.example.buildhabit.data.repository

import com.example.buildhabit.data.local.dao.HabitDao
import com.example.buildhabit.data.local.entity.Habit
import com.example.buildhabit.data.local.entity.HabitCompletion
import com.example.buildhabit.domain.repository.HabitRepository
import kotlinx.coroutines.flow.Flow

class HabitRepositoryImpl(
    private val habitDao: HabitDao
) : HabitRepository {

    override fun getAllHabitsFlow(): Flow<List<Habit>> = habitDao.getAllHabitsFlow()

    override suspend fun getAllHabits(): List<Habit> = habitDao.getAllHabits()

    override suspend fun getHabitById(id: Long): Habit? = habitDao.getHabitById(id)

    override fun getHabitByIdFlow(id: Long): Flow<Habit?> = habitDao.getHabitByIdFlow(id)

    override suspend fun insertHabit(habit: Habit): Long = habitDao.insertHabit(habit)

    override suspend fun updateHabit(habit: Habit) = habitDao.updateHabit(habit)

    override suspend fun deleteHabit(habit: Habit) = habitDao.deleteHabit(habit)

    override fun getCompletionsForHabitFlow(habitId: Long): Flow<List<HabitCompletion>> =
        habitDao.getCompletionsForHabitFlow(habitId)

    override suspend fun getCompletionsForHabit(habitId: Long): List<HabitCompletion> =
        habitDao.getCompletionsForHabit(habitId)

    override suspend fun toggleHabitCompletion(habitId: Long, date: String) {
        val isCompleted = habitDao.isHabitCompletedOnDate(habitId, date)
        if (isCompleted) {
            habitDao.deleteCompletion(habitId, date)
        } else {
            habitDao.insertCompletion(HabitCompletion(habitId = habitId, completedDate = date))
        }
        // After toggling, we should ideally update the streak.
        updateHabitStreak(habitId)
    }

    override suspend fun isHabitCompletedOnDate(habitId: Long, date: String): Boolean =
        habitDao.isHabitCompletedOnDate(habitId, date)

    override fun getAllCompletionsFlow(): Flow<List<HabitCompletion>> = 
        habitDao.getAllCompletionsFlow()

    private val zoneId = java.time.ZoneId.of("Asia/Kolkata")

    private suspend fun updateHabitStreak(habitId: Long) {
        val habit = habitDao.getHabitById(habitId) ?: return
        val completions = habitDao.getCompletionsForHabit(habitId)
            .map { it.completedDate }
            .toSet()
        
        val streak = calculateStreak(completions)
        val bestStreak = maxOf(habit.bestStreak, streak)
        
        habitDao.updateHabit(habit.copy(currentStreak = streak, bestStreak = bestStreak))
    }

    private fun calculateStreak(completions: Set<String>): Int {
        if (completions.isEmpty()) return 0
        
        val today = java.time.LocalDate.now(zoneId)
        var currentStreak = 0
        var checkDate = today
        
        // If not completed today, check if it was completed yesterday to continue the streak
        if (!completions.contains(today.toString())) {
            checkDate = today.minusDays(1)
        }
        
        while (completions.contains(checkDate.toString())) {
            currentStreak++
            checkDate = checkDate.minusDays(1)
        }
        
        return currentStreak
    }
}
