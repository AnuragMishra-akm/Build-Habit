package com.example.buildhabit.util

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import com.example.buildhabit.HabitApplication
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class RebootReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent) {
        if (intent.action == Intent.ACTION_BOOT_COMPLETED) {
            val repository = (context.applicationContext as HabitApplication).habitRepository
            
            CoroutineScope(Dispatchers.IO).launch {
                val habits = repository.getAllHabits()
                habits.forEach { habit ->
                    if (habit.reminderTime != null) {
                        NotificationHelper.scheduleReminder(
                            context, 
                            habit.id, 
                            habit.name, 
                            habit.reminderTime
                        )
                    }
                }
            }
        }
    }
}
