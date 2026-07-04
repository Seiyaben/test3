package com.foretmagique.ui.tracing

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.foretmagique.content.progression.ActivityType
import com.foretmagique.content.tracing.CapitalStrokeGuideCatalog
import com.foretmagique.content.tracing.NormalizedPoint
import com.foretmagique.content.tracing.PathScorer
import com.foretmagique.data.progress.ProgressRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

data class TracingUiState(
    val available: Boolean,
    val checkpoints: List<NormalizedPoint> = emptyList(),
    val attempt: Int = 0,
    val completedWithStars: Int? = null,
)

class TracingViewModel(
    private val letterId: String,
    private val progressRepository: ProgressRepository,
) : ViewModel() {

    private val guide = CapitalStrokeGuideCatalog.guideForLetter(letterId)

    private val _uiState = MutableStateFlow(
        if (guide == null) {
            TracingUiState(available = false)
        } else {
            TracingUiState(available = true, checkpoints = guide.checkpoints)
        },
    )
    val uiState: StateFlow<TracingUiState> = _uiState.asStateFlow()

    fun onTraceFinished(drawnPath: List<NormalizedPoint>) {
        val state = _uiState.value
        if (!state.available) return

        val result = PathScorer.score(state.checkpoints, drawnPath)
        val stars = PathScorer.starsFor(result)
        _uiState.value = state.copy(completedWithStars = stars)
        viewModelScope.launch {
            progressRepository.recordStars(letterId, ActivityType.TRACING, stars)
        }
    }

    fun onRetry() {
        val state = _uiState.value
        _uiState.value = state.copy(attempt = state.attempt + 1, completedWithStars = null)
    }

    companion object {
        fun factory(letterId: String, progressRepository: ProgressRepository): ViewModelProvider.Factory =
            viewModelFactory {
                initializer { TracingViewModel(letterId, progressRepository) }
            }
    }
}
