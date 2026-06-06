package com.example.buildhabit.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.CalendarMonth
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.buildhabit.ui.viewmodel.HabitDetailViewModel

@Composable
fun HabitDetailScreen(
    viewModel: HabitDetailViewModel,
    onHabitDeleted: () -> Unit
) {
    val uiState by viewModel.uiState.collectAsState()
    
    // Mock Data for the prototype
    val habitName = "Exercise"
    val habitColor = Color(0xFF2563EB)
    val currentStreak = 25
    val bestStreak = 42
    val completionRate = "94%"
    val isCompletedToday = true

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 20.dp),
        verticalArrangement = Arrangement.spacedBy(20.dp)
    ) {
        Spacer(modifier = Modifier.height(8.dp))
        
        // Top Summary Card
        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(28.dp),
            colors = CardDefaults.cardColors(containerColor = habitColor)
        ) {
            Column(modifier = Modifier.padding(24.dp)) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text("ACTIVE HABIT", style = MaterialTheme.typography.labelSmall, color = Color.White.copy(alpha = 0.8f), fontWeight = FontWeight.Bold)
                    Surface(
                        color = Color.White.copy(alpha = 0.2f),
                        shape = RoundedCornerShape(8.dp)
                    ) {
                        Text(
                            "DAILY",
                            modifier = Modifier.padding(horizontal = 8.dp, vertical = 2.dp),
                            style = MaterialTheme.typography.labelSmall,
                            color = Color.White,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
                
                Spacer(modifier = Modifier.height(8.dp))
                Text(habitName, style = MaterialTheme.typography.displayLarge.copy(fontSize = 36.sp, fontWeight = FontWeight.Bold), color = Color.White)
                
                Spacer(modifier = Modifier.height(24.dp))
                Row(verticalAlignment = Alignment.Bottom) {
                    Text(currentStreak.toString(), style = MaterialTheme.typography.displayLarge.copy(fontSize = 56.sp, fontWeight = FontWeight.Bold), color = Color.White)
                    Spacer(modifier = Modifier.width(12.dp))
                    Column(modifier = Modifier.padding(bottom = 8.dp)) {
                        Text("DAY STREAK", style = MaterialTheme.typography.labelSmall, color = Color.White.copy(alpha = 0.8f), fontWeight = FontWeight.Bold)
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(Icons.Default.CheckCircle, contentDescription = null, tint = Color(0xFF22C55E), modifier = Modifier.size(12.dp))
                            Spacer(modifier = Modifier.width(4.dp))
                            Text("TOP 5% GLOBALLY", style = MaterialTheme.typography.labelSmall, color = Color(0xFF22C55E), fontWeight = FontWeight.Bold)
                        }
                    }
                }
            }
        }

        // Consistency / Calendar Card
        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(24.dp),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant)
        ) {
            Column(modifier = Modifier.padding(20.dp)) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text("Consistency", style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold))
                    Text("Last 90 Days", style = MaterialTheme.typography.labelSmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
                }
                Spacer(modifier = Modifier.height(20.dp))
                
                // GitHub-style grid
                LazyVerticalGrid(
                    columns = GridCells.Fixed(16),
                    modifier = Modifier.height(110.dp),
                    horizontalArrangement = Arrangement.spacedBy(4.dp),
                    verticalArrangement = Arrangement.spacedBy(4.dp),
                    userScrollEnabled = false
                ) {
                    items(80) { index ->
                        val alpha = when {
                            index % 7 == 0 -> 1f
                            index % 5 == 0 -> 0.7f
                            index % 3 == 0 -> 0.4f
                            else -> 0.1f
                        }
                        Box(
                            modifier = Modifier
                                .aspectRatio(1f)
                                .background(habitColor.copy(alpha = alpha), RoundedCornerShape(3.dp))
                        )
                    }
                }
                
                Spacer(modifier = Modifier.height(16.dp))
                Row(
                    modifier = Modifier.align(Alignment.End),
                    horizontalArrangement = Arrangement.spacedBy(6.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text("Less", style = MaterialTheme.typography.labelSmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
                    Box(modifier = Modifier.size(12.dp).background(habitColor.copy(alpha = 0.1f), RoundedCornerShape(2.dp)))
                    Box(modifier = Modifier.size(12.dp).background(habitColor.copy(alpha = 0.4f), RoundedCornerShape(2.dp)))
                    Box(modifier = Modifier.size(12.dp).background(habitColor.copy(alpha = 0.7f), RoundedCornerShape(2.dp)))
                    Box(modifier = Modifier.size(12.dp).background(habitColor, RoundedCornerShape(2.dp)))
                    Text("More", style = MaterialTheme.typography.labelSmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
                }
            }
        }

        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(16.dp)) {
            Card(
                modifier = Modifier.weight(1f),
                shape = RoundedCornerShape(20.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Icon(Icons.Default.CalendarMonth, contentDescription = null, tint = Color(0xFF6366F1), modifier = Modifier.size(24.dp))
                    Spacer(modifier = Modifier.height(12.dp))
                    Text("COMPLETION RATE", style = MaterialTheme.typography.labelSmall, color = MaterialTheme.colorScheme.onSurfaceVariant, fontWeight = FontWeight.Bold)
                    Text(completionRate, style = MaterialTheme.typography.headlineSmall, fontWeight = FontWeight.Bold)
                }
            }
            Card(
                modifier = Modifier.weight(1f),
                shape = RoundedCornerShape(20.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Icon(Icons.Default.Star, contentDescription = null, tint = Color(0xFF22C55E), modifier = Modifier.size(24.dp))
                    Spacer(modifier = Modifier.height(12.dp))
                    Text("BEST STREAK", style = MaterialTheme.typography.labelSmall, color = MaterialTheme.colorScheme.onSurfaceVariant, fontWeight = FontWeight.Bold)
                    Text("$bestStreak Days", style = MaterialTheme.typography.headlineSmall, fontWeight = FontWeight.Bold)
                }
            }
        }

        Spacer(modifier = Modifier.weight(1f))

        Button(
            onClick = { /* viewModel.toggleCompletion() */ },
            modifier = Modifier.fillMaxWidth().height(64.dp),
            shape = RoundedCornerShape(18.dp),
            colors = ButtonDefaults.buttonColors(containerColor = if (isCompletedToday) Color(0xFF22C55E) else MaterialTheme.colorScheme.primary)
        ) {
            Icon(if (isCompletedToday) Icons.Filled.CheckCircle else Icons.Default.CheckCircle, contentDescription = null)
            Spacer(modifier = Modifier.width(8.dp))
            Text(if (isCompletedToday) "Completed Today" else "Mark as Completed", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
        }

        OutlinedButton(
            onClick = { 
                viewModel.deleteHabit()
                onHabitDeleted()
            },
            modifier = Modifier.fillMaxWidth().height(60.dp),
            shape = RoundedCornerShape(18.dp),
            colors = ButtonDefaults.outlinedButtonColors(contentColor = Color.Red)
        ) {
            Text("Delete Habit", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
        }
        
        Spacer(modifier = Modifier.height(8.dp))
    }
}
