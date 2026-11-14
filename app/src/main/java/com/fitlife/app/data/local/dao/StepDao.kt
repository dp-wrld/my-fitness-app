package com.fitlife.app.data.local.dao

import androidx.room.*
import com.fitlife.app.data.local.entity.StepEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface StepDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertOrUpdateSteps(steps: StepEntity)

    @Query("SELECT * FROM steps WHERE date = :date LIMIT 1")
    fun getStepsByDate(date: Long): Flow<StepEntity?>

    @Query("SELECT * FROM steps WHERE date >= :startDate AND date <= :endDate ORDER BY date ASC")
    fun getStepsByDateRange(startDate: Long, endDate: Long): Flow<List<StepEntity>>

    @Query("SELECT SUM(stepCount) FROM steps WHERE date >= :startDate AND date <= :endDate")
    fun getTotalSteps(startDate: Long, endDate: Long): Flow<Int?>

    @Query("SELECT AVG(stepCount) FROM steps WHERE date >= :startDate AND date <= :endDate")
    fun getAverageSteps(startDate: Long, endDate: Long): Flow<Float?>
}
