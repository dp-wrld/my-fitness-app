package com.fitlife.app.data.local.dao

import androidx.room.*
import com.fitlife.app.data.local.entity.ExerciseEntity
import com.fitlife.app.data.local.entity.WorkoutEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface WorkoutDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertWorkout(workout: WorkoutEntity): Long

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertExercises(exercises: List<ExerciseEntity>)

    @Update
    suspend fun updateWorkout(workout: WorkoutEntity)

    @Delete
    suspend fun deleteWorkout(workout: WorkoutEntity)

    @Query("SELECT * FROM workouts ORDER BY date DESC")
    fun getAllWorkouts(): Flow<List<WorkoutEntity>>

    @Query("SELECT * FROM workouts WHERE date >= :startDate AND date <= :endDate ORDER BY date DESC")
    fun getWorkoutsByDateRange(startDate: Long, endDate: Long): Flow<List<WorkoutEntity>>

    @Query("SELECT * FROM exercises WHERE workoutId = :workoutId ORDER BY `order`")
    fun getExercisesForWorkout(workoutId: Long): Flow<List<ExerciseEntity>>

    @Query("SELECT SUM(caloriesBurned) FROM workouts WHERE date >= :startDate AND date <= :endDate")
    fun getTotalCaloriesBurned(startDate: Long, endDate: Long): Flow<Int?>

    @Query("SELECT COUNT(*) FROM workouts WHERE isCompleted = 1 AND date >= :startDate AND date <= :endDate")
    fun getCompletedWorkoutsCount(startDate: Long, endDate: Long): Flow<Int>

    @Query("SELECT * FROM workouts WHERE id = :workoutId")
    suspend fun getWorkoutById(workoutId: Long): WorkoutEntity?
}
