package com.fitlife.app.ui.screens.diet

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
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.fitlife.app.data.local.entity.MealEntity
import com.fitlife.app.ui.theme.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DietScreen(
    navController: NavController,
    viewModel: DietViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()
    var showAddDialog by remember { mutableStateOf(false) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Diet Planner", fontWeight = FontWeight.Bold) },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = MaterialTheme.colorScheme.surface)
            )
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = { showAddDialog = true },
                containerColor = AccentGreen
            ) {
                Icon(Icons.Default.Add, contentDescription = "Add Meal", tint = Color.White)
            }
        },
        containerColor = MaterialTheme.colorScheme.background
    ) { padding ->
        LazyColumn(
            modifier = Modifier.fillMaxSize().padding(padding),
            contentPadding = PaddingValues(16.dp, 8.dp, 16.dp, 100.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            // Calorie summary ring
            item {
                CalorieSummaryCard(
                    consumed = uiState.totalCalories,
                    goal = uiState.calorieGoal,
                    burned = uiState.caloriesBurned
                )
            }

            // Macro breakdown
            item {
                MacroCard(
                    protein = uiState.totalProtein,
                    carbs = uiState.totalCarbs,
                    fat = uiState.totalFat
                )
            }

            // Meals by type
            val mealTypes = listOf("Breakfast", "Lunch", "Dinner", "Snack")
            mealTypes.forEach { type ->
                val typeMeals = uiState.meals.filter { it.mealType == type }
                if (typeMeals.isNotEmpty()) {
                    item {
                        MealTypeHeader(type = type, calories = typeMeals.sumOf { it.calories })
                    }
                    items(typeMeals, key = { it.id }) { meal ->
                        MealCard(meal = meal, onDelete = { viewModel.deleteMeal(meal) })
                    }
                }
            }

            if (uiState.meals.isEmpty()) {
                item {
                    EmptyDietState(onAdd = { showAddDialog = true })
                }
            }
        }
    }

    if (showAddDialog) {
        AddMealDialog(
            onDismiss = { showAddDialog = false },
            onAdd = { name, type, cal, protein, carbs, fat ->
                viewModel.addMeal(name, type, cal, protein, carbs, fat)
                showAddDialog = false
            }
        )
    }
}

@Composable
fun CalorieSummaryCard(consumed: Int, goal: Int, burned: Int) {
    val remaining = goal - consumed + burned
    val progress = (consumed.toFloat() / goal.toFloat()).coerceIn(0f, 1f)

    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text("Calorie Balance", style = MaterialTheme.typography.titleSmall, fontWeight = FontWeight.Bold)
            Spacer(modifier = Modifier.height(12.dp))
            LinearProgressIndicator(
                progress = { progress },
                modifier = Modifier.fillMaxWidth().height(8.dp).clip(RoundedCornerShape(4.dp)),
                color = if (progress < 0.8f) AccentGreen else if (progress < 1f) AccentOrange else ColorError,
                trackColor = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.1f)
            )
            Spacer(modifier = Modifier.height(12.dp))
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                CalorieItem("Consumed", "$consumed", AccentOrange)
                CalorieItem("Goal", "$goal", AccentBlue)
                CalorieItem("Burned", "$burned", AccentGreen)
                CalorieItem("Remaining", "$remaining", if (remaining >= 0) AccentGreen else ColorError)
            }
        }
    }
}

@Composable
fun CalorieItem(label: String, value: String, color: Color) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Text(value, style = MaterialTheme.typography.titleSmall, fontWeight = FontWeight.Bold, color = color)
        Text(label, style = MaterialTheme.typography.labelSmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
    }
}

@Composable
fun MacroCard(protein: Float, carbs: Float, fat: Float) {
    val total = protein + carbs + fat
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text("Macronutrients", style = MaterialTheme.typography.titleSmall, fontWeight = FontWeight.Bold)
            Spacer(modifier = Modifier.height(12.dp))
            MacroBar(label = "Protein", value = protein, total = total, color = AccentBlue, unit = "g")
            Spacer(modifier = Modifier.height(8.dp))
            MacroBar(label = "Carbs", value = carbs, total = total, color = AccentOrange, unit = "g")
            Spacer(modifier = Modifier.height(8.dp))
            MacroBar(label = "Fat", value = fat, total = total, color = AccentPurple, unit = "g")
        }
    }
}

