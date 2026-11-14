package com.fitlife.app.ui.screens.workout

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.fitlife.app.data.local.entity.WorkoutEntity
import com.fitlife.app.ui.theme.*
import java.text.SimpleDateFormat
import java.util.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun WorkoutScreen(
    navController: NavController,
    viewModel: WorkoutViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()
    var showAddDialog by remember { mutableStateOf(false) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Workouts", fontWeight = FontWeight.Bold) },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = MaterialTheme.colorScheme.surface)
            )
        },
        floatingActionButton = {
            FloatingActionButton(onClick = { showAddDialog = true }, containerColor = MaterialTheme.colorScheme.primary) {
                Icon(Icons.Default.Add, contentDescription = "Add Workout", tint = Color.White)
            }
        },
        containerColor = MaterialTheme.colorScheme.background
    ) { paddingValues ->
        LazyColumn(
            modifier = Modifier.fillMaxSize().padding(paddingValues),
            contentPadding = PaddingValues(16.dp, 8.dp, 16.dp, 100.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            item { CategoryFilterRow(selected = uiState.selectedCategory, onSelect = viewModel::selectCategory) }
            item {
                WeeklySummaryCard(
                    totalWorkouts = uiState.weeklyWorkouts,
                    totalCalories = uiState.weeklyCalories,
                    totalMinutes = uiState.weeklyMinutes
                )
            }
            if (uiState.workouts.isEmpty()) {
                item { EmptyWorkoutState(onAdd = { showAddDialog = true }) }
            } else {
                items(uiState.workouts, key = { it.id }) { workout ->
                    WorkoutCard(
                        workout = workout,
                        onToggleComplete = { viewModel.toggleWorkoutComplete(workout) },
                        onDelete = { viewModel.deleteWorkout(workout) }
                    )
                }
            }
        }
    }

    if (showAddDialog) {
        AddWorkoutDialog(
            onDismiss = { showAddDialog = false },
            onAdd = { name, category, duration, calories, notes ->
                viewModel.addWorkout(name, category, duration, calories, notes)
                showAddDialog = false
            }
        )
    }
}

@Composable
fun CategoryFilterRow(selected: String, onSelect: (String) -> Unit) {
    val categories = listOf("All", "Strength", "Cardio", "HIIT", "Flexibility")
    val colors = mapOf(
        "All" to Primary,
        "Strength" to AccentOrange,
        "Cardio" to AccentBlue,
        "HIIT" to ColorError,
        "Flexibility" to AccentPurple
    )
    Row(horizontalArrangement = Arrangement.spacedBy(8.dp), modifier = Modifier.fillMaxWidth()) {
        categories.forEach { cat ->
            val color = colors[cat] ?: Primary
            FilterChip(
                selected = selected == cat,
                onClick = { onSelect(cat) },
                label = { Text(cat, style = MaterialTheme.typography.labelSmall) },
                colors = FilterChipDefaults.filterChipColors(
                    selectedContainerColor = color.copy(alpha = 0.2f),
                    selectedLabelColor = color
                )
            )
        }
    }
}

@Composable
fun WeeklySummaryCard(totalWorkouts: Int, totalCalories: Int, totalMinutes: Int) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
    ) {
        Row(
            modifier = Modifier.padding(16.dp).fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            SummaryItem("Workouts", totalWorkouts.toString(), AccentBlue)
            VerticalDivider(modifier = Modifier.height(40.dp), color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.1f))
            SummaryItem("Calories", "${totalCalories}kcal", AccentOrange)
            VerticalDivider(modifier = Modifier.height(40.dp), color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.1f))
            SummaryItem("Minutes", totalMinutes.toString(), AccentGreen)
        }
    }
}

@Composable
fun SummaryItem(label: String, value: String, color: Color) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Text(value, style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold, color = color)
        Text(label, style = MaterialTheme.typography.labelSmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
    }
}

