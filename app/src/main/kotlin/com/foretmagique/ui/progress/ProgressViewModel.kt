package com.foretmagique.ui.progress

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.foretmagique.content.catalog.AlphabetCatalog
import com.foretmagique.content.progression.ActivityType
import com.foretmagique.content.progression.ProgressionEngine
import com.foretmagique.content.progression.UserProgressSnapshot
import com.foretmagique.data.progress.ProgressRepository
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn

data class LetterProgressUiState(
    val displayLetter: String,
    val bestStars: Int,
    val mastered: Boolean,
)

data class ProgressOverviewUiState(
    val totalStars: Int,
    val masteredLetterCount: Int,
    val totalLetterCount: Int,
    val letters: List<LetterProgressUiState>,
)

class ProgressViewModel(progressRepository: ProgressRepository) : ViewModel() {

    val uiState: StateFlow<ProgressOverviewUiState> = progressRepository.progress
        .map { snapshot -> buildOverview(snapshot) }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000),
            initialValue = buildOverview(ProgressionEngine.initialSnapshot()),
        )

    private fun buildOverview(snapshot: UserProgressSnapshot): ProgressOverviewUiState {
        val letters = AlphabetCatalog.lettersInTeachingOrder.map { letter ->
            val starsPerActivity = ActivityType.entries.map { activity ->
                snapshot.starsByActivityKey[ProgressionEngine.activityKey(letter.id, activity)] ?: 0
            }
            LetterProgressUiState(
                displayLetter = "${letter.upper}${letter.lower}",
                bestStars = starsPerActivity.max(),
                mastered = ProgressionEngine.isLetterMastered(snapshot, letter.id),
            )
        }
        val totalStars = AlphabetCatalog.lettersInTeachingOrder.sumOf { letter ->
            ActivityType.entries.sumOf { activity ->
                snapshot.starsByActivityKey[ProgressionEngine.activityKey(letter.id, activity)] ?: 0
            }
        }
        return ProgressOverviewUiState(
            totalStars = totalStars,
            masteredLetterCount = letters.count { it.mastered },
            totalLetterCount = letters.size,
            letters = letters,
        )
    }

    companion object {
        fun factory(progressRepository: ProgressRepository): ViewModelProvider.Factory = viewModelFactory {
            initializer { ProgressViewModel(progressRepository) }
        }
    }
}
