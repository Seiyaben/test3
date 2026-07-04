package com.foretmagique.ui.word

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.foretmagique.content.catalog.SyllableCatalog
import com.foretmagique.content.game.WordBuildSessionPlanner
import com.foretmagique.content.progression.ActivityType
import com.foretmagique.content.progression.ProgressionEngine
import com.foretmagique.data.progress.ProgressRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

data class WordTile(
    val instanceId: Int,
    val syllableId: String,
    val syllableText: String,
    val used: Boolean = false,
)

data class WordBuildUiState(
    val wordId: String,
    val wordText: String,
    val emoji: String,
    val targetSyllableIds: List<String>,
    val tiles: List<WordTile>,
    val progressIndex: Int = 0,
    val wrongAttempts: Int = 0,
    val completedWithStars: Int? = null,
) {
    val isComplete: Boolean get() = progressIndex >= targetSyllableIds.size
}

class WordBuildViewModel(
    private val letterId: String,
    private val progressRepository: ProgressRepository,
) : ViewModel() {

    private val _uiState = MutableStateFlow(buildInitialState())
    val uiState: StateFlow<WordBuildUiState?> = _uiState.asStateFlow()

    private fun buildInitialState(): WordBuildUiState? {
        val word = WordBuildSessionPlanner.pickWordForLetter(letterId) ?: return null
        val prompt = WordBuildSessionPlanner.buildPrompt(word)
        val tiles = prompt.tileSyllableIds.mapIndexed { index, syllableId ->
            WordTile(
                instanceId = index,
                syllableId = syllableId,
                syllableText = SyllableCatalog.syllableById(syllableId).text,
            )
        }
        return WordBuildUiState(
            wordId = word.id,
            wordText = word.text,
            emoji = word.emoji,
            targetSyllableIds = prompt.targetSyllableIds,
            tiles = tiles,
        )
    }

    fun onTileTapped(instanceId: Int) {
        val state = _uiState.value ?: return
        if (state.completedWithStars != null) return

        val tile = state.tiles.firstOrNull { it.instanceId == instanceId && !it.used } ?: return
        val expectedSyllableId = state.targetSyllableIds.getOrNull(state.progressIndex)

        if (tile.syllableId != expectedSyllableId) {
            _uiState.value = state.copy(wrongAttempts = state.wrongAttempts + 1)
            return
        }

        val updatedTiles = state.tiles.map { if (it.instanceId == instanceId) it.copy(used = true) else it }
        val newProgressIndex = state.progressIndex + 1

        if (newProgressIndex >= state.targetSyllableIds.size) {
            val stars = ProgressionEngine.computeStars(correctFirstTry = state.wrongAttempts == 0, hintsUsed = state.wrongAttempts)
            _uiState.value = state.copy(tiles = updatedTiles, progressIndex = newProgressIndex, completedWithStars = stars)
            viewModelScope.launch {
                progressRepository.recordStars(letterId, ActivityType.WORD_BUILD, stars)
            }
        } else {
            _uiState.value = state.copy(tiles = updatedTiles, progressIndex = newProgressIndex)
        }
    }

    companion object {
        fun factory(letterId: String, progressRepository: ProgressRepository): ViewModelProvider.Factory =
            viewModelFactory {
                initializer { WordBuildViewModel(letterId, progressRepository) }
            }
    }
}
