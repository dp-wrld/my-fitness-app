package com.fitlife.app.ui.screens.challenge

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.fitlife.app.data.local.entity.ChallengeEntity
import com.fitlife.app.data.local.entity.ChallengeLogEntity
import com.fitlife.app.domain.repository.ChallengeRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import java.util.*
import javax.inject.Inject

data class ChallengeUiState(
    val allChallenges: List<ChallengeEntity> = emptyList(),
    val activeChallenges: List<ChallengeEntity> = emptyList(),
    val completedDaysMap: Map<Long, Int> = emptyMap()
)

@HiltViewModel
class ChallengeViewModel @Inject constructor(
    private val repository: ChallengeRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(ChallengeUiState())
    val uiState: StateFlow<ChallengeUiState> = _uiState.asStateFlow()

    init {
        viewModelScope.launch {
            repository.getAllChallenges().collect { all ->
                _uiState.update { it.copy(allChallenges = all, activeChallenges = all.filter { c -> c.isActive }) }
                // Load completed days for each challenge
                all.forEach { challenge ->
                    launch {
                        repository.getCompletedDaysCount(challenge.id).collect { count ->
                            _uiState.update { state ->
                                state.copy(completedDaysMap = state.completedDaysMap + (challenge.id to count))
                            }
                        }
                    }
                }
            }
        }
    }

    fun addChallenge(title: String, description: String, days: Int, category: String) {
        viewModelScope.launch {
            repository.insertChallenge(
                ChallengeEntity(
                    title = title, description = description, durationDays = days,
                    category = category, startDate = System.currentTimeMillis(), isActive = true
                )
            )
        }
    }

    fun startChallenge(template: ChallengeTemplate) {
        viewModelScope.launch {
            repository.insertChallenge(
                ChallengeEntity(
                    title = template.title, description = template.description,
                    durationDays = template.durationDays, category = template.category,
                    startDate = System.currentTimeMillis(), isActive = true
                )
            )
        }
    }

    fun logToday(challenge: ChallengeEntity) {
        viewModelScope.launch {
            val completed = (_uiState.value.completedDaysMap[challenge.id] ?: 0) + 1
            repository.insertChallengeLog(
                ChallengeLogEntity(
                    challengeId = challenge.id,
                    day = completed,
                    date = System.currentTimeMillis(),
                    value = 1f,
                    isCompleted = true
                )
            )
            if (completed >= challenge.durationDays) {
                repository.updateChallenge(challenge.copy(isActive = false, currentDay = completed))
            } else {
                repository.updateChallenge(challenge.copy(currentDay = completed))
            }
        }
    }

    fun deleteChallenge(challenge: ChallengeEntity) {
        viewModelScope.launch { repository.deleteChallenge(challenge) }
    }
}
