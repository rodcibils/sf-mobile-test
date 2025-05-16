package com.rodcibils.sfmobiletest.ui.screen.qrcode

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.viewmodel.compose.viewModel
import com.rodcibils.sfmobiletest.R
import com.rodcibils.sfmobiletest.ui.common.CustomTopAppBar
import com.rodcibils.sfmobiletest.util.QRCodeBitmapGenerator
import kotlinx.coroutines.launch

@Composable
fun QRCodeScreen(
    onBack: (() -> Unit)? = null,
    viewModel: QRCodeViewModel = viewModel(),
) {
    val uiState by viewModel.uiState.collectAsState()
    val lifecycleOwner = LocalLifecycleOwner.current

    // Launch retrieveSeed in lifecycleScope when screen is first composed
    LaunchedEffect(Unit) {
        lifecycleOwner.lifecycleScope.launch {
            viewModel.retrieveSeed()
        }
    }

    val (qrBitmap, formattedDate) =
        remember(uiState) {
            if (uiState is QRCodeViewModel.UiState.Success) {
                val seed = (uiState as QRCodeViewModel.UiState.Success).qrCodeSeed
                val bitmap = QRCodeBitmapGenerator.generate(seed.seed)
                val formattedDate = seed.getFormattedExpiration()
                bitmap to formattedDate
            } else {
                null to null
            }
        }

    Scaffold(
        topBar = {
            CustomTopAppBar(stringResource(R.string.scan), onBackPressed = onBack)
        },
    ) { innerPadding ->
        Box(
            modifier =
                Modifier
                    .fillMaxSize()
                    .padding(innerPadding),
            contentAlignment = Alignment.Center,
        ) {
            when (val state = uiState) {
                is QRCodeViewModel.UiState.Loading -> {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        CircularProgressIndicator()
                        Text(
                            text = stringResource(R.string.loading),
                            style = MaterialTheme.typography.bodyLarge,
                            modifier = Modifier.padding(top = 16.dp),
                            textAlign = TextAlign.Center,
                        )
                    }
                }

                is QRCodeViewModel.UiState.Error -> {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text(
                            text = state.message,
                            style = MaterialTheme.typography.bodyLarge,
                            color = MaterialTheme.colorScheme.error,
                            modifier = Modifier.padding(16.dp),
                        )
                        Button(onClick = {
                            lifecycleOwner.lifecycleScope.launch {
                                viewModel.retrieveSeed()
                            }
                        }) {
                            Text(stringResource(R.string.retry))
                        }
                    }
                }

                is QRCodeViewModel.UiState.Success -> {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        qrBitmap?.let {
                            Image(bitmap = it.asImageBitmap(), contentDescription = "QR Code")
                        }
                        formattedDate?.let {
                            Text(
                                text = "Expires at: $it",
                                style = MaterialTheme.typography.bodyLarge,
                                modifier = Modifier.padding(top = 16.dp),
                            )
                        }
                    }
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun QRCodeScreenPreview() {
    MaterialTheme {
        QRCodeScreen()
    }
}
