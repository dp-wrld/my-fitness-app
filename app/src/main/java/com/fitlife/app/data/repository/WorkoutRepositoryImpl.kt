package com.fitlife.app.data.repository

import com.fitlife.app.data.local.dao.WorkoutDao
import com.fitlife.app.data.local.entity.ExerciseEntity
import com.fitlife.app.data.local.entity.WorkoutEntity
import com.fitlife.app.domain.repository.WorkoutRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class WorkoutRepositoryImpl @Inject constructor(private val dao: WorkoutDao) : WorkoutRepository {
    override fun getAllWorkouts() = dao.getAllWorkouts()
    override fun getWorkoutsByDateRange(startDate: Long, endDate: Long) = dao.getWorkoutsByDateRange(startDate, endDate)
    override fun getExercisesForWorkout(workoutId: Long) = dao.getExercisesForWorkout(workoutId)
    override suspend fun insertWorkout(workout: WorkoutEntity) = dao.insertWorkout(workout)
    override suspend fun insertExercises(exercises: List<ExerciseEntity>) = dao.insertExercises(exercises)
    override suspend fun updateWorkout(workout: WorkoutEntity) = dao.updateWorkout(workout)
    override suspend fun deleteWorkout(workout: WorkoutEntity) = dao.deleteWorkout(workout)
    override fun getTotalCaloriesBurned(startDate: Long, endDate: Long) = dao.getTotalCaloriesBurned(startDate, endDate)
    override fun getCompletedWorkoutsCount(startDate: Long, endDate: Long) = dao.getCompletedWorkoutsCount(startDate, endDate)
}
