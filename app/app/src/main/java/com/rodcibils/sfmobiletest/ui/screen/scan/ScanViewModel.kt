package com.rodcibils.sfmobiletest.ui.screen.scan

import android.content.pm.PackageManager
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class ScanViewModel : ViewModel() {
    sealed class UiState {
        data object Idle : UiState()

        data class Scanning(val lastCode: String?) : UiState()

        data class Error(val message: String) : UiState()

        data object PermissionDenied : UiState()
    }

    private val _uiState = MutableStateFlow<UiState>(UiState.Idle)
    val uiState: StateFlow<UiState> get() = _uiState
    private val _hasRequestedPermission = mutableStateOf(false)
    val hasRequestedPermission: State<Boolean> get() = _hasRequestedPermission

    fun onCodeScanned(code: String) {
        _uiState.value = UiState.Scanning(lastCode = code)
    }

    fun onPermissionDenied() {
        _uiState.value = UiState.PermissionDenied
    }

    fun onError(message: String) {
        _uiState.value = UiState.Error(message)
    }

    fun resetState() {
        _uiState.value = UiState.Idle
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
}
