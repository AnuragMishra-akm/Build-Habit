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
import androidx.compose.material.icons.filled.AccessTime
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.buildhabit.ui.viewmodel.HabitDetailViewModel
import java.time.LocalDate

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HabitDetailScreen(
    viewModel: HabitDetailViewModel,
    onBack: () -> Unit,
    onHabitDeleted: () -> Unit,
    isEditMode: Boolean = false,
    onEditDismiss: () -> Unit = {}
) {
    val uiState by viewModel.uiState.collectAsState()
    val habit = uiState.habit
    val context = androidx.compose.ui.platform.LocalContext.current

    if (habit == null) {
        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            CircularProgressIndicator()
        }
        return
    }

    var showDeleteWarning by remember { mutableStateOf(false) }
    
    if (showDeleteWarning) {
        AlertDialog(
            onDismissRequest = { showDeleteWarning = false },
            title = { Text("Delete Habit") },
            text = { Text("Are you sure you want to delete \"${habit.name}\"? All history will be lost.") },
            confirmButton = {
                TextButton(onClick = {
                    com.example.buildhabit.util.NotificationHelper.cancelReminder(context, habit.id)
                    viewModel.deleteHabit()
                    showDeleteWarning = false
                    onHabitDeleted()
                }) {
                    Text("Delete", color = Color.Red)
                }
            },
            dismissButton = {
                TextButton(onClick = { showDeleteWarning = false }) {
                    Text("Cancel")
                }
            }
        )
    }

    if (isEditMode) {
        var editedName by remember { mutableStateOf(habit.name) }
        var editedTime by remember { mutableStateOf(habit.reminderTime ?: "09:00 PM") }
        var showTimePicker by remember { mutableStateOf(false) }

        if (showTimePicker) {
            val timePickerState = rememberTimePickerState()
            AlertDialog(
                onDismissRequest = { showTimePicker = false },
                confirmButton = {
                    TextButton(onClick = {
                        val calendar = java.util.Calendar.getInstance()
                        calendar.set(java.util.Calendar.HOUR_OF_DAY, timePickerState.hour)
                        calendar.set(java.util.Calendar.MINUTE, timePickerState.minute)
                        val timeFormat = java.text.SimpleDateFormat("hh:mm a", java.util.Locale.US)
                        editedTime = timeFormat.format(calendar.time)
                        showTimePicker = false
                    }) { Text("OK") }
                },
                dismissButton = { TextButton(onClick = { showTimePicker = false }) { Text("Cancel") } },
                text = { TimePicker(state = timePickerState) }
            )
        }

        AlertDialog(
            onDismissRequest = onEditDismiss,
            title = { Text("Edit Habit") },
            text = {
                Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                    OutlinedTextField(
                        value = editedName,
                        onValueChange = { editedName = it },
                        label = { Text("Name") }
                    )
                    OutlinedTextField(
                        value = editedTime,
                        onValueChange = {},
                        readOnly = true,
                        label = { Text("Reminder") },
                        trailingIcon = {
                            IconButton(onClick = { showTimePicker = true }) {
                                Icon(Icons.Default.AccessTime, contentDescription = null)
                            }
                        }
                    )
                }
            },
            confirmButton = {
                Button(onClick = {
                    viewModel.updateHabit(editedName, editedTime, habit.colorHex)
                    com.example.buildhabit.util.NotificationHelper.scheduleReminder(context, habit.id, editedName, editedTime)
                    onEditDismiss()
                }) { Text("Save") }
            },
            dismissButton = { TextButton(onClick = onEditDismiss) { Text("Cancel") } }
        )
    }

    val habitColor = Color(android.graphics.Color.parseColor(habit.colorHex))
    val completionRate = if (uiState.completions.isNotEmpty()) {
        val daysSinceCreated = java.time.Duration.between(
            java.time.Instant.ofEpochMilli(habit.createdAt).atZone(java.time.ZoneId.systemDefault()).toLocalDate().atStartOfDay(),
            java.time.LocalDate.now().atStartOfDay()
        ).toDays() + 1
        "${(uiState.completions.size * 100 / daysSinceCreated).toInt()}%"
    } else "0%"

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
                Text(habit.name, style = MaterialTheme.typography.displayLarge.copy(fontSize = 36.sp, fontWeight = FontWeight.Bold), color = Color.White)
                
                Spacer(modifier = Modifier.height(24.dp))
                Row(verticalAlignment = Alignment.Bottom) {
                    Text(habit.currentStreak.toString(), style = MaterialTheme.typography.displayLarge.copy(fontSize = 56.sp, fontWeight = FontWeight.Bold), color = Color.White)
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

        // Consistency Grid Card (90 days, 18 columns, 5 rows)
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
                
                LazyVerticalGrid(
                    columns = GridCells.Fixed(18),
                    modifier = Modifier.height(110.dp),
                    horizontalArrangement = Arrangement.spacedBy(4.dp),
                    verticalArrangement = Arrangement.spacedBy(4.dp),
                    userScrollEnabled = false
                ) {
                    items(90) { index ->
                        val date = LocalDate.now().minusDays((89 - index).toLong())
                        val isCompleted = uiState.completions.any { it.completedDate == date.toString() }
                        Box(
                            modifier = Modifier
                                .aspectRatio(1f)
                                .background(
                                    if (isCompleted) habitColor else habitColor.copy(alpha = 0.1f),
                                    RoundedCornerShape(3.dp)
                                )
                        )
                    }
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
                    Text("${habit.bestStreak} Days", style = MaterialTheme.typography.headlineSmall, fontWeight = FontWeight.Bold)
                }
            }
        }

        Spacer(modifier = Modifier.weight(1f))

        Button(
            onClick = { viewModel.toggleCompletion() },
            modifier = Modifier.fillMaxWidth().height(64.dp),
            shape = RoundedCornerShape(18.dp),
            colors = ButtonDefaults.buttonColors(containerColor = if (uiState.isCompletedToday) Color(0xFF22C55E) else MaterialTheme.colorScheme.primary)
        ) {
            Icon(if (uiState.isCompletedToday) Icons.Default.CheckCircle else Icons.Default.CheckCircle, contentDescription = null)
            Spacer(modifier = Modifier.width(8.dp))
            Text(if (uiState.isCompletedToday) "Completed Today" else "Mark Completed", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
        }

        OutlinedButton(
            onClick = { showDeleteWarning = true },
            modifier = Modifier.fillMaxWidth().height(60.dp),
            shape = RoundedCornerShape(18.dp),
            colors = ButtonDefaults.outlinedButtonColors(contentColor = Color.Red)
        ) {
            Text("Delete Habit", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
        }
        
        Spacer(modifier = Modifier.height(8.dp))
    }
}
