package com.rodcibils.sfmobiletest.ui.screen.qrcode

import androidx.lifecycle.ViewModel
import com.rodcibils.sfmobiletest.repo.SeedRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class QRCodeViewModel(
    /**
     * TODO: replace with DI
     */
    private val repository: SeedRepository = SeedRepository(),
) : ViewModel() {
    sealed class UiState {
        data object Loading : UiState()

        data class Success(val seed: String) : UiState()

        data class Error(val message: String) : UiState()
    }

    private val _uiState = MutableStateFlow<UiState>(UiState.Loading)
    val uiState: StateFlow<UiState> get() = _uiState

    suspend fun retrieveSeed() {
        _uiState.value = UiState.Loading

        try {
            val seed = repository.retrieveSeed()
            _uiState.value = UiState.Success(seed)
        } catch (e: Exception) {
            _uiState.value = UiState.Error("Failed to load seed: ${e.localizedMessage}")
        }
    }
}
