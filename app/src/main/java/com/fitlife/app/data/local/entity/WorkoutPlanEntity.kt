package com.fitlife.app.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "workout_plans")
data class WorkoutPlanEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val name: String,
    val description: String,
    val durationWeeks: Int,
    val daysPerWeek: Int,
    val difficulty: String, // Beginner, Intermediate, Advanced
    val goal: String, // Weight Loss, Muscle Gain, Endurance, Flexibility
    val isActive: Boolean = false,
    val createdAt: Long = System.currentTimeMillis()
)

@Entity(tableName = "plan_days")
data class PlanDayEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val planId: Long,
    val dayOfWeek: Int, // 1=Monday to 7=Sunday
    val weekNumber: Int,
    val workoutName: String,
    val exercises: String, // JSON string of exercises
    val isRestDay: Boolean = false
)
