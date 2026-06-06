package com.example.buildhabit.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
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
import com.example.buildhabit.ui.components.ConsistencyHeatmap
import com.example.buildhabit.ui.components.HabitCard
import com.example.buildhabit.ui.viewmodel.TrackHabitViewModel

@Composable
fun TrackHabitScreen(
    viewModel: TrackHabitViewModel,
    onHabitClick: (Long) -> Unit
) {
    val uiState by viewModel.uiState.collectAsState()

    val mockHabits = listOf(
        MockTrackHabit(1, "Read Books", "🔥 25 Days", Icons.Default.MenuBook, Color(0xFF6366F1), true),
        MockTrackHabit(2, "Morning Run", "🔥 12 Days", Icons.Default.DirectionsRun, Color(0xFF10B981), false),
        MockTrackHabit(3, "Meditate", "🔥 48 Days", Icons.Default.SelfImprovement, Color(0xFF8B5CF6), true),
        MockTrackHabit(4, "Deep Work", "🔥 7 Days", Icons.Default.Computer, Color(0xFF334155), false)
    )

    Column(
        modifier = Modifier.fillMaxSize()
    ) {
        // Space between top bar and search bar
        Spacer(modifier = Modifier.height(12.dp)) // Reduced padding as requested
        
        Column(modifier = Modifier.padding(horizontal = 20.dp)) {
            OutlinedTextField(
                value = uiState.searchQuery,
                onValueChange = viewModel::onSearchQueryChange,
                modifier = Modifier.fillMaxWidth(),
                placeholder = { Text("Search habits...") },
                leadingIcon = { Icon(Icons.Default.Search, contentDescription = null) },
                shape = RoundedCornerShape(16.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    unfocusedContainerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f),
                    focusedContainerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f),
                    unfocusedBorderColor = Color.Transparent,
                    focusedBorderColor = MaterialTheme.colorScheme.primary
                )
            )

            Spacer(modifier = Modifier.height(24.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Daily Rituals",
                    style = MaterialTheme.typography.headlineSmall.copy(fontWeight = FontWeight.Bold)
                )
                Text(
                    text = "80% Done",
                    color = Color(0xFF22C55E),
                    style = MaterialTheme.typography.labelLarge,
                    fontWeight = FontWeight.Bold
                )
            }

            Spacer(modifier = Modifier.height(16.dp))
        }

        LazyColumn(
            verticalArrangement = Arrangement.spacedBy(16.dp),
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth()
                .padding(horizontal = 20.dp)
        ) {
            items(mockHabits) { habit ->
                HabitCard(
                    name = habit.name,
                    stats = "${habit.streak} · ${if (habit.isCompleted) "Completed" else "Pending"}",
                    isCompletedToday = habit.isCompleted,
                    onToggle = { /* Mock */ },
                    onClick = { onHabitClick(habit.id) },
                    icon = habit.icon,
                    iconBackgroundColor = habit.color,
                    onDelete = { /* Mock */ }
                )
            }
            
            item {
                Spacer(modifier = Modifier.height(16.dp))
            }
        }

        // Fixed bottom heatmap with minimal padding
        Surface(
            modifier = Modifier.fillMaxWidth(),
            color = MaterialTheme.colorScheme.background,
            tonalElevation = 2.dp
        ) {
            Column(modifier = Modifier.padding(start = 20.dp, end = 20.dp, top = 8.dp, bottom = 8.dp)) {
                ConsistencyHeatmapCard()
            }
        }
    }
}

@Composable
fun ConsistencyHeatmapCard() {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(24.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant)
    ) {
        Column(modifier = Modifier.padding(20.dp)) {
            Text(
                text = "Consistency Heatmap",
                style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold)
            )
            Spacer(modifier = Modifier.height(16.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                val days = listOf("M", "T", "W", "T", "F", "S", "S")
                days.forEach { day ->
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Surface(
                            modifier = Modifier.size(36.dp),
                            shape = RoundedCornerShape(10.dp),
                            color = Color(0xFF22C55E).copy(alpha = 0.15f)
                        ) {
                            Box(contentAlignment = Alignment.Center) {
                                Text(day, style = MaterialTheme.typography.labelSmall, fontWeight = FontWeight.Bold, color = Color(0xFF22C55E))
                            }
                        }
                        Spacer(modifier = Modifier.height(8.dp))
                        Box(
                            modifier = Modifier
                                .size(36.dp)
                                .background(if (day == "T") Color(0xFF1E293B) else Color(0xFF22C55E), RoundedCornerShape(10.dp))
                        )
                    }
                }
            }
        Spacer(modifier = Modifier.height(16.dp)) // Restored small padding
            Text(
                text = "You've reached your goals 5 days this week. Keep it up!",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}

data class MockTrackHabit(
    val id: Long,
    val name: String,
    val streak: String,
    val icon: ImageVector,
    val color: Color,
    val isCompleted: Boolean
)
