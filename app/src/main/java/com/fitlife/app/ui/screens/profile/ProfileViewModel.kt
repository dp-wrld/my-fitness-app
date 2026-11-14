package com.fitlife.app.ui.screens.profile

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.fitlife.app.data.local.entity.UserProfileEntity
import com.fitlife.app.domain.repository.UserProfileRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

data class ProfileUiState(
    val name: String = "",
    val age: Int = 25,
    val heightCm: Float = 170f,
    val weightKg: Float = 70f,
    val gender: String = "Not specified",
    val activityLevel: String = "Moderate",
    val goal: String = "Stay Fit",
    val dailyStepGoal: Int = 10000,
    val dailyCalorieGoal: Int = 2000
)

@HiltViewModel
class ProfileViewModel @Inject constructor(
    private val repository: UserProfileRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(ProfileUiState())
    val uiState: StateFlow<ProfileUiState> = _uiState.asStateFlow()

    init {
        viewModelScope.launch {
            repository.getProfile().collect { profile ->
                profile?.let { p ->
                    _uiState.update {
                        ProfileUiState(
                            name = p.name, age = p.age, heightCm = p.heightCm, weightKg = p.weightKg,
                            gender = p.gender, activityLevel = p.activityLevel, goal = p.goal,
                            dailyStepGoal = p.dailyStepGoal, dailyCalorieGoal = p.dailyCalorieGoal
                        )
                    }
                }
            }
        }
    }

    fun saveProfile(name: String, age: Int, weight: Float, height: Float, gender: String, activityLevel: String, goal: String, stepGoal: Int, calorieGoal: Int) {
        viewModelScope.launch {
            repository.insertOrUpdateProfile(
                UserProfileEntity(
                    id = 1, name = name, age = age, heightCm = height, weightKg = weight,
                    gender = gender, activityLevel = activityLevel, goal = goal,
                    dailyStepGoal = stepGoal, dailyCalorieGoal = calorieGoal
                )
            )
        }
    }
}
