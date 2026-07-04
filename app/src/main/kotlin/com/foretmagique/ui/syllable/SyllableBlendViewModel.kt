package com.foretmagique.ui.syllable

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.foretmagique.content.game.SyllableBlendSessionPlanner
import com.foretmagique.content.progression.ActivityType
import com.foretmagique.content.progression.ProgressionEngine
import com.foretmagique.data.progress.ProgressRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

enum class SyllableTileKind { CONSONANT, VOWEL }

data class SyllableBlendUiState(
    val available: Boolean,
    val syllableId: String = "",
    val syllableText: String = "",
    val consonantPhonemeId: String = "",
    val vowelPhonemeId: String = "",
    val tileOrder: List<SyllableTileKind> = emptyList(),
    val tappedCount: Int = 0,
    val wrongAttempts: Int = 0,
    val completedWithStars: Int? = null,
)

class SyllableBlendViewModel(
    private val letterId: String,
    private val progressRepository: ProgressRepository,
) : ViewModel() {

    private val _uiState = MutableStateFlow(buildInitialState())
    val uiState: StateFlow<SyllableBlendUiState> = _uiState.asStateFlow()

    private fun buildInitialState(): SyllableBlendUiState {
        val syllable = SyllableBlendSessionPlanner.pickSyllableForLetter(letterId) ?: return SyllableBlendUiState(available = false)
        val consonantId = syllable.consonantLetterId ?: return SyllableBlendUiState(available = false)
        return SyllableBlendUiState(
            available = true,
            syllableId = syllable.id,
            syllableText = syllable.text,
            consonantPhonemeId = consonantId,
            vowelPhonemeId = syllable.vowelLetterId,
            tileOrder = listOf(SyllableTileKind.CONSONANT, SyllableTileKind.VOWEL).shuffled(),
        )
    }

    fun onTileTapped(kind: SyllableTileKind) {
        val state = _uiState.value
        if (!state.available || state.completedWithStars != null) return

        val expectedKind = if (state.tappedCount == 0) SyllableTileKind.CONSONANT else SyllableTileKind.VOWEL
        if (kind != expectedKind) {
            _uiState.value = state.copy(wrongAttempts = state.wrongAttempts + 1)
            return
        }

        val newTappedCount = state.tappedCount + 1
        if (newTappedCount < 2) {
            _uiState.value = state.copy(tappedCount = newTappedCount)
            return
        }

        val stars = ProgressionEngine.computeStars(correctFirstTry = state.wrongAttempts == 0, hintsUsed = state.wrongAttempts)
        _uiState.value = state.copy(tappedCount = newTappedCount, completedWithStars = stars)
        viewModelScope.launch {
            progressRepository.recordStars(letterId, ActivityType.SYLLABLE_BLEND, stars)
        }
    }

    companion object {
        fun factory(letterId: String, progressRepository: ProgressRepository): ViewModelProvider.Factory =
            viewModelFactory {
                initializer { SyllableBlendViewModel(letterId, progressRepository) }
            }
    }
}
