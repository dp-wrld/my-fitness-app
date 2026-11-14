package com.fitlife.app.domain.repository

import com.fitlife.app.data.local.entity.*
import kotlinx.coroutines.flow.Flow

interface WorkoutRepository {
    fun getAllWorkouts(): Flow<List<WorkoutEntity>>
    fun getWorkoutsByDateRange(startDate: Long, endDate: Long): Flow<List<WorkoutEntity>>
    fun getExercisesForWorkout(workoutId: Long): Flow<List<ExerciseEntity>>
    suspend fun insertWorkout(workout: WorkoutEntity): Long
    suspend fun insertExercises(exercises: List<ExerciseEntity>)
    suspend fun updateWorkout(workout: WorkoutEntity)
    suspend fun deleteWorkout(workout: WorkoutEntity)
    fun getTotalCaloriesBurned(startDate: Long, endDate: Long): Flow<Int?>
    fun getCompletedWorkoutsCount(startDate: Long, endDate: Long): Flow<Int>
}

interface StepRepository {
    fun getStepsByDate(date: Long): Flow<StepEntity?>
    fun getStepsByDateRange(startDate: Long, endDate: Long): Flow<List<StepEntity>>
    suspend fun insertOrUpdateSteps(steps: StepEntity)
    fun getTotalSteps(startDate: Long, endDate: Long): Flow<Int?>
    fun getAverageSteps(startDate: Long, endDate: Long): Flow<Float?>
}

interface MealRepository {
    fun getMealsByDate(startDate: Long, endDate: Long): Flow<List<MealEntity>>
    suspend fun insertMeal(meal: MealEntity): Long
    suspend fun updateMeal(meal: MealEntity)
    suspend fun deleteMeal(meal: MealEntity)
    fun getTotalCaloriesForDay(startDate: Long, endDate: Long): Flow<Int?>
    fun getTotalProteinForDay(startDate: Long, endDate: Long): Flow<Float?>
    fun getTotalCarbsForDay(startDate: Long, endDate: Long): Flow<Float?>
    fun getTotalFatForDay(startDate: Long, endDate: Long): Flow<Float?>
}

interface ChallengeRepository {
    fun getAllChallenges(): Flow<List<ChallengeEntity>>
    fun getActiveChallenges(): Flow<List<ChallengeEntity>>
    suspend fun insertChallenge(challenge: ChallengeEntity): Long
    suspend fun updateChallenge(challenge: ChallengeEntity)
    suspend fun deleteChallenge(challenge: ChallengeEntity)
    suspend fun insertChallengeLog(log: ChallengeLogEntity)
    fun getLogsForChallenge(challengeId: Long): Flow<List<ChallengeLogEntity>>
    fun getCompletedDaysCount(challengeId: Long): Flow<Int>
}

interface WorkoutPlanRepository {
    fun getAllPlans(): Flow<List<WorkoutPlanEntity>>
    fun getActivePlan(): Flow<WorkoutPlanEntity?>
    suspend fun insertPlan(plan: WorkoutPlanEntity): Long
    suspend fun insertPlanDays(days: List<PlanDayEntity>)
    suspend fun updatePlan(plan: WorkoutPlanEntity)
    suspend fun deletePlan(plan: WorkoutPlanEntity)
    fun getDaysForPlan(planId: Long): Flow<List<PlanDayEntity>>
}

interface UserProfileRepository {
    fun getProfile(): Flow<UserProfileEntity?>
    suspend fun insertOrUpdateProfile(profile: UserProfileEntity)
    suspend fun updateProfile(profile: UserProfileEntity)
}
