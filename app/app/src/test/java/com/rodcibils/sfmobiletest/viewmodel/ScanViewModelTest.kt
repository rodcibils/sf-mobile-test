package com.rodcibils.sfmobiletest.viewmodel

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import androidx.core.content.ContextCompat
import app.cash.turbine.test
import com.rodcibils.sfmobiletest.repo.SeedRepository
import com.rodcibils.sfmobiletest.ui.screen.scan.ScanViewModel
import io.mockk.clearAllMocks
import io.mockk.coEvery
import io.mockk.every
import io.mockk.mockk
import io.mockk.mockkStatic
import io.mockk.unmockkStatic
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test
import kotlin.test.assertEquals
import kotlin.test.assertIs
import kotlin.test.assertTrue

class ScanViewModelTest {
    private val repository: SeedRepository = mockk()

    private fun createViewModel(): ScanViewModel = ScanViewModel(repository)

    @Before
    fun setup() {
        clearAllMocks()
    }

    @Test
    fun `onCodeScanned emits ValidSeed when seed is valid`() =
        runTest {
            coEvery { repository.isSeedValid("abc123") } returns true
            val viewModel = createViewModel()

            viewModel.uiState.test {
                // Skip initial Idle state emitted by StateFlow
                awaitItem()

                viewModel.onCodeScanned("abc123")

                assertIs<ScanViewModel.UiState.Validating>(awaitItem())
                val result = awaitItem()
                assertIs<ScanViewModel.UiState.ValidSeed>(result)
                assertEquals("abc123", result.seed)
            }
        }

    @Test
    fun `onCodeScanned emits InvalidSeed when seed is invalid`() =
        runTest {
            coEvery { repository.isSeedValid("wrong-seed") } returns false
            val viewModel = createViewModel()

            viewModel.uiState.test {
                // Skip initial Idle state emitted by StateFlow
                awaitItem()

                viewModel.onCodeScanned("wrong-seed")

                assertIs<ScanViewModel.UiState.Validating>(awaitItem())
                val result = awaitItem()
                assertIs<ScanViewModel.UiState.InvalidSeed>(result)
                assertEquals("wrong-seed", result.seed)
            }
        }

    @Test
    fun `onCodeScanned emits Error when repository throws`() =
        runTest {
            coEvery { repository.isSeedValid("crash-seed") } throws RuntimeException("network fail")
            val viewModel = createViewModel()

            viewModel.uiState.test {
                // Skip initial Idle state emitted by StateFlow
                awaitItem()

                viewModel.onCodeScanned("crash-seed")

                assertIs<ScanViewModel.UiState.Validating>(awaitItem())
                val error = awaitItem()
                assertIs<ScanViewModel.UiState.Error>(error)
                assertTrue(error.message.contains("network fail"))
            }
        }

    @Test
    fun `resetState sets uiState to Idle`() =
        runTest {
            val viewModel = createViewModel()
            viewModel.resetState()
            assertIs<ScanViewModel.UiState.Idle>(viewModel.uiState.value)
        }

    @Test
    fun `onPermissionDenied sets uiState to PermissionDenied`() =
        runTest {
            val viewModel = createViewModel()
            viewModel.onPermissionDenied()
            assertIs<ScanViewModel.UiState.PermissionDenied>(viewModel.uiState.value)
        }

    @Test
    fun `onPermissionRequested sets hasRequestedPermission to true`() {
        val viewModel = createViewModel()
        assertEquals(false, viewModel.hasRequestedPermission.value)
        viewModel.onPermissionRequested()
        assertEquals(true, viewModel.hasRequestedPermission.value)
    }

    @Test
    fun `checkCameraPermission emits Idle if granted`() {
        val mockContext = mockk<Context>()
        mockkStatic(ContextCompat::class)

        every {
            ContextCompat.checkSelfPermission(mockContext, Manifest.permission.CAMERA)
        } returns PackageManager.PERMISSION_GRANTED

        val viewModel = createViewModel()
        viewModel.checkCameraPermission(mockContext)

        assertIs<ScanViewModel.UiState.Idle>(viewModel.uiState.value)
        unmockkStatic(ContextCompat::class)
    }

    @Test
    fun `checkCameraPermission emits PermissionDenied if denied`() {
        val mockContext = mockk<Context>()
        mockkStatic(ContextCompat::class)

        every {
            ContextCompat.checkSelfPermission(mockContext, Manifest.permission.CAMERA)
        } returns PackageManager.PERMISSION_DENIED

        val viewModel = createViewModel()
        viewModel.checkCameraPermission(mockContext)

        assertIs<ScanViewModel.UiState.PermissionDenied>(viewModel.uiState.value)
        unmockkStatic(ContextCompat::class)
    }
}
