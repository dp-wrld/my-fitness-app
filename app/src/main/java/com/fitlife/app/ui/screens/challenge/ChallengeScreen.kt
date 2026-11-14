package com.fitlife.app.ui.screens.challenge

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
import com.fitlife.app.data.local.entity.ChallengeEntity
import com.fitlife.app.ui.theme.*
import java.text.SimpleDateFormat
import java.util.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ChallengeScreen(
    navController: NavController,
    viewModel: ChallengeViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()
    var showAddDialog by remember { mutableStateOf(false) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Fitness Challenges", fontWeight = FontWeight.Bold) },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = MaterialTheme.colorScheme.surface)
            )
        },
        floatingActionButton = {
            FloatingActionButton(onClick = { showAddDialog = true }, containerColor = AccentYellow) {
                Icon(Icons.Default.Add, contentDescription = "Add Challenge", tint = Color.Black)
            }
        },
        containerColor = MaterialTheme.colorScheme.background
    ) { padding ->
        LazyColumn(
            modifier = Modifier.fillMaxSize().padding(padding),
            contentPadding = PaddingValues(16.dp, 8.dp, 16.dp, 100.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            item {
                Text("🏆 Active Challenges", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.onBackground)
            }

            if (uiState.activeChallenges.isEmpty()) {
                item { EmptyChallengeState(onBrowse = { showAddDialog = true }) }
            } else {
                items(uiState.activeChallenges, key = { it.id }) { challenge ->
                    ChallengeCard(
                        challenge = challenge,
                        completedDays = uiState.completedDaysMap[challenge.id] ?: 0,
                        onLogDay = { viewModel.logToday(challenge) },
                        onDelete = { viewModel.deleteChallenge(challenge) }
                    )
                }
            }

            item {
                Spacer(modifier = Modifier.height(8.dp))
                Text("📋 All Challenges", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.onBackground)
            }

            items(uiState.allChallenges.filter { !it.isActive }, key = { "inactive_${it.id}" }) { challenge ->
                ChallengeCard(
                    challenge = challenge,
                    completedDays = uiState.completedDaysMap[challenge.id] ?: 0,
                    onLogDay = { viewModel.logToday(challenge) },
                    onDelete = { viewModel.deleteChallenge(challenge) }
                )
            }

            item {
                Spacer(modifier = Modifier.height(8.dp))
                Text("💡 Popular Challenges", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.onBackground)
            }
            items(popularChallenges) { template ->
                TemplateChallengeCard(template = template, onStart = { viewModel.startChallenge(template) })
            }
        }
    }

    if (showAddDialog) {
        AddChallengeDialog(
            onDismiss = { showAddDialog = false },
            onAdd = { title, desc, days, category ->
                viewModel.addChallenge(title, desc, days, category)
                showAddDialog = false
            }
        )
    }
}

data class ChallengeTemplate(
    val title: String,
    val description: String,
    val durationDays: Int,
    val category: String,
    val emoji: String
)

val popularChallenges = listOf(
    ChallengeTemplate("30-Day Push-Up Challenge", "Increase push-ups daily from 10 to 100", 30, "Strength", "💪"),
    ChallengeTemplate("10K Steps Daily", "Walk 10,000 steps every day for 30 days", 30, "Cardio", "🚶"),
    ChallengeTemplate("7-Day Plank Challenge", "Hold plank position, increasing each day", 7, "Strength", "🧱"),
    ChallengeTemplate("21-Day Clean Eating", "No junk food for 21 days", 21, "Diet", "🥗"),
    ChallengeTemplate("30-Day Squat Challenge", "Daily squats from 50 to 250", 30, "Strength", "🏋️"),
    ChallengeTemplate("Couch to 5K", "Build up to running 5km in 8 weeks", 56, "Cardio", "🏃")
)

