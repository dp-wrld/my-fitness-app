package com.fitlife.app.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "meals")
data class MealEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val name: String,
    val mealType: String, // Breakfast, Lunch, Dinner, Snack
    val calories: Int,
    val proteinG: Float,
    val carbsG: Float,
    val fatG: Float,
    val date: Long,
    val servingSize: Float = 1f,
    val servingUnit: String = "serving"
)