@Composable
fun MacroBar(label: String, value: Float, total: Float, color: Color, unit: String) {
    val progress = if (total > 0) (value / total).coerceIn(0f, 1f) else 0f
    Row(verticalAlignment = Alignment.CenterVertically) {
        Text(label, modifier = Modifier.width(60.dp), style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
        LinearProgressIndicator(
            progress = { progress },
            modifier = Modifier.weight(1f).height(8.dp).clip(RoundedCornerShape(4.dp)),
            color = color,
            trackColor = color.copy(alpha = 0.15f)
        )
        Text(
            "${value.toInt()}$unit",
            modifier = Modifier.width(44.dp),
            style = MaterialTheme.typography.labelSmall,
            color = color,
            fontWeight = FontWeight.Bold
        )
    }
}

@Composable
fun MealTypeHeader(type: String, calories: Int) {
    Row(
        modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(type, style = MaterialTheme.typography.titleSmall, fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.onBackground)
        Text("$calories kcal", style = MaterialTheme.typography.labelMedium, color = AccentOrange)
    }
}

@Composable
fun MealCard(meal: MealEntity, onDelete: () -> Unit) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
    ) {
        Row(
            modifier = Modifier.padding(12.dp, 10.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(Icons.Default.Restaurant, contentDescription = null, tint = AccentGreen, modifier = Modifier.size(24.dp))
            Spacer(modifier = Modifier.width(12.dp))
            Column(modifier = Modifier.weight(1f)) {
                Text(meal.name, style = MaterialTheme.typography.titleSmall, fontWeight = FontWeight.SemiBold)
                Text(
                    "P:${meal.proteinG.toInt()}g C:${meal.carbsG.toInt()}g F:${meal.fatG.toInt()}g",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
            Text("${meal.calories} kcal", style = MaterialTheme.typography.titleSmall, fontWeight = FontWeight.Bold, color = AccentOrange)
            IconButton(onClick = onDelete) {
                Icon(Icons.Default.Delete, contentDescription = "Delete", tint = ColorError.copy(alpha = 0.7f))
            }
        }
    }
}

@Composable
fun EmptyDietState(onAdd: () -> Unit) {
    Column(
        modifier = Modifier.fillMaxWidth().padding(32.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Icon(Icons.Default.Restaurant, contentDescription = null, modifier = Modifier.size(64.dp), tint = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.4f))
        Spacer(modifier = Modifier.height(16.dp))
        Text("No meals logged today", style = MaterialTheme.typography.titleMedium, color = MaterialTheme.colorScheme.onSurfaceVariant)
        Spacer(modifier = Modifier.height(16.dp))
        Button(onClick = onAdd, colors = ButtonDefaults.buttonColors(containerColor = AccentGreen)) { Text("Log First Meal") }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddMealDialog(
    onDismiss: () -> Unit,
    onAdd: (String, String, Int, Float, Float, Float) -> Unit
) {
    var name by remember { mutableStateOf("") }
    var mealType by remember { mutableStateOf("Breakfast") }
    var calories by remember { mutableStateOf("") }
    var protein by remember { mutableStateOf("") }
    var carbs by remember { mutableStateOf("") }
    var fat by remember { mutableStateOf("") }
    val types = listOf("Breakfast", "Lunch", "Dinner", "Snack")
    var expanded by remember { mutableStateOf(false) }

    AlertDialog(
        onDismissRequest = onDismiss,
        containerColor = MaterialTheme.colorScheme.surface,
        title = { Text("Log Meal", fontWeight = FontWeight.Bold) },
        text = {
            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                OutlinedTextField(value = name, onValueChange = { name = it }, label = { Text("Food Name") }, modifier = Modifier.fillMaxWidth(), singleLine = true)
                ExposedDropdownMenuBox(expanded = expanded, onExpandedChange = { expanded = it }) {
                    OutlinedTextField(value = mealType, onValueChange = {}, readOnly = true, label = { Text("Meal Type") }, trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded) }, modifier = Modifier.menuAnchor().fillMaxWidth())
                    ExposedDropdownMenu(expanded = expanded, onDismissRequest = { expanded = false }) {
                        types.forEach { DropdownMenuItem(text = { Text(it) }, onClick = { mealType = it; expanded = false }) }
                    }
                }
                OutlinedTextField(value = calories, onValueChange = { calories = it }, label = { Text("Calories (kcal)") }, modifier = Modifier.fillMaxWidth(), singleLine = true)
                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    OutlinedTextField(value = protein, onValueChange = { protein = it }, label = { Text("Protein (g)") }, modifier = Modifier.weight(1f), singleLine = true)
                    OutlinedTextField(value = carbs, onValueChange = { carbs = it }, label = { Text("Carbs (g)") }, modifier = Modifier.weight(1f), singleLine = true)
                    OutlinedTextField(value = fat, onValueChange = { fat = it }, label = { Text("Fat (g)") }, modifier = Modifier.weight(1f), singleLine = true)
                }
            }
        },
        confirmButton = {
            Button(onClick = {
                if (name.isNotBlank() && calories.isNotBlank()) {
                    onAdd(name, mealType, calories.toIntOrNull() ?: 0, protein.toFloatOrNull() ?: 0f, carbs.toFloatOrNull() ?: 0f, fat.toFloatOrNull() ?: 0f)
                }
            }) { Text("Log Meal") }
        },
        dismissButton = { TextButton(onClick = onDismiss) { Text("Cancel") } }
    )
}
