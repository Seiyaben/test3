package com.foretmagique.ui.matching

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.foretmagique.content.catalog.AlphabetCatalog
import com.foretmagique.content.game.SoundMatchSessionPlanner
import com.foretmagique.content.progression.ActivityType
import com.foretmagique.content.progression.ProgressionEngine
import com.foretmagique.data.progress.ProgressRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

enum class SoundMatchFeedback { CORRECT, INCORRECT }

data class SoundMatchUiState(
    val targetLetterId: String,
    val optionLetterIds: List<String>,
    val wrongAttempts: Int = 0,
    val feedback: SoundMatchFeedback? = null,
    val completedWithStars: Int? = null,
)

class SoundMatchViewModel(
    private val letterId: String,
    private val progressRepository: ProgressRepository,
) : ViewModel() {

    private val _uiState = MutableStateFlow(buildInitialState())
    val uiState: StateFlow<SoundMatchUiState> = _uiState.asStateFlow()

    private fun buildInitialState(): SoundMatchUiState {
        val prompt = SoundMatchSessionPlanner.buildPrompt(
            targetLetterId = letterId,
            allLetterIds = AlphabetCatalog.lettersInTeachingOrder.map { it.id },
        )
        return SoundMatchUiState(targetLetterId = prompt.targetLetterId, optionLetterIds = prompt.optionLetterIds)
    }

    fun onOptionSelected(selectedLetterId: String) {
        val state = _uiState.value
        if (state.completedWithStars != null) return

        if (selectedLetterId == state.targetLetterId) {
            val stars = ProgressionEngine.computeStars(
                correctFirstTry = state.wrongAttempts == 0,
                hintsUsed = state.wrongAttempts,
            )
            _uiState.value = state.copy(feedback = SoundMatchFeedback.CORRECT, completedWithStars = stars)
            viewModelScope.launch {
                progressRepository.recordStars(letterId, ActivityType.SOUND_MATCH, stars)
            }
        } else {
            _uiState.value = state.copy(
                feedback = SoundMatchFeedback.INCORRECT,
                wrongAttempts = state.wrongAttempts + 1,
            )
        }
    }

    companion object {
        fun factory(letterId: String, progressRepository: ProgressRepository): ViewModelProvider.Factory =
            viewModelFactory {
                initializer { SoundMatchViewModel(letterId, progressRepository) }
            }
    }
}
