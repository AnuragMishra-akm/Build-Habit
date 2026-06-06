package com.example.buildhabit.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.buildhabit.data.local.dao.HabitDao
import com.example.buildhabit.data.local.entity.Habit
import com.example.buildhabit.data.local.entity.HabitCompletion

@Database(
    entities = [Habit::class, HabitCompletion::class],
    version = 1,
    exportSchema = false
)
abstract class HabitDatabase : RoomDatabase() {
    abstract fun habitDao(): HabitDao

    companion object {
        const val DATABASE_NAME = "habit_db"
    }
}
