package com.fitlife.app.ui.screens.profile

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.fitlife.app.ui.theme.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfileScreen(
    navController: NavController,
    viewModel: ProfileViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()
    var isEditing by remember { mutableStateOf(false) }
    var editName by remember(uiState.name) { mutableStateOf(uiState.name) }
    var editAge by remember(uiState.age) { mutableStateOf(uiState.age.toString()) }
    var editWeight by remember(uiState.weightKg) { mutableStateOf(uiState.weightKg.toString()) }
    var editHeight by remember(uiState.heightCm) { mutableStateOf(uiState.heightCm.toString()) }
    var editStepGoal by remember(uiState.dailyStepGoal) { mutableStateOf(uiState.dailyStepGoal.toString()) }
    var editCalGoal by remember(uiState.dailyCalorieGoal) { mutableStateOf(uiState.dailyCalorieGoal.toString()) }
    var genderExpanded by remember { mutableStateOf(false) }
    var activityExpanded by remember { mutableStateOf(false) }
    var goalExpanded by remember { mutableStateOf(false) }
    var selectedGender by remember(uiState.gender) { mutableStateOf(uiState.gender) }
    var selectedActivity by remember(uiState.activityLevel) { mutableStateOf(uiState.activityLevel) }
    var selectedGoal by remember(uiState.goal) { mutableStateOf(uiState.goal) }

    LazyColumn(
        modifier = Modifier.fillMaxSize().background(MaterialTheme.colorScheme.background),
        contentPadding = PaddingValues(bottom = 100.dp)
    ) {
        item {
            // Header banner
            Box(
                modifier = Modifier.fillMaxWidth().height(200.dp).background(
                    Brush.verticalGradient(listOf(Primary.copy(alpha = 0.8f), MaterialTheme.colorScheme.background))
                ),
                contentAlignment = Alignment.Center
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Box(
                        modifier = Modifier.size(80.dp).clip(CircleShape).background(MaterialTheme.colorScheme.surface),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(uiState.name.firstOrNull()?.uppercase() ?: "👤", fontSize = 36.sp, color = Primary)
                    }
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(uiState.name.ifBlank { "Set Your Name" }, style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold, color = Color.White)
                    Text(uiState.goal, style = MaterialTheme.typography.bodyMedium, color = Color.White.copy(alpha = 0.8f))
                }
            }
        }

        // BMI & Stats
        item {
            val bmi = if (uiState.heightCm > 0) uiState.weightKg / ((uiState.heightCm / 100f) * (uiState.heightCm / 100f)) else 0f
            val bmiCategory = when {
                bmi < 18.5f -> "Underweight"
                bmi < 25f -> "Normal Weight"
                bmi < 30f -> "Overweight"
                else -> "Obese"
            }
            val bmiColor = when {
                bmi < 18.5f -> AccentBlue
                bmi < 25f -> AccentGreen
                bmi < 30f -> AccentOrange
                else -> ColorError
            }
            Row(
                modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp, vertical = 8.dp),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                StatInfoCard(modifier = Modifier.weight(1f), label = "Weight", value = "${uiState.weightKg}kg", color = AccentBlue)
                StatInfoCard(modifier = Modifier.weight(1f), label = "Height", value = "${uiState.heightCm}cm", color = AccentGreen)
                StatInfoCard(modifier = Modifier.weight(1f), label = "BMI", value = String.format("%.1f", bmi), color = bmiColor, subtitle = bmiCategory)
            }
        }

        // Profile form
        item {
            Card(
                modifier = Modifier.fillMaxWidth().padding(16.dp),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
            ) {
                Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
                    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) {
                        Text("Personal Info", style = MaterialTheme.typography.titleSmall, fontWeight = FontWeight.Bold)
                        IconButton(onClick = { isEditing = !isEditing }) {
                            Icon(if (isEditing) Icons.Default.Check else Icons.Default.Edit, contentDescription = "Edit", tint = Primary)
                        }
                    }

                    if (isEditing) {
                        OutlinedTextField(value = editName, onValueChange = { editName = it }, label = { Text("Name") }, modifier = Modifier.fillMaxWidth(), singleLine = true)
                        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                            OutlinedTextField(value = editAge, onValueChange = { editAge = it }, label = { Text("Age") }, modifier = Modifier.weight(1f), singleLine = true)
                            OutlinedTextField(value = editWeight, onValueChange = { editWeight = it }, label = { Text("Weight (kg)") }, modifier = Modifier.weight(1f), singleLine = true)
                        }
                        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                            OutlinedTextField(value = editHeight, onValueChange = { editHeight = it }, label = { Text("Height (cm)") }, modifier = Modifier.weight(1f), singleLine = true)
                        }

                        // Gender dropdown
                        ExposedDropdownMenuBox(expanded = genderExpanded, onExpandedChange = { genderExpanded = it }) {
                            OutlinedTextField(value = selectedGender, onValueChange = {}, readOnly = true, label = { Text("Gender") }, trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(genderExpanded) }, modifier = Modifier.menuAnchor().fillMaxWidth())
                            ExposedDropdownMenu(expanded = genderExpanded, onDismissRequest = { genderExpanded = false }) {
                                listOf("Male", "Female", "Not specified").forEach { DropdownMenuItem(text = { Text(it) }, onClick = { selectedGender = it; genderExpanded = false }) }
                            }
                        }

                        // Activity level dropdown
                        ExposedDropdownMenuBox(expanded = activityExpanded, onExpandedChange = { activityExpanded = it }) {
                            OutlinedTextField(value = selectedActivity, onValueChange = {}, readOnly = true, label = { Text("Activity Level") }, trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(activityExpanded) }, modifier = Modifier.menuAnchor().fillMaxWidth())
                            ExposedDropdownMenu(expanded = activityExpanded, onDismissRequest = { activityExpanded = false }) {
                                listOf("Sedentary", "Light", "Moderate", "Active", "Very Active").forEach { DropdownMenuItem(text = { Text(it) }, onClick = { selectedActivity = it; activityExpanded = false }) }
                            }
                        }

                        // Goal dropdown
                        ExposedDropdownMenuBox(expanded = goalExpanded, onExpandedChange = { goalExpanded = it }) {
                            OutlinedTextField(value = selectedGoal, onValueChange = {}, readOnly = true, label = { Text("Primary Goal") }, trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(goalExpanded) }, modifier = Modifier.menuAnchor().fillMaxWidth())
                            ExposedDropdownMenu(expanded = goalExpanded, onDismissRequest = { goalExpanded = false }) {
                                listOf("Lose Weight", "Gain Muscle", "Stay Fit", "Improve Endurance").forEach { DropdownMenuItem(text = { Text(it) }, onClick = { selectedGoal = it; goalExpanded = false }) }
                            }
                        }

                        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                            OutlinedTextField(value = editStepGoal, onValueChange = { editStepGoal = it }, label = { Text("Step Goal") }, modifier = Modifier.weight(1f), singleLine = true)
                            OutlinedTextField(value = editCalGoal, onValueChange = { editCalGoal = it }, label = { Text("Calorie Goal") }, modifier = Modifier.weight(1f), singleLine = true)
                        }

                        Button(
                            onClick = {
                                viewModel.saveProfile(
                                    name = editName,
                                    age = editAge.toIntOrNull() ?: 25,
                                    weight = editWeight.toFloatOrNull() ?: 70f,
                                    height = editHeight.toFloatOrNull() ?: 170f,
                                    gender = selectedGender,
                                    activityLevel = selectedActivity,
                                    goal = selectedGoal,
                                    stepGoal = editStepGoal.toIntOrNull() ?: 10000,
                                    calorieGoal = editCalGoal.toIntOrNull() ?: 2000
                                )
                                isEditing = false
                            },
                            modifier = Modifier.fillMaxWidth(),
                            colors = ButtonDefaults.buttonColors(containerColor = Primary)
                        ) { Text("Save Profile") }

                    } else {
                        // View mode
                        ProfileRow(label = "Name", value = uiState.name.ifBlank { "Not set" })
                        ProfileRow(label = "Age", value = "${uiState.age} years")
                        ProfileRow(label = "Gender", value = uiState.gender)
                        ProfileRow(label = "Activity Level", value = uiState.activityLevel)
                        ProfileRow(label = "Primary Goal", value = uiState.goal)
                        ProfileRow(label = "Daily Step Goal", value = "${uiState.dailyStepGoal} steps")
                        ProfileRow(label = "Daily Calorie Goal", value = "${uiState.dailyCalorieGoal} kcal")
                    }
                }
            }
        }
    }
}

@Composable
fun StatInfoCard(modifier: Modifier = Modifier, label: String, value: String, color: Color, subtitle: String? = null) {
    Card(modifier = modifier, shape = RoundedCornerShape(12.dp), colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)) {
        Column(modifier = Modifier.padding(12.dp), horizontalAlignment = Alignment.CenterHorizontally) {
            Text(value, style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold, color = color)
            if (subtitle != null) Text(subtitle, style = MaterialTheme.typography.labelSmall, color = color)
            Text(label, style = MaterialTheme.typography.labelSmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
        }
    }
}

@Composable
fun ProfileRow(label: String, value: String) {
    Row(modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp), horizontalArrangement = Arrangement.SpaceBetween) {
        Text(label, style = MaterialTheme.typography.bodyMedium, color = MaterialTheme.colorScheme.onSurfaceVariant)
        Text(value, style = MaterialTheme.typography.bodyMedium, fontWeight = FontWeight.Medium, color = MaterialTheme.colorScheme.onSurface)
    }
}
