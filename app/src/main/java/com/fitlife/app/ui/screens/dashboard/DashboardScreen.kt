package com.fitlife.app.ui.screens.dashboard

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
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
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.fitlife.app.ui.navigation.Screen
import com.fitlife.app.ui.theme.*
import java.text.SimpleDateFormat
import java.util.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DashboardScreen(
    navController: NavController,
    viewModel: DashboardViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()
    val dateFormat = SimpleDateFormat("EEEE, MMM d", Locale.getDefault())
    val today = dateFormat.format(Date())

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background),
        contentPadding = PaddingValues(bottom = 100.dp)
    ) {
        // Header
        item {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(
                        Brush.verticalGradient(
                            listOf(
                                MaterialTheme.colorScheme.surface,
                                MaterialTheme.colorScheme.background
                            )
                        )
                    )
                    .padding(horizontal = 20.dp, vertical = 20.dp)
            ) {
                Column {
                    Text(
                        text = "Hey, ${uiState.userName} 👋",
                        style = MaterialTheme.typography.headlineSmall,
                        color = MaterialTheme.colorScheme.onBackground,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        text = today,
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = uiState.motivationalQuote,
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.primary
                    )
                }
            }
        }

        // Today's Stats Row
        item {
            Text(
                text = "Today's Progress",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onBackground,
                modifier = Modifier.padding(horizontal = 20.dp, vertical = 8.dp)
            )
        }

        item {
            LazyRow(
                contentPadding = PaddingValues(horizontal = 20.dp),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                item {
                    StatCard(
                        title = "Steps",
                        value = uiState.todaySteps.toString(),
                        goal = "/ ${uiState.stepGoal}",
                        icon = Icons.Default.DirectionsWalk,
                        color = AccentBlue,
                        progress = uiState.todaySteps.toFloat() / uiState.stepGoal.toFloat()
                    )
                }
                item {
                    StatCard(
                        title = "Calories Burned",
                        value = uiState.caloriesBurned.toString(),
                        goal = "kcal",
                        icon = Icons.Default.LocalFireDepartment,
                        color = AccentOrange,
                        progress = uiState.caloriesBurned.toFloat() / 500f
                    )
                }
                item {
                    StatCard(
                        title = "Water",
                        value = uiState.waterGlasses.toString(),
                        goal = "/ 8 glasses",
                        icon = Icons.Default.WaterDrop,
                        color = AccentBlue,
                        progress = uiState.waterGlasses / 8f
                    )
                }
                item {
                    StatCard(
                        title = "Calories In",
                        value = uiState.caloriesToday.toString(),
                        goal = "/ ${uiState.calorieGoal} kcal",
                        icon = Icons.Default.Restaurant,
                        color = AccentGreen,
                        progress = uiState.caloriesToday.toFloat() / uiState.calorieGoal.toFloat()
                    )
                }
            }
        }

        // Weekly Streak
        item {
            Spacer(modifier = Modifier.height(16.dp))
            WeeklyStreakCard(
                streak = uiState.currentStreak,
                weeklyWorkouts = uiState.weeklyWorkouts
            )
        }

        // Active Challenge
        if (uiState.activeChallenge != null) {
            item {
                Spacer(modifier = Modifier.height(16.dp))
                ActiveChallengeCard(
                    challengeTitle = uiState.activeChallenge!!,
                    day = uiState.challengeCurrentDay,
                    totalDays = uiState.challengeTotalDays,
                    onClick = { navController.navigate(Screen.Challenge.route) }
                )
            }
        }

        // Quick Actions
        item {
            Spacer(modifier = Modifier.height(16.dp))
            Text(
                text = "Quick Actions",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onBackground,
                modifier = Modifier.padding(horizontal = 20.dp, vertical = 8.dp)
            )
        }

        item {
            LazyRow(
                contentPadding = PaddingValues(horizontal = 20.dp),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                item {
                    QuickActionCard(
                        label = "Start Workout",
                        icon = Icons.Default.FitnessCenter,
                        color = Primary,
                        onClick = { navController.navigate(Screen.Workout.route) }
                    )
                }
                item {
                    QuickActionCard(
                        label = "Log Meal",
                        icon = Icons.Default.Restaurant,
                        color = AccentGreen,
                        onClick = { navController.navigate(Screen.Diet.route) }
                    )
                }
                item {
                    QuickActionCard(
                        label = "View Plans",
                        icon = Icons.Default.CalendarMonth,
                        color = AccentPurple,
                        onClick = { navController.navigate(Screen.Plans.route) }
                    )
                }
                item {
                    QuickActionCard(
                        label = "Profile",
                        icon = Icons.Default.Person,
                        color = AccentBlue,
                        onClick = { navController.navigate(Screen.Profile.route) }
                    )
                }
            }
        }

        // BMI Card
        item {
            Spacer(modifier = Modifier.height(16.dp))
            BmiCard(bmi = uiState.bmi)
            Spacer(modifier = Modifier.height(16.dp))
        }
    }
}

