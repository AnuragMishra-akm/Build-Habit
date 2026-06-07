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
        
        createNotificationChannel()
    }

    private fun createNotificationChannel() {
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            val channelId = "habit_reminder_channel"
            val name = "Habit Reminders"
            val descriptionText = "Daily reminders for your habits"
            val importance = android.app.NotificationManager.IMPORTANCE_HIGH
            val channel = android.app.NotificationChannel(channelId, name, importance).apply {
                description = descriptionText
                enableLights(true)
                enableVibration(true)
            }
            val notificationManager = getSystemService(android.content.Context.NOTIFICATION_SERVICE) as android.app.NotificationManager
            notificationManager.createNotificationChannel(channel)
        }
    }
}
