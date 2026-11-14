package com.fitlife.app.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import com.fitlife.app.data.local.dao.*
import com.fitlife.app.data.local.entity.*

@Database(
    entities = [
        WorkoutEntity::class,
        ExerciseEntity::class,
        StepEntity::class,
        MealEntity::class,
        ChallengeEntity::class,
        ChallengeLogEntity::class,
        WorkoutPlanEntity::class,
        PlanDayEntity::class,
        UserProfileEntity::class
    ],
    version = 1,
    exportSchema = false
)
abstract class FitLifeDatabase : RoomDatabase() {
    abstract fun workoutDao(): WorkoutDao
    abstract fun stepDao(): StepDao
    abstract fun mealDao(): MealDao
    abstract fun challengeDao(): ChallengeDao
    abstract fun workoutPlanDao(): WorkoutPlanDao
    abstract fun userProfileDao(): UserProfileDao
}
