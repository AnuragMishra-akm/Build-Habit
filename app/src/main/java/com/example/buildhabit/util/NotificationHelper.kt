package com.example.buildhabit.util

import android.app.AlarmManager
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.os.Build
import android.util.Log
import androidx.core.app.NotificationCompat
import com.example.buildhabit.MainActivity
import java.util.*

private const val TAG = "HabitNotifications"

class HabitReminderReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent) {
        val habitName = intent.getStringExtra("HABIT_NAME") ?: "Habit"
        val habitId = intent.getLongExtra("HABIT_ID", -1L)
        val timeString = intent.getStringExtra("REMINDER_TIME")
        
        Log.d(TAG, "Received reminder for habit: $habitName (ID: $habitId)")
        
        showNotification(context, habitName, habitId)

        // Reschedule for the next day
        if (habitId != -1L && timeString != null) {
            NotificationHelper.scheduleReminder(context, habitId, habitName, timeString)
        }
    }

    private fun showNotification(context: Context, habitName: String, habitId: Long) {
        val notificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        val channelId = "habit_reminder_channel"

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                channelId, 
                "Habit Reminders", 
                NotificationManager.IMPORTANCE_HIGH
            ).apply {
                description = "Daily reminders for your habits"
                enableLights(true)
                enableVibration(true)
            }
            notificationManager.createNotificationChannel(channel)
        }

        val intent = Intent(context, MainActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        }
        val pendingIntent = PendingIntent.getActivity(
            context, habitId.toInt(), intent, 
            PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_UPDATE_CURRENT
        )

        val notification = NotificationCompat.Builder(context, channelId)
            .setSmallIcon(com.example.buildhabit.R.drawable.ic_launcher_foreground)
            .setContentTitle("Time for $habitName!")
            .setContentText("Don't forget to build your consistency today.")
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setCategory(NotificationCompat.CATEGORY_REMINDER)
            .setVisibility(NotificationCompat.VISIBILITY_PUBLIC)
            .setContentIntent(pendingIntent)
            .setAutoCancel(true)
            .build()

        notificationManager.notify(habitId.toInt(), notification)
    }
}

object NotificationHelper {
    fun scheduleReminder(context: Context, habitId: Long, habitName: String, timeString: String) {
        val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
        val intent = Intent(context, HabitReminderReceiver::class.java).apply {
            putExtra("HABIT_NAME", habitName)
            putExtra("HABIT_ID", habitId)
            putExtra("REMINDER_TIME", timeString)
        }
        val pendingIntent = PendingIntent.getBroadcast(
            context, habitId.toInt(), intent, 
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )

        val calendar = Calendar.getInstance(TimeZone.getTimeZone("Asia/Kolkata"))
        // Use Locale.US to ensure consistent parsing of AM/PM regardless of device language
        val sdf = java.text.SimpleDateFormat("hh:mm a", Locale.US).apply {
            timeZone = TimeZone.getTimeZone("Asia/Kolkata")
        }
        
        val date = try { sdf.parse(timeString) } catch (e: Exception) { 
            Log.e(TAG, "Failed to parse time string: $timeString", e)
            null 
        } ?: return

        val timeCalendar = Calendar.getInstance(TimeZone.getTimeZone("Asia/Kolkata")).apply { time = date }
        
        calendar.set(Calendar.HOUR_OF_DAY, timeCalendar.get(Calendar.HOUR_OF_DAY))
        calendar.set(Calendar.MINUTE, timeCalendar.get(Calendar.MINUTE))
        calendar.set(Calendar.SECOND, 0)
        calendar.set(Calendar.MILLISECOND, 0)

        if (calendar.timeInMillis <= System.currentTimeMillis()) {
            calendar.add(Calendar.DAY_OF_YEAR, 1)
        }

        Log.d(TAG, "Scheduling alarm for $habitName at ${calendar.time} (UTC: ${calendar.timeInMillis})")

        try {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
                if (alarmManager.canScheduleExactAlarms()) {
                    alarmManager.setExactAndAllowWhileIdle(
                        AlarmManager.RTC_WAKEUP, 
                        calendar.timeInMillis, 
                        pendingIntent
                    )
                    Log.d(TAG, "Exact alarm scheduled (S+)")
                } else {
                    alarmManager.setAndAllowWhileIdle(
                        AlarmManager.RTC_WAKEUP, 
                        calendar.timeInMillis, 
                        pendingIntent
                    )
                    Log.d(TAG, "Inexact alarm scheduled (can't schedule exact)")
                }
            } else {
                alarmManager.setExactAndAllowWhileIdle(
                    AlarmManager.RTC_WAKEUP, 
                    calendar.timeInMillis, 
                    pendingIntent
                )
                Log.d(TAG, "Exact alarm scheduled (<S)")
            }
        } catch (e: Exception) {
            Log.e(TAG, "Error scheduling alarm", e)
            alarmManager.set(AlarmManager.RTC_WAKEUP, calendar.timeInMillis, pendingIntent)
        }
    }

    fun cancelReminder(context: Context, habitId: Long) {
        val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
        val intent = Intent(context, HabitReminderReceiver::class.java)
        val pendingIntent = PendingIntent.getBroadcast(
            context, habitId.toInt(), intent, 
            PendingIntent.FLAG_NO_CREATE or PendingIntent.FLAG_IMMUTABLE
        )
        if (pendingIntent != null) {
            alarmManager.cancel(pendingIntent)
        }
    }
}
