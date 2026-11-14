package com.fitlife.app.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "workouts")
data class WorkoutEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val name: String,
    val category: String, // Strength, Cardio, Flexibility, HIIT
    val durationMinutes: Int,
    val caloriesBurned: Int,
    val date: Long, // timestamp
    val notes: String = "",
    val isCompleted: Boolean = false
)

@Entity(tableName = "exercises")
data class ExerciseEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val workoutId: Long,
    val name: String,
    val sets: Int = 0,
    val reps: Int = 0,
    val weightKg: Float = 0f,
    val durationSeconds: Int = 0,
    val restSeconds: Int = 60,
    val order: Int = 0
)
