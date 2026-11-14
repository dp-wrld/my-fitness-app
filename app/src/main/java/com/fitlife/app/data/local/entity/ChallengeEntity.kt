package com.fitlife.app.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "challenges")
data class ChallengeEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val title: String,
    val description: String,
    val durationDays: Int,
    val category: String,
    val startDate: Long,
    val isActive: Boolean = true,
    val currentDay: Int = 1,
    val targetValue: Float = 0f,
    val unit: String = ""
)

@Entity(tableName = "challenge_logs")
data class ChallengeLogEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val challengeId: Long,
    val day: Int,
    val date: Long,
    val value: Float,
    val isCompleted: Boolean = false,
    val notes: String = ""
)
