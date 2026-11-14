package com.fitlife.app.data.repository

import com.fitlife.app.data.local.dao.StepDao
import com.fitlife.app.data.local.entity.StepEntity
import com.fitlife.app.domain.repository.StepRepository
import javax.inject.Inject

class StepRepositoryImpl @Inject constructor(private val dao: StepDao) : StepRepository {
    override fun getStepsByDate(date: Long) = dao.getStepsByDate(date)
    override fun getStepsByDateRange(startDate: Long, endDate: Long) = dao.getStepsByDateRange(startDate, endDate)
    override suspend fun insertOrUpdateSteps(steps: StepEntity) = dao.insertOrUpdateSteps(steps)
    override fun getTotalSteps(startDate: Long, endDate: Long) = dao.getTotalSteps(startDate, endDate)
    override fun getAverageSteps(startDate: Long, endDate: Long) = dao.getAverageSteps(startDate, endDate)
}
