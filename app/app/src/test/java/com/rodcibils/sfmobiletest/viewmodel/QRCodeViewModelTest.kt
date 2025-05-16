package com.rodcibils.sfmobiletest.viewmodel

import app.cash.turbine.test
import com.rodcibils.sfmobiletest.model.QRCodeSeed
import com.rodcibils.sfmobiletest.repo.SeedRepository
import com.rodcibils.sfmobiletest.ui.screen.qrcode.QRCodeViewModel
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.Test
import kotlin.test.assertIs

class QRCodeViewModelTest {
    private val repository: SeedRepository = mockk()

    private fun createViewModel() = QRCodeViewModel(repository)

    @Test
    fun `retrieveSeed emits Success when repository returns seed`() =
        runTest {
            val fakeSeed = QRCodeSeed(seed = "abc123", expiresAt = "2025-01-01T00:00:00.000Z")
            coEvery { repository.retrieveSeed() } returns fakeSeed

            val viewModel = createViewModel()

            viewModel.uiState.test {
                viewModel.retrieveSeed()

                assertIs<QRCodeViewModel.UiState.Loading>(awaitItem())
                assertIs<QRCodeViewModel.UiState.Success>(awaitItem())
                cancelAndIgnoreRemainingEvents()
            }
        }

    @Test
    fun `retrieveSeed emits Error when repository throws`() =
        runTest {
            coEvery { repository.retrieveSeed() } throws RuntimeException("network fail")

            val viewModel = createViewModel()

            viewModel.uiState.test {
                viewModel.retrieveSeed()

                assertIs<QRCodeViewModel.UiState.Loading>(awaitItem())
                val error = awaitItem()
                assertIs<QRCodeViewModel.UiState.Error>(error)
                cancelAndIgnoreRemainingEvents()
            }
        }
}
