package com.fitlife.app.ui.screens.dashboard

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.fitlife.app.domain.repository.ChallengeRepository
import com.fitlife.app.domain.repository.MealRepository
import com.fitlife.app.domain.repository.StepRepository
import com.fitlife.app.domain.repository.UserProfileRepository
import com.fitlife.app.domain.repository.WorkoutRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import java.util.*
import javax.inject.Inject

data class DashboardUiState(
    val userName: String = "Champ",
    val todaySteps: Int = 0,
    val stepGoal: Int = 10000,
    val caloriesBurned: Int = 0,
    val caloriesToday: Int = 0,
    val calorieGoal: Int = 2000,
    val waterGlasses: Int = 0,
    val currentStreak: Int = 3,
    val weeklyWorkouts: Int = 0,
    val activeChallenge: String? = null,
    val challengeCurrentDay: Int = 1,
    val challengeTotalDays: Int = 30,
    val bmi: Float = 22.5f,
    val motivationalQuote: String = "\"Every rep counts. Every step matters.\""
)

@HiltViewModel
class DashboardViewModel @Inject constructor(
    private val stepRepository: StepRepository,
    private val workoutRepository: WorkoutRepository,
    private val mealRepository: MealRepository,
    private val challengeRepository: ChallengeRepository,
    private val profileRepository: UserProfileRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(DashboardUiState())
    val uiState: StateFlow<DashboardUiState> = _uiState.asStateFlow()

    private val quotes = listOf(
        "\"Every rep counts. Every step matters.\"",
        "\"Your body can do it. It's your mind you need to convince.\"",
        "\"Sweat now. Shine later.\"",
        "\"Push yourself because no one else is going to do it for you.\"",
        "\"The only bad workout is the one that didn't happen.\""
    )

    init {
        loadDashboardData()
    }

    private fun loadDashboardData() {
        viewModelScope.launch {
            val calendar = Calendar.getInstance()
            val startOfDay = calendar.apply {
                set(Calendar.HOUR_OF_DAY, 0)
                set(Calendar.MINUTE, 0)
                set(Calendar.SECOND, 0)
                set(Calendar.MILLISECOND, 0)
            }.timeInMillis
            val endOfDay = calendar.apply {
                set(Calendar.HOUR_OF_DAY, 23)
                set(Calendar.MINUTE, 59)
                set(Calendar.SECOND, 59)
            }.timeInMillis

            // Load profile
            profileRepository.getProfile().collect { profile ->
                profile?.let {
                    val calculatedBmi = if (it.heightCm > 0) {
                        it.weightKg / ((it.heightCm / 100f) * (it.heightCm / 100f))
                    } else 22.5f
                    _uiState.update { state ->
                        state.copy(
                            userName = if (it.name.isBlank()) "Champ" else it.name,
                            stepGoal = it.dailyStepGoal,
                            calorieGoal = it.dailyCalorieGoal,
                            bmi = calculatedBmi
                        )
                    }
                }
            }
        }

        viewModelScope.launch {
            // Load today's steps
            val startOfDay = Calendar.getInstance().apply {
                set(Calendar.HOUR_OF_DAY, 0); set(Calendar.MINUTE, 0); set(Calendar.SECOND, 0); set(Calendar.MILLISECOND, 0)
            }.timeInMillis
            stepRepository.getStepsByDate(startOfDay).collect { steps ->
                steps?.let { s ->
                    _uiState.update {
                        it.copy(
                            todaySteps = s.stepCount,
                            caloriesBurned = it.caloriesBurned.coerceAtLeast(s.caloriesBurned)
                        )
                    }
                }
            }
        }

        viewModelScope.launch {
            val startOfDay = Calendar.getInstance().apply {
                set(Calendar.HOUR_OF_DAY, 0); set(Calendar.MINUTE, 0); set(Calendar.SECOND, 0); set(Calendar.MILLISECOND, 0)
            }.timeInMillis
            val endOfDay = Calendar.getInstance().apply {
                set(Calendar.HOUR_OF_DAY, 23); set(Calendar.MINUTE, 59); set(Calendar.SECOND, 59)
            }.timeInMillis

            mealRepository.getTotalCaloriesForDay(startOfDay, endOfDay).collect { calories ->
                _uiState.update { it.copy(caloriesToday = calories ?: 0) }
            }
        }

        viewModelScope.launch {
            val cal = Calendar.getInstance()
            cal.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY)
            val weekStart = cal.apply {
                set(Calendar.HOUR_OF_DAY, 0); set(Calendar.MINUTE, 0); set(Calendar.SECOND, 0); set(Calendar.MILLISECOND, 0)
            }.timeInMillis
            val weekEnd = System.currentTimeMillis()

            workoutRepository.getCompletedWorkoutsCount(weekStart, weekEnd).collect { count ->
                _uiState.update { it.copy(weeklyWorkouts = count) }
            }
        }

        viewModelScope.launch {
            challengeRepository.getActiveChallenges().collect { challenges ->
                val active = challenges.firstOrNull()
                _uiState.update { state ->
                    state.copy(
                        activeChallenge = active?.title,
                        challengeCurrentDay = active?.currentDay ?: 1,
                        challengeTotalDays = active?.durationDays ?: 30
                    )
                }
            }
        }

        _uiState.update { it.copy(motivationalQuote = quotes.random()) }
    }
}
