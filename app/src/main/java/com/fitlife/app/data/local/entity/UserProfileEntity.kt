package com.fitlife.app.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "user_profile")
data class UserProfileEntity(
    @PrimaryKey val id: Int = 1,
    val name: String = "",
    val age: Int = 25,
    val heightCm: Float = 170f,
    val weightKg: Float = 70f,
    val gender: String = "Not specified",
    val activityLevel: String = "Moderate", // Sedentary, Light, Moderate, Active, Very Active
    val goal: String = "Stay Fit", // Lose Weight, Gain Muscle, Stay Fit, Improve Endurance
    val dailyStepGoal: Int = 10000,
    val dailyCalorieGoal: Int = 2000,
    val profileImageUri: String = ""
)
