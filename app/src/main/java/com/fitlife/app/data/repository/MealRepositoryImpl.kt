package com.fitlife.app.data.repository

import com.fitlife.app.data.local.dao.MealDao
import com.fitlife.app.data.local.entity.MealEntity
import com.fitlife.app.domain.repository.MealRepository
import javax.inject.Inject

class MealRepositoryImpl @Inject constructor(private val dao: MealDao) : MealRepository {
    override fun getMealsByDate(startDate: Long, endDate: Long) = dao.getMealsByDate(startDate, endDate)
    override suspend fun insertMeal(meal: MealEntity) = dao.insertMeal(meal)
    override suspend fun updateMeal(meal: MealEntity) = dao.updateMeal(meal)
    override suspend fun deleteMeal(meal: MealEntity) = dao.deleteMeal(meal)
    override fun getTotalCaloriesForDay(startDate: Long, endDate: Long) = dao.getTotalCaloriesForDay(startDate, endDate)
    override fun getTotalProteinForDay(startDate: Long, endDate: Long) = dao.getTotalProteinForDay(startDate, endDate)
    override fun getTotalCarbsForDay(startDate: Long, endDate: Long) = dao.getTotalCarbsForDay(startDate, endDate)
    override fun getTotalFatForDay(startDate: Long, endDate: Long) = dao.getTotalFatForDay(startDate, endDate)
}
