package com.fitlife.app.data.repository

import com.fitlife.app.data.local.dao.WorkoutPlanDao
import com.fitlife.app.data.local.entity.PlanDayEntity
import com.fitlife.app.data.local.entity.WorkoutPlanEntity
import com.fitlife.app.domain.repository.WorkoutPlanRepository
import javax.inject.Inject

class WorkoutPlanRepositoryImpl @Inject constructor(private val dao: WorkoutPlanDao) : WorkoutPlanRepository {
    override fun getAllPlans() = dao.getAllPlans()
    override fun getActivePlan() = dao.getActivePlan()
    override suspend fun insertPlan(plan: WorkoutPlanEntity) = dao.insertPlan(plan)
    override suspend fun insertPlanDays(days: List<PlanDayEntity>) = dao.insertPlanDays(days)
    override suspend fun updatePlan(plan: WorkoutPlanEntity) = dao.updatePlan(plan)
    override suspend fun deletePlan(plan: WorkoutPlanEntity) = dao.deletePlan(plan)
    override fun getDaysForPlan(planId: Long) = dao.getDaysForPlan(planId)
}
