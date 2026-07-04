package com.foretmagique.ui.forestmap

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

data class ClearingUiState(
    val letterId: String,
    val displayLetter: String,
    val unlocked: Boolean,
    val stars: Int,
)

class ForestMapViewModel(progressRepository: ProgressRepository) : ViewModel() {

    val clearings: StateFlow<List<ClearingUiState>> = progressRepository.progress
        .map { snapshot -> buildClearings(snapshot) }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000),
            initialValue = buildClearings(ProgressionEngine.initialSnapshot()),
        )

    private fun buildClearings(snapshot: UserProgressSnapshot): List<ClearingUiState> =
        AlphabetCatalog.lettersInTeachingOrder.map { letter ->
            val soundMatchKey = ProgressionEngine.activityKey(letter.id, ActivityType.SOUND_MATCH)
            ClearingUiState(
                letterId = letter.id,
                displayLetter = "${letter.upper}${letter.lower}",
                unlocked = letter.id in snapshot.unlockedLetterIds,
                stars = snapshot.starsByActivityKey[soundMatchKey] ?: 0,
            )
        }

    companion object {
        fun factory(progressRepository: ProgressRepository): ViewModelProvider.Factory = viewModelFactory {
            initializer { ForestMapViewModel(progressRepository) }
        }
    }
}
