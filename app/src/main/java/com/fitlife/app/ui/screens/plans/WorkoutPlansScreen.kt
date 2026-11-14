package com.fitlife.app.ui.screens.plans

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
import com.fitlife.app.data.local.entity.WorkoutPlanEntity
import com.fitlife.app.ui.theme.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun WorkoutPlansScreen(
    navController: NavController,
    viewModel: WorkoutPlansViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()
    var showAddDialog by remember { mutableStateOf(false) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Workout Plans", fontWeight = FontWeight.Bold) },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = MaterialTheme.colorScheme.surface)
            )
        },
        floatingActionButton = {
            FloatingActionButton(onClick = { showAddDialog = true }, containerColor = AccentPurple) {
                Icon(Icons.Default.Add, contentDescription = "Add Plan", tint = Color.White)
            }
        },
        containerColor = MaterialTheme.colorScheme.background
    ) { padding ->
        LazyColumn(
            modifier = Modifier.fillMaxSize().padding(padding),
            contentPadding = PaddingValues(16.dp, 8.dp, 16.dp, 100.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            if (uiState.activePlan != null) {
                item {
                    Text("✅ Active Plan", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
                    Spacer(modifier = Modifier.height(8.dp))
                    ActivePlanCard(plan = uiState.activePlan!!, onDeactivate = { viewModel.deactivatePlan(uiState.activePlan!!) })
                }
            }

            // Built-in plan templates
            item {
                Spacer(modifier = Modifier.height(8.dp))
                Text("🔥 Popular Plans", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
            }
            items(builtInPlans) { plan ->
                PlanTemplateCard(plan = plan, onActivate = { viewModel.activateBuiltInPlan(plan) })
            }

            item {
                Spacer(modifier = Modifier.height(8.dp))
                Text("📋 My Plans", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
            }
            if (uiState.allPlans.isEmpty()) {
                item {
                    EmptyPlanState(onAdd = { showAddDialog = true })
                }
            } else {
                items(uiState.allPlans, key = { it.id }) { plan ->
                    PlanCard(
                        plan = plan,
                        isActive = uiState.activePlan?.id == plan.id,
                        onActivate = { viewModel.setActivePlan(plan) },
                        onDelete = { viewModel.deletePlan(plan) }
                    )
                }
            }
        }
    }

    if (showAddDialog) {
        AddPlanDialog(
            onDismiss = { showAddDialog = false },
            onAdd = { name, desc, weeks, daysPerWeek, difficulty, goal ->
                viewModel.addPlan(name, desc, weeks, daysPerWeek, difficulty, goal)
                showAddDialog = false
            }
        )
    }
}

data class PlanTemplate(
    val name: String, val description: String, val durationWeeks: Int,
    val daysPerWeek: Int, val difficulty: String, val goal: String, val emoji: String
)

val builtInPlans = listOf(
    PlanTemplate("Beginner Full Body", "3-day full body workouts for beginners", 8, 3, "Beginner", "Weight Loss", "🌱"),
    PlanTemplate("PPL Strength", "Push/Pull/Legs split for muscle gain", 12, 6, "Intermediate", "Muscle Gain", "💪"),
    PlanTemplate("HIIT Shred", "High-intensity interval training for fat loss", 6, 5, "Advanced", "Weight Loss", "🔥"),
    PlanTemplate("5x5 StrongLifts", "Classic strength building 5x5 program", 12, 3, "Intermediate", "Muscle Gain", "🏋️"),
    PlanTemplate("Marathon Prep", "16-week marathon training plan", 16, 5, "Advanced", "Endurance", "🏃"),
    PlanTemplate("Yoga Flow", "Daily yoga and flexibility routine", 4, 7, "Beginner", "Flexibility", "🧘")
)

@Composable
fun ActivePlanCard(plan: WorkoutPlanEntity, onDeactivate: () -> Unit) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = AccentPurple.copy(alpha = 0.15f))
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(Icons.Default.PlayCircle, contentDescription = null, tint = AccentPurple, modifier = Modifier.size(32.dp))
                Spacer(modifier = Modifier.width(12.dp))
                Column(modifier = Modifier.weight(1f)) {
                    Text(plan.name, style = MaterialTheme.typography.titleSmall, fontWeight = FontWeight.Bold)
                    Text("${plan.durationWeeks} weeks • ${plan.daysPerWeek} days/week • ${plan.difficulty}", style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
                }
                TextButton(onClick = onDeactivate) { Text("Pause", color = ColorError) }
            }
            Spacer(modifier = Modifier.height(8.dp))
            Text(plan.description, style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
        }
    }
}

@Composable
fun PlanTemplateCard(plan: PlanTemplate, onActivate: () -> Unit) {
    val difficultyColor = when (plan.difficulty) {
        "Beginner" -> AccentGreen
        "Intermediate" -> AccentOrange
        "Advanced" -> ColorError
        else -> AccentBlue
    }
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
    ) {
        Row(modifier = Modifier.padding(16.dp), verticalAlignment = Alignment.CenterVertically) {
            Text(plan.emoji, style = MaterialTheme.typography.headlineMedium)
            Spacer(modifier = Modifier.width(12.dp))
            Column(modifier = Modifier.weight(1f)) {
                Text(plan.name, style = MaterialTheme.typography.titleSmall, fontWeight = FontWeight.SemiBold)
                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    Surface(shape = RoundedCornerShape(4.dp), color = difficultyColor.copy(alpha = 0.15f)) {
                        Text(plan.difficulty, modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp), style = MaterialTheme.typography.labelSmall, color = difficultyColor)
                    }
                    Surface(shape = RoundedCornerShape(4.dp), color = AccentPurple.copy(alpha = 0.15f)) {
                        Text(plan.goal, modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp), style = MaterialTheme.typography.labelSmall, color = AccentPurple)
                    }
                }
                Text("${plan.durationWeeks}wk • ${plan.daysPerWeek}d/wk", style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
                Text(plan.description, style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant, maxLines = 1)
            }
            OutlinedButton(onClick = onActivate) { Text("Start") }
        }
    }
}

