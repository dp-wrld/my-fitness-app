package com.fitlife.app.data.local.dao

import androidx.room.*
import com.fitlife.app.data.local.entity.PlanDayEntity
import com.fitlife.app.data.local.entity.WorkoutPlanEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface WorkoutPlanDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertPlan(plan: WorkoutPlanEntity): Long

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertPlanDays(days: List<PlanDayEntity>)

    @Update
    suspend fun updatePlan(plan: WorkoutPlanEntity)

    @Delete
    suspend fun deletePlan(plan: WorkoutPlanEntity)

    @Query("SELECT * FROM workout_plans ORDER BY createdAt DESC")
    fun getAllPlans(): Flow<List<WorkoutPlanEntity>>

    @Query("SELECT * FROM workout_plans WHERE isActive = 1 LIMIT 1")
    fun getActivePlan(): Flow<WorkoutPlanEntity?>

    @Query("SELECT * FROM plan_days WHERE planId = :planId ORDER BY weekNumber, dayOfWeek")
    fun getDaysForPlan(planId: Long): Flow<List<PlanDayEntity>>
}
