package com.example.buildhabit

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.example.buildhabit.ui.BuildHabitApp
import com.example.buildhabit.ui.theme.BuildHabitTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            BuildHabitTheme {
                BuildHabitApp()
            }
        }
    }
}
