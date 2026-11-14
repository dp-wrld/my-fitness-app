package com.fitlife.app.ui.screens.plans

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.fitlife.app.data.local.entity.WorkoutPlanEntity
import com.fitlife.app.domain.repository.WorkoutPlanRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

data class WorkoutPlansUiState(
    val allPlans: List<WorkoutPlanEntity> = emptyList(),
    val activePlan: WorkoutPlanEntity? = null
)

@HiltViewModel
class WorkoutPlansViewModel @Inject constructor(
    private val repository: WorkoutPlanRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(WorkoutPlansUiState())
    val uiState: StateFlow<WorkoutPlansUiState> = _uiState.asStateFlow()

    init {
        viewModelScope.launch {
            repository.getAllPlans().collect { plans ->
                _uiState.update { it.copy(allPlans = plans, activePlan = plans.firstOrNull { p -> p.isActive }) }
            }
        }
    }

    fun addPlan(name: String, description: String, weeks: Int, daysPerWeek: Int, difficulty: String, goal: String) {
        viewModelScope.launch {
            repository.insertPlan(WorkoutPlanEntity(name = name, description = description, durationWeeks = weeks, daysPerWeek = daysPerWeek, difficulty = difficulty, goal = goal))
        }
    }

    fun setActivePlan(plan: WorkoutPlanEntity) {
        viewModelScope.launch {
            _uiState.value.allPlans.forEach { p ->
                if (p.isActive) repository.updatePlan(p.copy(isActive = false))
            }
            repository.updatePlan(plan.copy(isActive = true))
        }
    }

    fun activateBuiltInPlan(template: PlanTemplate) {
        viewModelScope.launch {
            _uiState.value.allPlans.forEach { p ->
                if (p.isActive) repository.updatePlan(p.copy(isActive = false))
            }
            repository.insertPlan(
                WorkoutPlanEntity(name = template.name, description = template.description, durationWeeks = template.durationWeeks, daysPerWeek = template.daysPerWeek, difficulty = template.difficulty, goal = template.goal, isActive = true)
            )
        }
    }

    fun deactivatePlan(plan: WorkoutPlanEntity) {
        viewModelScope.launch { repository.updatePlan(plan.copy(isActive = false)) }
    }

    fun deletePlan(plan: WorkoutPlanEntity) {
        viewModelScope.launch { repository.deletePlan(plan) }
    }
}
