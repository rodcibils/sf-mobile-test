package com.rodcibils.sfmobiletest.ui.screen.qrcode

import android.content.pm.PackageManager
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModel
import com.rodcibils.sfmobiletest.repo.SeedRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class ScanViewModel(
    /**
     * TODO: replace with DI in real app
     */
    private val repository: SeedRepository = SeedRepository(),
) : ViewModel() {
    private val _uiState = MutableStateFlow<UiState>(UiState.Idle)
    val uiState: StateFlow<UiState> get() = _uiState

    private val _hasRequestedPermission = mutableStateOf(false)
    val hasRequestedPermission: State<Boolean> get() = _hasRequestedPermission

    suspend fun onCodeScanned(code: String) {
        _uiState.value = UiState.Validating
        validateSeed(code)
    }

    private suspend fun validateSeed(seed: String) {
        try {
            val isValid = repository.isSeedValid(seed)
            _uiState.value =
                if (isValid) {
                    UiState.ValidSeed(seed)
                } else {
                    UiState.InvalidSeed(seed)
                }
        } catch (e: Exception) {
            _uiState.value = UiState.Error("Validation error: ${e.localizedMessage}")
        }
    }

    fun resetState() {
        _uiState.value = UiState.Idle
    }

    fun onPermissionDenied() {
        _uiState.value = UiState.PermissionDenied
    }

    fun onError(message: String) {
        _uiState.value = UiState.Error(message)
    }

    fun onPermissionRequested() {
        _hasRequestedPermission.value = true
    }

    fun checkCameraPermission(context: android.content.Context) {
        val granted =
            ContextCompat.checkSelfPermission(
                context,
                android.Manifest.permission.CAMERA,
            ) == PackageManager.PERMISSION_GRANTED

        if (granted) {
            resetState()
        } else {
            _uiState.value = UiState.PermissionDenied
        }
    }

    sealed class UiState {
        data object Idle : UiState()

        data object Validating : UiState()

        data class ValidSeed(val seed: String) : UiState()

        data class InvalidSeed(val seed: String) : UiState()

        data class Scanning(val lastCode: String?) : UiState()

        data class Error(val message: String) : UiState()

        data object PermissionDenied : UiState()
    }
}
