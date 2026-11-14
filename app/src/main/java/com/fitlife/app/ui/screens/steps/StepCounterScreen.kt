package com.fitlife.app.ui.screens.steps

import androidx.compose.foundation.Canvas
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
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.fitlife.app.ui.theme.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun StepCounterScreen(
    navController: NavController,
    viewModel: StepViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background),
        contentPadding = PaddingValues(bottom = 100.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        item {
            TopAppBar(
                title = { Text("Step Counter", fontWeight = FontWeight.Bold) },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = MaterialTheme.colorScheme.surface)
            )
        }

        // Circular progress
        item {
            Spacer(modifier = Modifier.height(24.dp))
            StepCircularProgress(
                steps = uiState.todaySteps,
                goal = uiState.stepGoal
            )
        }

        // Stats row
        item {
            Spacer(modifier = Modifier.height(24.dp))
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 20.dp),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                StepStatCard(
                    modifier = Modifier.weight(1f),
                    title = "Distance",
                    value = String.format("%.2f km", uiState.distanceKm),
                    icon = Icons.Default.Straighten,
                    color = AccentBlue
                )
                StepStatCard(
                    modifier = Modifier.weight(1f),
                    title = "Calories",
                    value = "${uiState.caloriesBurned} kcal",
                    icon = Icons.Default.LocalFireDepartment,
                    color = AccentOrange
                )
                StepStatCard(
                    modifier = Modifier.weight(1f),
                    title = "Active Time",
                    value = "${uiState.activeMinutes} min",
                    icon = Icons.Default.Timer,
                    color = AccentGreen
                )
            }
        }

        // Weekly bar chart
        item {
            Spacer(modifier = Modifier.height(20.dp))
            WeeklyStepsCard(weeklySteps = uiState.weeklySteps, goal = uiState.stepGoal)
        }

        // Goal editor
        item {
            Spacer(modifier = Modifier.height(16.dp))
            GoalCard(
                currentGoal = uiState.stepGoal,
                onGoalChange = viewModel::updateGoal
            )
        }

        // Manual step entry
        item {
            Spacer(modifier = Modifier.height(16.dp))
            ManualStepEntry(onAdd = viewModel::addManualSteps)
        }
    }
}

@Composable
fun StepCircularProgress(steps: Int, goal: Int) {
    val progress = (steps.toFloat() / goal.toFloat()).coerceIn(0f, 1f)
    val percentage = (progress * 100).toInt()

    Box(
        contentAlignment = Alignment.Center,
        modifier = Modifier.size(220.dp)
    ) {
        Canvas(modifier = Modifier.size(220.dp)) {
            val strokeWidth = 20.dp.toPx()
            val radius = (size.minDimension - strokeWidth) / 2
            val center = Offset(size.width / 2, size.height / 2)
            val startAngle = -90f

            // Background track
            drawArc(
                color = Color(0xFF2A2A4A),
                startAngle = startAngle,
                sweepAngle = 360f,
                useCenter = false,
                style = Stroke(width = strokeWidth, cap = StrokeCap.Round),
                topLeft = Offset(center.x - radius, center.y - radius),
                size = Size(radius * 2, radius * 2)
            )

            // Progress arc
            if (progress > 0f) {
                drawArc(
                    color = AccentBlue,
                    startAngle = startAngle,
                    sweepAngle = 360f * progress,
                    useCenter = false,
                    style = Stroke(width = strokeWidth, cap = StrokeCap.Round),
                    topLeft = Offset(center.x - radius, center.y - radius),
                    size = Size(radius * 2, radius * 2)
                )
            }
        }

        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Text(
                text = steps.toString(),
                fontSize = 42.sp,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onBackground
            )
            Text(
                text = "steps",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = "$percentage% of goal",
                style = MaterialTheme.typography.labelMedium,
                color = AccentBlue
            )
        }
    }
}

