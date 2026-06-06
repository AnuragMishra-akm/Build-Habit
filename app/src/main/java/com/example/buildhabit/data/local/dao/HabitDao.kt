package com.example.buildhabit.data.local.dao

import androidx.room.*
import com.example.buildhabit.data.local.entity.Habit
import com.example.buildhabit.data.local.entity.HabitCompletion
import kotlinx.coroutines.flow.Flow

@Dao
interface HabitDao {

    // --- Habit CRUD Operations ---

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertHabit(habit: Habit): Long

    @Update
    suspend fun updateHabit(habit: Habit)

    @Delete
    suspend fun deleteHabit(habit: Habit)

    @Query("SELECT * FROM habits WHERE id = :id")
    suspend fun getHabitById(id: Long): Habit?

    @Query("SELECT * FROM habits WHERE id = :id")
    fun getHabitByIdFlow(id: Long): Flow<Habit?>

    @Query("SELECT * FROM habits ORDER BY createdAt DESC")
    fun getAllHabitsFlow(): Flow<List<Habit>>

    @Query("SELECT * FROM habits ORDER BY createdAt DESC")
    suspend fun getAllHabits(): List<Habit>

    // --- Habit Completion Operations ---

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertCompletion(completion: HabitCompletion): Long

    @Query("DELETE FROM habit_completions WHERE habitId = :habitId AND completedDate = :completedDate")
    suspend fun deleteCompletion(habitId: Long, completedDate: String): Int

    @Query("SELECT * FROM habit_completions WHERE habitId = :habitId ORDER BY completedDate ASC")
    fun getCompletionsForHabitFlow(habitId: Long): Flow<List<HabitCompletion>>

    @Query("SELECT * FROM habit_completions WHERE habitId = :habitId ORDER BY completedDate ASC")
    suspend fun getCompletionsForHabit(habitId: Long): List<HabitCompletion>

    @Query("SELECT * FROM habit_completions WHERE completedDate = :date")
    fun getCompletionsForDateFlow(date: String): Flow<List<HabitCompletion>>

    @Query("SELECT * FROM habit_completions WHERE completedDate = :date")
    suspend fun getCompletionsForDate(date: String): List<HabitCompletion>

    @Query("SELECT EXISTS(SELECT 1 FROM habit_completions WHERE habitId = :habitId AND completedDate = :date)")
    suspend fun isHabitCompletedOnDate(habitId: Long, date: String): Boolean

    @Query("SELECT * FROM habit_completions ORDER BY completedDate ASC")
    fun getAllCompletionsFlow(): Flow<List<HabitCompletion>>

    @Query("SELECT * FROM habit_completions ORDER BY completedDate ASC")
    suspend fun getAllCompletions(): List<HabitCompletion>
}
