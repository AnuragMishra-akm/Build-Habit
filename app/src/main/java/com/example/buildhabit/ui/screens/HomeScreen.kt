package com.example.buildhabit.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.*
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.buildhabit.ui.components.ConsistencyHeatmap
import com.example.buildhabit.ui.components.HabitCard
import com.example.buildhabit.ui.components.ProgressCard
import com.example.buildhabit.ui.viewmodel.HomeViewModel

@Composable
fun HomeScreen(
    viewModel: HomeViewModel,
    onSeeAllHabits: () -> Unit,
    onHabitClick: (Long) -> Unit
) {
    val uiState by viewModel.uiState.collectAsState()
    val today = java.time.LocalDate.now(java.time.ZoneId.of("Asia/Kolkata")).toString()

    Column(
        modifier = Modifier.fillMaxSize()
    ) {
        LazyColumn(
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth()
                .padding(horizontal = 20.dp),
            verticalArrangement = Arrangement.spacedBy(24.dp)
        ) {
            item {
                Column {
                    Spacer(modifier = Modifier.height(16.dp))
                    Text(
                        text = "Hello, Anurag 👋",
                        style = MaterialTheme.typography.bodyLarge,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                    Text(
                        text = "Ready for your discipline?",
                        style = MaterialTheme.typography.headlineSmall.copy(fontWeight = FontWeight.Bold)
                    )
                    Spacer(modifier = Modifier.height(24.dp))
                    ProgressCard(
                        streak = uiState.currentStreak,
                        completionRate = uiState.completionRate
                    )
                }
            }

            item {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "Ongoing Habits",
                        style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold)
                    )
                    TextButton(onClick = onSeeAllHabits) {
                        Text("See All Habits", style = MaterialTheme.typography.labelLarge)
                    }
                }
            }

            if (uiState.habits.isEmpty()) {
                item {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 40.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            "No ongoing habits found.",
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }
            } else {
                items(uiState.habits) { habit ->
                    val isCompleted = uiState.completions.any { it.habitId == habit.id && it.completedDate == today }
                    HabitCard(
                        name = habit.name,
                        stats = "${habit.currentStreak} Day Streak",
                        isCompletedToday = isCompleted,
                        onToggle = { viewModel.toggleCompletion(habit.id) },
                        onClick = { onHabitClick(habit.id) },
                        icon = getIconForHabit(habit.name),
                        iconBackgroundColor = Color(android.graphics.Color.parseColor(habit.colorHex))
                    )
                }
            }

            item {
                Spacer(modifier = Modifier.height(16.dp))
            }
        }
        
        // Fixed bottom tracker with minimal padding
        Surface(
            modifier = Modifier.fillMaxWidth(),
            color = MaterialTheme.colorScheme.background,
            tonalElevation = 2.dp
        ) {
            ConsistencyHeatmap(
                completions = uiState.completions.map { java.time.LocalDate.parse(it.completedDate) },
                totalHabitsPerDay = { date ->
                    // For the monthly tracker, we assume the current habit count applies to past days 
                    // unless we were to implement a much more complex historical snapshot system.
                    uiState.totalHabits 
                },
                completionsPerDay = { date ->
                    uiState.completions.count { it.completedDate == date.toString() }
                },
                modifier = Modifier
                    .padding(20.dp)
                    .padding(bottom = 8.dp)
            )
        }
    }
}

fun getIconForHabit(name: String): ImageVector {
    return when (name.lowercase()) {
        "exercise" -> Icons.Default.FitnessCenter
        "reading" -> Icons.AutoMirrored.Filled.MenuBook
        "meditation" -> Icons.Default.SelfImprovement
        "drink water" -> Icons.Default.WaterDrop
        "sleep early" -> Icons.Default.Bedtime
        "morning run" -> Icons.AutoMirrored.Filled.DirectionsRun
        "deep work" -> Icons.Default.Computer
        "journaling" -> Icons.Default.EditNote
        else -> Icons.Default.AutoAwesome
    }
}
