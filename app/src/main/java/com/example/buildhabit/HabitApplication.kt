package com.example.buildhabit

import android.app.Application
import androidx.room.Room
import com.example.buildhabit.data.local.HabitDatabase
import com.example.buildhabit.data.repository.HabitRepositoryImpl
import com.example.buildhabit.domain.repository.HabitRepository

class HabitApplication : Application() {

    lateinit var habitRepository: HabitRepository

    override fun onCreate() {
        super.onCreate()
        val database = Room.databaseBuilder(
            applicationContext,
            HabitDatabase::class.java,
            HabitDatabase.DATABASE_NAME
        ).build()
        
        habitRepository = HabitRepositoryImpl(database.habitDao())
    }
}
