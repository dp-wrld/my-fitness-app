package com.fitlife.app.ui.screens.steps

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.fitlife.app.data.local.entity.StepEntity
import com.fitlife.app.domain.repository.StepRepository
import com.fitlife.app.domain.repository.UserProfileRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.*
import javax.inject.Inject

data class StepUiState(
    val todaySteps: Int = 0,
    val stepGoal: Int = 10000,
    val distanceKm: Float = 0f,
    val caloriesBurned: Int = 0,
    val activeMinutes: Int = 0,
    val weeklySteps: List<Pair<String, Int>> = emptyList()
)

@HiltViewModel
class StepViewModel @Inject constructor(
    private val stepRepository: StepRepository,
    private val profileRepository: UserProfileRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(StepUiState())
    val uiState: StateFlow<StepUiState> = _uiState.asStateFlow()

    init {
        loadStepData()
        loadWeeklySteps()
    }

    private fun getMidnight(): Long {
        return Calendar.getInstance().apply {
            set(Calendar.HOUR_OF_DAY, 0); set(Calendar.MINUTE, 0); set(Calendar.SECOND, 0); set(Calendar.MILLISECOND, 0)
        }.timeInMillis
    }

    private fun loadStepData() {
        viewModelScope.launch {
            profileRepository.getProfile().collect { profile ->
                _uiState.update { it.copy(stepGoal = profile?.dailyStepGoal ?: 10000) }
            }
        }
        viewModelScope.launch {
            stepRepository.getStepsByDate(getMidnight()).collect { steps ->
                val s = steps?.stepCount ?: 0
                val dist = s * 0.762f / 1000f // average stride length
                val cal = (s * 0.04f).toInt()
                val active = (s / 100).coerceAtMost(60)
                _uiState.update { it.copy(todaySteps = s, distanceKm = dist, caloriesBurned = cal, activeMinutes = active) }
            }
        }
    }

    private fun loadWeeklySteps() {
        viewModelScope.launch {
            val calendar = Calendar.getInstance()
            val days = mutableListOf<Pair<String, Long>>()
            val dayNames = listOf("Mon", "Tue", "Wed", "Thu", "Fri", "Sat", "Sun")
            calendar.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY)
            repeat(7) { i ->
                calendar.set(Calendar.HOUR_OF_DAY, 0)
                calendar.set(Calendar.MINUTE, 0)
                calendar.set(Calendar.SECOND, 0)
                calendar.set(Calendar.MILLISECOND, 0)
                days.add(Pair(dayNames[i], calendar.timeInMillis))
                calendar.add(Calendar.DAY_OF_MONTH, 1)
            }
            val weekStart = days.first().second
            val weekEnd = System.currentTimeMillis()
            stepRepository.getStepsByDateRange(weekStart, weekEnd).collect { stepList ->
                val stepMap = stepList.associateBy { it.date }
                val weekly = days.map { (name, midnight) ->
                    Pair(name, stepMap[midnight]?.stepCount ?: 0)
                }
                _uiState.update { it.copy(weeklySteps = weekly) }
            }
        }
    }

    fun addManualSteps(steps: Int) {
        viewModelScope.launch {
            val midnight = getMidnight()
            val current = _uiState.value.todaySteps
            val total = current + steps
            val dist = total * 0.762f / 1000f
            val cal = (total * 0.04f).toInt()
            stepRepository.insertOrUpdateSteps(
                StepEntity(date = midnight, stepCount = total, distanceMeters = dist * 1000, caloriesBurned = cal)
            )
        }
    }

    fun updateGoal(newGoal: Int) {
        viewModelScope.launch {
            _uiState.update { it.copy(stepGoal = newGoal) }
        }
    }
}