@Composable
fun ChallengeCard(
    challenge: ChallengeEntity,
    completedDays: Int,
    onLogDay: () -> Unit,
    onDelete: () -> Unit
) {
    val progress = (completedDays.toFloat() / challenge.durationDays.toFloat()).coerceIn(0f, 1f)
    val categoryColor = when (challenge.category) {
        "Strength" -> AccentOrange
        "Cardio" -> AccentBlue
        "Diet" -> AccentGreen
        "Flexibility" -> AccentPurple
        else -> Primary
    }

    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Box(
                    modifier = Modifier
                        .size(44.dp)
                        .background(categoryColor.copy(alpha = 0.15f), RoundedCornerShape(12.dp)),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(Icons.Default.EmojiEvents, contentDescription = null, tint = categoryColor, modifier = Modifier.size(26.dp))
                }
                Spacer(modifier = Modifier.width(12.dp))
                Column(modifier = Modifier.weight(1f)) {
                    Text(challenge.title, style = MaterialTheme.typography.titleSmall, fontWeight = FontWeight.Bold)
                    Text(challenge.category, style = MaterialTheme.typography.labelSmall, color = categoryColor)
                }
                if (challenge.isActive) {
                    Surface(shape = RoundedCornerShape(8.dp), color = AccentGreen.copy(alpha = 0.15f)) {
                        Text("Active", modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp), style = MaterialTheme.typography.labelSmall, color = AccentGreen)
                    }
                }
            }
            Spacer(modifier = Modifier.height(12.dp))
            Text(challenge.description, style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant, maxLines = 2)
            Spacer(modifier = Modifier.height(12.dp))
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text("Day $completedDays / ${challenge.durationDays}", style = MaterialTheme.typography.labelMedium, color = MaterialTheme.colorScheme.onSurfaceVariant)
                Spacer(modifier = Modifier.width(8.dp))
                LinearProgressIndicator(
                    progress = { progress },
                    modifier = Modifier.weight(1f).height(6.dp).clip(RoundedCornerShape(3.dp)),
                    color = categoryColor,
                    trackColor = categoryColor.copy(alpha = 0.15f)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text("${(progress * 100).toInt()}%", style = MaterialTheme.typography.labelSmall, color = categoryColor)
            }
            Spacer(modifier = Modifier.height(12.dp))
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                Button(
                    onClick = onLogDay,
                    modifier = Modifier.weight(1f),
                    colors = ButtonDefaults.buttonColors(containerColor = categoryColor)
                ) { Text("Log Today ✓") }
                IconButton(onClick = onDelete) {
                    Icon(Icons.Default.Delete, contentDescription = "Delete", tint = ColorError.copy(alpha = 0.7f))
                }
            }
        }
    }
}

@Composable
fun TemplateChallengeCard(template: ChallengeTemplate, onStart: () -> Unit) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(template.emoji, style = MaterialTheme.typography.headlineMedium)
            Spacer(modifier = Modifier.width(12.dp))
            Column(modifier = Modifier.weight(1f)) {
                Text(template.title, style = MaterialTheme.typography.titleSmall, fontWeight = FontWeight.SemiBold)
                Text("${template.durationDays} days • ${template.category}", style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
                Text(template.description, style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant, maxLines = 1)
            }
            OutlinedButton(onClick = onStart, border = ButtonDefaults.outlinedButtonBorder) {
                Text("Start", style = MaterialTheme.typography.labelMedium)
            }
        }
    }
}

@Composable
fun EmptyChallengeState(onBrowse: () -> Unit) {
    Column(
        modifier = Modifier.fillMaxWidth().padding(32.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text("🏆", style = MaterialTheme.typography.displayMedium)
        Spacer(modifier = Modifier.height(16.dp))
        Text("No active challenges", style = MaterialTheme.typography.titleMedium, color = MaterialTheme.colorScheme.onSurfaceVariant)
        Spacer(modifier = Modifier.height(16.dp))
        Button(onClick = onBrowse, colors = ButtonDefaults.buttonColors(containerColor = AccentYellow)) {
            Text("Create Challenge", color = Color.Black)
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddChallengeDialog(onDismiss: () -> Unit, onAdd: (String, String, Int, String) -> Unit) {
    var title by remember { mutableStateOf("") }
    var desc by remember { mutableStateOf("") }
    var days by remember { mutableStateOf("30") }
    var category by remember { mutableStateOf("Strength") }
    val categories = listOf("Strength", "Cardio", "Diet", "Flexibility", "Mental")
    var expanded by remember { mutableStateOf(false) }

    AlertDialog(
        onDismissRequest = onDismiss,
        containerColor = MaterialTheme.colorScheme.surface,
        title = { Text("New Challenge", fontWeight = FontWeight.Bold) },
        text = {
            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                OutlinedTextField(value = title, onValueChange = { title = it }, label = { Text("Challenge Title") }, modifier = Modifier.fillMaxWidth(), singleLine = true)
                OutlinedTextField(value = desc, onValueChange = { desc = it }, label = { Text("Description") }, modifier = Modifier.fillMaxWidth(), maxLines = 2)
                OutlinedTextField(value = days, onValueChange = { days = it.filter(Char::isDigit).take(3) }, label = { Text("Duration (days)") }, modifier = Modifier.fillMaxWidth(), singleLine = true)
                ExposedDropdownMenuBox(expanded = expanded, onExpandedChange = { expanded = it }) {
                    OutlinedTextField(
                        value = category,
                        onValueChange = {},
                        readOnly = true,
                        label = { Text("Category") },
                        trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded) },
                        modifier = Modifier.menuAnchor().fillMaxWidth()
                    )
                    ExposedDropdownMenu(expanded = expanded, onDismissRequest = { expanded = false }) {
                        categories.forEach { DropdownMenuItem(text = { Text(it) }, onClick = { category = it; expanded = false }) }
                    }
                }
            }
        },
        confirmButton = {
            Button(onClick = {
                val duration = days.toIntOrNull()
                if (title.isNotBlank() && duration != null && duration > 0) onAdd(title.trim(), desc.trim(), duration, category)
            }) { Text("Start Challenge") }
        },
        dismissButton = { TextButton(onClick = onDismiss) { Text("Cancel") } }
    )
}
