package com.fitlife.app.ui.screens.workout

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.fitlife.app.data.local.entity.WorkoutEntity
import com.fitlife.app.domain.repository.WorkoutRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import java.util.*
import javax.inject.Inject

data class WorkoutUiState(
    val workouts: List<WorkoutEntity> = emptyList(),
    val selectedCategory: String = "All",
    val weeklyWorkouts: Int = 0,
    val weeklyCalories: Int = 0,
    val weeklyMinutes: Int = 0
)

@HiltViewModel
class WorkoutViewModel @Inject constructor(
    private val repository: WorkoutRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(WorkoutUiState())
    val uiState: StateFlow<WorkoutUiState> = _uiState.asStateFlow()

    init {
        loadWorkouts()
    }

    fun selectCategory(category: String) {
        _uiState.update { it.copy(selectedCategory = category) }
        loadWorkouts()
    }

    private fun loadWorkouts() {
        viewModelScope.launch {
            repository.getAllWorkouts().collect { allWorkouts ->
                val filtered = if (_uiState.value.selectedCategory == "All") allWorkouts
                else allWorkouts.filter { it.category == _uiState.value.selectedCategory }

                val calendar = Calendar.getInstance()
                calendar.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY)
                val weekStart = calendar.apply {
                    set(Calendar.HOUR_OF_DAY, 0); set(Calendar.MINUTE, 0); set(Calendar.SECOND, 0)
                }.timeInMillis
                val weekWorkouts = allWorkouts.filter { it.date >= weekStart }

                _uiState.update {
                    it.copy(
                        workouts = filtered,
                        weeklyWorkouts = weekWorkouts.count { w -> w.isCompleted },
                        weeklyCalories = weekWorkouts.sumOf { w -> w.caloriesBurned },
                        weeklyMinutes = weekWorkouts.sumOf { w -> w.durationMinutes }
                    )
                }
            }
        }
    }

    fun addWorkout(name: String, category: String, duration: Int, calories: Int, notes: String) {
        viewModelScope.launch {
            val workout = WorkoutEntity(
                name = name,
                category = category,
                durationMinutes = duration,
                caloriesBurned = calories,
                date = System.currentTimeMillis(),
                notes = notes,
                isCompleted = false
            )
            repository.insertWorkout(workout)
        }
    }

    fun toggleWorkoutComplete(workout: WorkoutEntity) {
        viewModelScope.launch {
            repository.updateWorkout(workout.copy(isCompleted = !workout.isCompleted))
        }
    }

    fun deleteWorkout(workout: WorkoutEntity) {
        viewModelScope.launch {
            repository.deleteWorkout(workout)
        }
    }
}
