package com.fitlife.app.data.local.dao

import androidx.room.*
import com.fitlife.app.data.local.entity.MealEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface MealDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertMeal(meal: MealEntity): Long

    @Update
    suspend fun updateMeal(meal: MealEntity)

    @Delete
    suspend fun deleteMeal(meal: MealEntity)

    @Query("SELECT * FROM meals WHERE date >= :startDate AND date <= :endDate ORDER BY date DESC")
    fun getMealsByDate(startDate: Long, endDate: Long): Flow<List<MealEntity>>

    @Query("SELECT SUM(calories) FROM meals WHERE date >= :startDate AND date <= :endDate")
    fun getTotalCaloriesForDay(startDate: Long, endDate: Long): Flow<Int?>

    @Query("SELECT SUM(proteinG) FROM meals WHERE date >= :startDate AND date <= :endDate")
    fun getTotalProteinForDay(startDate: Long, endDate: Long): Flow<Float?>

    @Query("SELECT SUM(carbsG) FROM meals WHERE date >= :startDate AND date <= :endDate")
    fun getTotalCarbsForDay(startDate: Long, endDate: Long): Flow<Float?>

    @Query("SELECT SUM(fatG) FROM meals WHERE date >= :startDate AND date <= :endDate")
    fun getTotalFatForDay(startDate: Long, endDate: Long): Flow<Float?>
}