@Composable
fun PlanCard(plan: WorkoutPlanEntity, isActive: Boolean, onActivate: () -> Unit, onDelete: () -> Unit) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
    ) {
        Row(modifier = Modifier.padding(16.dp), verticalAlignment = Alignment.CenterVertically) {
            Icon(Icons.Default.CalendarMonth, contentDescription = null, tint = AccentPurple, modifier = Modifier.size(36.dp))
            Spacer(modifier = Modifier.width(12.dp))
            Column(modifier = Modifier.weight(1f)) {
                Text(plan.name, style = MaterialTheme.typography.titleSmall, fontWeight = FontWeight.SemiBold)
                Text("${plan.durationWeeks}wk • ${plan.daysPerWeek}d/wk • ${plan.difficulty}", style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
            }
            if (!isActive) {
                TextButton(onClick = onActivate) { Text("Activate", color = AccentPurple) }
            } else {
                Surface(shape = RoundedCornerShape(8.dp), color = AccentGreen.copy(alpha = 0.15f)) {
                    Text("Active", modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp), style = MaterialTheme.typography.labelSmall, color = AccentGreen)
                }
            }
            IconButton(onClick = onDelete) {
                Icon(Icons.Default.Delete, contentDescription = "Delete", tint = ColorError.copy(alpha = 0.7f))
            }
        }
    }
}

@Composable
fun EmptyPlanState(onAdd: () -> Unit) {
    Column(
        modifier = Modifier.fillMaxWidth().padding(32.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Icon(Icons.Default.CalendarMonth, contentDescription = null, modifier = Modifier.size(64.dp), tint = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.4f))
        Spacer(modifier = Modifier.height(16.dp))
        Text("No custom plans yet", style = MaterialTheme.typography.titleMedium, color = MaterialTheme.colorScheme.onSurfaceVariant)
        Spacer(modifier = Modifier.height(16.dp))
        Button(onClick = onAdd, colors = ButtonDefaults.buttonColors(containerColor = AccentPurple)) { Text("Create Plan") }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddPlanDialog(
    onDismiss: () -> Unit,
    onAdd: (String, String, Int, Int, String, String) -> Unit
) {
    var name by remember { mutableStateOf("") }
    var desc by remember { mutableStateOf("") }
    var weeks by remember { mutableStateOf("8") }
    var daysPerWeek by remember { mutableStateOf("3") }
    var difficulty by remember { mutableStateOf("Beginner") }
    var goal by remember { mutableStateOf("Weight Loss") }
    val difficulties = listOf("Beginner", "Intermediate", "Advanced")
    val goals = listOf("Weight Loss", "Muscle Gain", "Endurance", "Flexibility")
    var diffExpanded by remember { mutableStateOf(false) }
    var goalExpanded by remember { mutableStateOf(false) }

    AlertDialog(
        onDismissRequest = onDismiss,
        containerColor = MaterialTheme.colorScheme.surface,
        title = { Text("Create Plan", fontWeight = FontWeight.Bold) },
        text = {
            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                OutlinedTextField(value = name, onValueChange = { name = it }, label = { Text("Plan Name") }, modifier = Modifier.fillMaxWidth(), singleLine = true)
                OutlinedTextField(value = desc, onValueChange = { desc = it }, label = { Text("Description") }, modifier = Modifier.fillMaxWidth(), maxLines = 2)
                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    OutlinedTextField(value = weeks, onValueChange = { weeks = it }, label = { Text("Weeks") }, modifier = Modifier.weight(1f), singleLine = true)
                    OutlinedTextField(value = daysPerWeek, onValueChange = { daysPerWeek = it }, label = { Text("Days/Week") }, modifier = Modifier.weight(1f), singleLine = true)
                }
                ExposedDropdownMenuBox(expanded = diffExpanded, onExpandedChange = { diffExpanded = it }) {
                    OutlinedTextField(value = difficulty, onValueChange = {}, readOnly = true, label = { Text("Difficulty") }, trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(diffExpanded) }, modifier = Modifier.menuAnchor().fillMaxWidth())
                    ExposedDropdownMenu(expanded = diffExpanded, onDismissRequest = { diffExpanded = false }) {
                        difficulties.forEach { DropdownMenuItem(text = { Text(it) }, onClick = { difficulty = it; diffExpanded = false }) }
                    }
                }
                ExposedDropdownMenuBox(expanded = goalExpanded, onExpandedChange = { goalExpanded = it }) {
                    OutlinedTextField(value = goal, onValueChange = {}, readOnly = true, label = { Text("Goal") }, trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(goalExpanded) }, modifier = Modifier.menuAnchor().fillMaxWidth())
                    ExposedDropdownMenu(expanded = goalExpanded, onDismissRequest = { goalExpanded = false }) {
                        goals.forEach { DropdownMenuItem(text = { Text(it) }, onClick = { goal = it; goalExpanded = false }) }
                    }
                }
            }
        },
        confirmButton = {
            Button(onClick = { if (name.isNotBlank()) onAdd(name, desc, weeks.toIntOrNull() ?: 8, daysPerWeek.toIntOrNull() ?: 3, difficulty, goal) }) { Text("Create") }
        },
        dismissButton = { TextButton(onClick = onDismiss) { Text("Cancel") } }
    )
}
