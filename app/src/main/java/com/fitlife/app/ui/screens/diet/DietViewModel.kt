package com.fitlife.app.ui.screens.diet

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.fitlife.app.data.local.entity.MealEntity
import com.fitlife.app.domain.repository.MealRepository
import com.fitlife.app.domain.repository.UserProfileRepository
import com.fitlife.app.domain.repository.WorkoutRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import java.util.*
import javax.inject.Inject

data class DietUiState(
    val meals: List<MealEntity> = emptyList(),
    val totalCalories: Int = 0,
    val totalProtein: Float = 0f,
    val totalCarbs: Float = 0f,
    val totalFat: Float = 0f,
    val calorieGoal: Int = 2000,
    val caloriesBurned: Int = 0
)

@HiltViewModel
class DietViewModel @Inject constructor(
    private val mealRepository: MealRepository,
    private val profileRepository: UserProfileRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(DietUiState())
    val uiState: StateFlow<DietUiState> = _uiState.asStateFlow()

    init {
        loadTodayMeals()
    }

    private fun getDayRange(): Pair<Long, Long> {
        val cal = Calendar.getInstance()
        val end = cal.apply { set(Calendar.HOUR_OF_DAY, 23); set(Calendar.MINUTE, 59); set(Calendar.SECOND, 59) }.timeInMillis
        val start = cal.apply { set(Calendar.HOUR_OF_DAY, 0); set(Calendar.MINUTE, 0); set(Calendar.SECOND, 0) }.timeInMillis
        return Pair(start, end)
    }

    private fun loadTodayMeals() {
        val (start, end) = getDayRange()
        viewModelScope.launch {
            profileRepository.getProfile().collect { profile ->
                _uiState.update { it.copy(calorieGoal = profile?.dailyCalorieGoal ?: 2000) }
            }
        }
        viewModelScope.launch {
            mealRepository.getMealsByDate(start, end).collect { meals ->
                _uiState.update {
                    it.copy(
                        meals = meals,
                        totalCalories = meals.sumOf { m -> m.calories },
                        totalProtein = meals.sumOf { m -> m.proteinG.toDouble() }.toFloat(),
                        totalCarbs = meals.sumOf { m -> m.carbsG.toDouble() }.toFloat(),
                        totalFat = meals.sumOf { m -> m.fatG.toDouble() }.toFloat()
                    )
                }
            }
        }
    }

    fun addMeal(name: String, type: String, calories: Int, protein: Float, carbs: Float, fat: Float) {
        viewModelScope.launch {
            mealRepository.insertMeal(
                MealEntity(
                    name = name, mealType = type, calories = calories,
                    proteinG = protein, carbsG = carbs, fatG = fat,
                    date = System.currentTimeMillis()
                )
            )
        }
    }

    fun deleteMeal(meal: MealEntity) {
        viewModelScope.launch { mealRepository.deleteMeal(meal) }
    }
}
