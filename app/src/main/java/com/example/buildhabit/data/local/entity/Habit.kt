package com.example.buildhabit.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "habits")
data class Habit(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val name: String,
    val reminderTime: String?, // "HH:mm" format or null if no reminder
    val colorHex: String,      // personalized calendar chip color (e.g. "#FF0000" or custom color)
    val createdAt: Long = System.currentTimeMillis(),
    val currentStreak: Int = 0,
    val bestStreak: Int = 0
)
