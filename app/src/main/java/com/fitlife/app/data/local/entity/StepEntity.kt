package com.fitlife.app.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "steps")
data class StepEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val date: Long, // day timestamp (midnight)
    val stepCount: Int,
    val distanceMeters: Float,
    val caloriesBurned: Int
)