@Composable
fun StatCard(
    title: String,
    value: String,
    goal: String,
    icon: ImageVector,
    color: Color,
    progress: Float
) {
    Card(
        modifier = Modifier.width(150.dp),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            Icon(
                imageVector = icon,
                contentDescription = title,
                tint = color,
                modifier = Modifier.size(28.dp)
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = value,
                style = MaterialTheme.typography.headlineSmall,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onSurface
            )
            Text(
                text = goal,
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = title,
                style = MaterialTheme.typography.labelSmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
            Spacer(modifier = Modifier.height(4.dp))
            LinearProgressIndicator(
                progress = { progress.coerceIn(0f, 1f) },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(4.dp)
                    .clip(RoundedCornerShape(2.dp)),
                color = color,
                trackColor = color.copy(alpha = 0.2f)
            )
        }
    }
}

@Composable
fun QuickActionCard(
    label: String,
    icon: ImageVector,
    color: Color,
    onClick: () -> Unit
) {
    Card(
        onClick = onClick,
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = color.copy(alpha = 0.15f)),
        modifier = Modifier.size(100.dp)
    ) {
        Column(
            modifier = Modifier.fillMaxSize().padding(12.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Icon(imageVector = icon, contentDescription = label, tint = color, modifier = Modifier.size(32.dp))
            Spacer(modifier = Modifier.height(6.dp))
            Text(
                text = label,
                style = MaterialTheme.typography.labelSmall,
                color = color,
                fontWeight = FontWeight.Medium
            )
        }
    }
}

@Composable
fun WeeklyStreakCard(streak: Int, weeklyWorkouts: Int) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 20.dp),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
    ) {
        Row(
            modifier = Modifier.padding(16.dp).fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(56.dp)
                    .clip(CircleShape)
                    .background(AccentYellow.copy(alpha = 0.2f)),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    Icons.Default.LocalFireDepartment,
                    contentDescription = "Streak",
                    tint = AccentYellow,
                    modifier = Modifier.size(32.dp)
                )
            }
            Spacer(modifier = Modifier.width(16.dp))
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = "$streak Day Streak 🔥",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onSurface
                )
                Text(
                    text = "$weeklyWorkouts workouts this week",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
            Text(
                text = "Keep it up!",
                style = MaterialTheme.typography.labelMedium,
                color = Primary
            )
        }
    }
}

@Composable
fun ActiveChallengeCard(
    challengeTitle: String,
    day: Int,
    totalDays: Int,
    onClick: () -> Unit
) {
    Card(
        onClick = onClick,
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 20.dp),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(
            containerColor = Primary.copy(alpha = 0.15f)
        )
    ) {
        Row(
            modifier = Modifier.padding(16.dp).fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(Icons.Default.EmojiEvents, contentDescription = null, tint = AccentYellow, modifier = Modifier.size(36.dp))
            Spacer(modifier = Modifier.width(12.dp))
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = "Active Challenge",
                    style = MaterialTheme.typography.labelSmall,
                    color = Primary
                )
                Text(
                    text = challengeTitle,
                    style = MaterialTheme.typography.titleSmall,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onSurface
                )
                Text(
                    text = "Day $day of $totalDays",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                Spacer(modifier = Modifier.height(4.dp))
                LinearProgressIndicator(
                    progress = { day.toFloat() / totalDays.toFloat() },
                    modifier = Modifier.fillMaxWidth().height(4.dp).clip(RoundedCornerShape(2.dp)),
                    color = Primary,
                    trackColor = Primary.copy(alpha = 0.2f)
                )
            }
            Icon(Icons.Default.ChevronRight, contentDescription = null, tint = Primary)
        }
    }
}

@Composable
fun BmiCard(bmi: Float) {
    val (category, color) = when {
        bmi < 18.5f -> "Underweight" to AccentBlue
        bmi < 25f -> "Normal" to AccentGreen
        bmi < 30f -> "Overweight" to AccentOrange
        else -> "Obese" to ColorError
    }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 20.dp),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(
                text = "BMI Overview",
                style = MaterialTheme.typography.titleSmall,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onSurface
            )
            Spacer(modifier = Modifier.height(12.dp))
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(
                    text = String.format("%.1f", bmi),
                    style = MaterialTheme.typography.headlineMedium,
                    fontWeight = FontWeight.Bold,
                    color = color
                )
                Spacer(modifier = Modifier.width(12.dp))
                Column {
                    Text(
                        text = category,
                        style = MaterialTheme.typography.titleSmall,
                        color = color,
                        fontWeight = FontWeight.SemiBold
                    )
                    Text(
                        text = "Body Mass Index",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }
        }
    }
}