@Composable
fun WorkoutCard(workout: WorkoutEntity, onToggleComplete: () -> Unit, onDelete: () -> Unit) {
    val sdf = SimpleDateFormat("MMM d, hh:mm a", Locale.getDefault())
    val categoryColor = when (workout.category) {
        "Strength" -> AccentOrange
        "Cardio" -> AccentBlue
        "HIIT" -> ColorError
        "Flexibility" -> AccentPurple
        else -> Primary
    }
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
    ) {
        Row(modifier = Modifier.padding(16.dp), verticalAlignment = Alignment.CenterVertically) {
            Box(
                modifier = Modifier.size(48.dp).background(categoryColor.copy(alpha = 0.15f), RoundedCornerShape(12.dp)),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = when (workout.category) {
                        "Cardio" -> Icons.Default.DirectionsRun
                        "Strength" -> Icons.Default.FitnessCenter
                        "HIIT" -> Icons.Default.Bolt
                        "Flexibility" -> Icons.Default.SelfImprovement
                        else -> Icons.Default.FitnessCenter
                    },
                    contentDescription = null,
                    tint = categoryColor,
                    modifier = Modifier.size(28.dp)
                )
            }
            Spacer(modifier = Modifier.width(12.dp))
            Column(modifier = Modifier.weight(1f)) {
                Text(workout.name, style = MaterialTheme.typography.titleSmall, fontWeight = FontWeight.SemiBold, color = MaterialTheme.colorScheme.onSurface)
                Text("${workout.durationMinutes} min • ${workout.caloriesBurned} kcal", style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
                Text(sdf.format(Date(workout.date)), style = MaterialTheme.typography.labelSmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
            }
            Checkbox(checked = workout.isCompleted, onCheckedChange = { onToggleComplete() }, colors = CheckboxDefaults.colors(checkedColor = AccentGreen))
            IconButton(onClick = onDelete) {
                Icon(Icons.Default.Delete, contentDescription = "Delete", tint = ColorError.copy(alpha = 0.7f))
            }
        }
    }
}

@Composable
fun EmptyWorkoutState(onAdd: () -> Unit) {
    Column(modifier = Modifier.fillMaxWidth().padding(32.dp), horizontalAlignment = Alignment.CenterHorizontally) {
        Icon(Icons.Default.FitnessCenter, contentDescription = null, modifier = Modifier.size(64.dp), tint = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.4f))
        Spacer(modifier = Modifier.height(16.dp))
        Text("No workouts yet", style = MaterialTheme.typography.titleMedium, color = MaterialTheme.colorScheme.onSurfaceVariant)
        Text("Start tracking your fitness journey!", style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
        Spacer(modifier = Modifier.height(16.dp))
        Button(onClick = onAdd) { Text("Log First Workout") }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddWorkoutDialog(onDismiss: () -> Unit, onAdd: (String, String, Int, Int, String) -> Unit) {
    var name by remember { mutableStateOf("") }
    var category by remember { mutableStateOf("Strength") }
    var duration by remember { mutableStateOf("30") }
    var calories by remember { mutableStateOf("200") }
    var notes by remember { mutableStateOf("") }
    val categories = listOf("Strength", "Cardio", "HIIT", "Flexibility")
    var expanded by remember { mutableStateOf(false) }

    AlertDialog(
        onDismissRequest = onDismiss,
        containerColor = MaterialTheme.colorScheme.surface,
        title = { Text("Log Workout", fontWeight = FontWeight.Bold) },
        text = {
            Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                OutlinedTextField(value = name, onValueChange = { name = it }, label = { Text("Workout Name") }, modifier = Modifier.fillMaxWidth(), singleLine = true)
                ExposedDropdownMenuBox(expanded = expanded, onExpandedChange = { expanded = it }) {
                    OutlinedTextField(
                        value = category,
                        onValueChange = {},
                        readOnly = true,
                        label = { Text("Category") },
                        trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded) },
                        modifier = Modifier.menuAnchor().fillMaxWidth()
                    )
                    ExposedDropdownMenu(expanded = expanded, onDismissRequest = { expanded = false }) {
                        categories.forEach { cat -> DropdownMenuItem(text = { Text(cat) }, onClick = { category = cat; expanded = false }) }
                    }
                }
                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    OutlinedTextField(value = duration, onValueChange = { duration = it.filter(Char::isDigit).take(3) }, label = { Text("Duration (min)") }, modifier = Modifier.weight(1f), singleLine = true)
                    OutlinedTextField(value = calories, onValueChange = { calories = it.filter(Char::isDigit).take(4) }, label = { Text("Calories") }, modifier = Modifier.weight(1f), singleLine = true)
                }
                OutlinedTextField(value = notes, onValueChange = { notes = it }, label = { Text("Notes (optional)") }, modifier = Modifier.fillMaxWidth(), maxLines = 2)
            }
        },
        confirmButton = {
            Button(onClick = {
                val minutes = duration.toIntOrNull()
                val burned = calories.toIntOrNull()
                if (name.isNotBlank() && minutes != null && minutes > 0 && burned != null) {
                    onAdd(name.trim(), category, minutes, burned, notes.trim())
                }
            }) { Text("Log Workout") }
        },
        dismissButton = { TextButton(onClick = onDismiss) { Text("Cancel") } }
    )
}
