package com.example.buildhabit.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
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

    // Comprehensive Mock Data
    val mockHabits = listOf(
        MockHabit(1, "Exercise", "45 mins · Today", Icons.Default.FitnessCenter, Color(0xFF2563EB), 24, true),
        MockHabit(2, "Reading", "20 pages · Today", Icons.Default.MenuBook, Color(0xFF10B981), 12, false),
        MockHabit(3, "Meditation", "10 mins · Evening", Icons.Default.SelfImprovement, Color(0xFF8B5CF6), 48, false)
    )

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
                    Spacer(modifier = Modifier.height(16.dp)) // Restored small padding
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
                        streak = 24,
                        completionRate = 0.85f
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

            if (mockHabits.isEmpty()) {
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
                items(mockHabits) { habit ->
                    HabitCard(
                        name = habit.name,
                        stats = habit.stats,
                        isCompletedToday = habit.isCompleted,
                        onToggle = { /* Mock */ },
                        onClick = { onHabitClick(habit.id) },
                        icon = habit.icon,
                        iconBackgroundColor = habit.color
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
                modifier = Modifier
                    .padding(20.dp)
                    .padding(bottom = 8.dp) // Restored minimal padding
            )
        }
    }
}

data class MockHabit(
    val id: Long,
    val name: String,
    val stats: String,
    val icon: ImageVector,
    val color: Color,
    val streak: Int,
    val isCompleted: Boolean
)