@Composable
fun StepStatCard(modifier: Modifier = Modifier, title: String, value: String, icon: androidx.compose.ui.graphics.vector.ImageVector, color: Color) {
    Card(
        modifier = modifier,
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
    ) {
        Column(
            modifier = Modifier.padding(12.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Icon(icon, contentDescription = title, tint = color, modifier = Modifier.size(24.dp))
            Spacer(modifier = Modifier.height(4.dp))
            Text(value, style = MaterialTheme.typography.titleSmall, fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.onSurface)
            Text(title, style = MaterialTheme.typography.labelSmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
        }
    }
}

@Composable
fun WeeklyStepsCard(weeklySteps: List<Pair<String, Int>>, goal: Int) {
    Card(
        modifier = Modifier.fillMaxWidth().padding(horizontal = 20.dp),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text("This Week", style = MaterialTheme.typography.titleSmall, fontWeight = FontWeight.Bold)
            Spacer(modifier = Modifier.height(16.dp))
            Row(
                modifier = Modifier.fillMaxWidth().height(80.dp),
                horizontalArrangement = Arrangement.SpaceEvenly,
                verticalAlignment = Alignment.Bottom
            ) {
                weeklySteps.forEach { (day, steps) ->
                    val barHeight = if (goal > 0) (steps.toFloat() / goal.toFloat()).coerceIn(0f, 1f) else 0f
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Bottom,
                        modifier = Modifier.weight(1f)
                    ) {
                        Box(
                            modifier = Modifier
                                .width(20.dp)
                                .fillMaxHeight(barHeight.coerceAtLeast(0.05f))
                                .clip(RoundedCornerShape(topStart = 4.dp, topEnd = 4.dp))
                                .background(if (barHeight >= 1f) AccentGreen else AccentBlue.copy(alpha = 0.7f))
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(day, style = MaterialTheme.typography.labelSmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
                    }
                }
            }
        }
    }
}

@Composable
fun GoalCard(currentGoal: Int, onGoalChange: (Int) -> Unit) {
    var editing by remember { mutableStateOf(false) }
    var goalText by remember(currentGoal) { mutableStateOf(currentGoal.toString()) }

    Card(
        modifier = Modifier.fillMaxWidth().padding(horizontal = 20.dp),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
    ) {
        Row(
            modifier = Modifier.padding(16.dp).fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(Icons.Default.Flag, contentDescription = null, tint = Primary)
            Spacer(modifier = Modifier.width(12.dp))
            Column(modifier = Modifier.weight(1f)) {
                Text("Daily Step Goal", style = MaterialTheme.typography.titleSmall, fontWeight = FontWeight.SemiBold)
                if (editing) {
                    OutlinedTextField(
                        value = goalText,
                        onValueChange = { goalText = it },
                        singleLine = true,
                        modifier = Modifier.fillMaxWidth()
                    )
                } else {
                    Text("$currentGoal steps", style = MaterialTheme.typography.bodyMedium, color = MaterialTheme.colorScheme.onSurfaceVariant)
                }
            }
            if (editing) {
                TextButton(onClick = {
                    goalText.toIntOrNull()?.let { onGoalChange(it) }
                    editing = false
                }) { Text("Save") }
            } else {
                IconButton(onClick = { editing = true }) {
                    Icon(Icons.Default.Edit, contentDescription = "Edit", tint = Primary)
                }
            }
        }
    }
}

@Composable
fun ManualStepEntry(onAdd: (Int) -> Unit) {
    var stepsText by remember { mutableStateOf("") }
    Card(
        modifier = Modifier.fillMaxWidth().padding(horizontal = 20.dp),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text("Add Steps Manually", style = MaterialTheme.typography.titleSmall, fontWeight = FontWeight.SemiBold)
            Spacer(modifier = Modifier.height(8.dp))
            Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                OutlinedTextField(
                    value = stepsText,
                    onValueChange = { stepsText = it },
                    label = { Text("Steps to add") },
                    modifier = Modifier.weight(1f),
                    singleLine = true
                )
                Button(onClick = {
                    stepsText.toIntOrNull()?.let { onAdd(it) }
                    stepsText = ""
                }) { Text("Add") }
            }
        }
    }
}
