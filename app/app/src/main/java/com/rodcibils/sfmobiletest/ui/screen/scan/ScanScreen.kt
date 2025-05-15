package com.rodcibils.sfmobiletest.ui.screen.scan

import android.Manifest
import android.content.Intent
import android.net.Uri
import android.provider.Settings
import android.util.Log
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.OptIn
import androidx.camera.core.CameraSelector
import androidx.camera.core.ExperimentalGetImage
import androidx.camera.core.ImageAnalysis
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.camera.view.PreviewView
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.content.ContextCompat
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.lifecycle.viewmodel.compose.viewModel
import com.google.mlkit.vision.barcode.BarcodeScannerOptions
import com.google.mlkit.vision.barcode.BarcodeScanning
import com.google.mlkit.vision.barcode.common.Barcode
import com.google.mlkit.vision.common.InputImage
import com.rodcibils.sfmobiletest.R
import com.rodcibils.sfmobiletest.ui.common.CustomTopAppBar

@OptIn(ExperimentalGetImage::class)
@Composable
fun ScanScreen(
    onBack: (() -> Unit)? = null,
    viewModel: ScanViewModel = viewModel(),
) {
    val context = LocalContext.current
    val lifecycleOwner = LocalLifecycleOwner.current

    DisposableEffect(Unit) {
        val observer =
            androidx.lifecycle.LifecycleEventObserver { _, event ->
                if (event.name == "ON_RESUME") {
                    viewModel.checkCameraPermission(context)
                }
            }

        val lifecycle = lifecycleOwner.lifecycle
        lifecycle.addObserver(observer)

        onDispose {
            lifecycle.removeObserver(observer)
        }
    }

    val barcodeScanner =
        remember {
            BarcodeScanning.getClient(
                BarcodeScannerOptions.Builder().setBarcodeFormats(Barcode.FORMAT_QR_CODE).build(),
            )
        }
    val cameraProviderFuture = remember { ProcessCameraProvider.getInstance(context) }

    val permissionLauncher =
        rememberLauncherForActivityResult(
            ActivityResultContracts.RequestPermission(),
        ) { granted ->
            viewModel.onPermissionRequested()
            if (granted) viewModel.resetState() else viewModel.onPermissionDenied()
        }

    LaunchedEffect(Unit) {
        if (!viewModel.hasRequestedPermission.value) {
            permissionLauncher.launch(Manifest.permission.CAMERA)
        }
    }

    val uiState by viewModel.uiState.collectAsState()

    // Show Toast when a code is scanned
    LaunchedEffect(uiState) {
        if (uiState is ScanViewModel.UiState.Scanning) {
            val code = (uiState as ScanViewModel.UiState.Scanning).lastCode
            Toast.makeText(context, "QR Code read: $code", Toast.LENGTH_SHORT).show()
        }
    }

    Scaffold(
        topBar = {
            CustomTopAppBar(
                title = stringResource(R.string.scan),
                onBackPressed = onBack,
            )
        },
    ) { innerPadding ->
        Box(
            modifier =
                Modifier
                    .fillMaxSize()
                    .padding(innerPadding),
            contentAlignment = Alignment.Center,
        ) {
            when (uiState) {
                is ScanViewModel.UiState.PermissionDenied -> {
                    PermissionExplanation(
                        onClick = {
                            val intent =
                                Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS).apply {
                                    data = Uri.fromParts("package", context.packageName, null)
                                }
                            context.startActivity(intent)
                        },
                    )
                }

                else -> {
                    AndroidView(factory = { ctx ->
                        val previewView = PreviewView(ctx)

                        val preview =
                            androidx.camera.core.Preview.Builder().build().apply {
                                surfaceProvider = previewView.surfaceProvider
                            }

                        val cameraSelector = CameraSelector.DEFAULT_BACK_CAMERA

                        val imageAnalyzer =
                            @Suppress("ktlint:standard:max-line-length")
                            ImageAnalysis.Builder().setBackpressureStrategy(
                                ImageAnalysis.STRATEGY_KEEP_ONLY_LATEST,
                            ).build().also {
                                it.setAnalyzer(
                                    ContextCompat.getMainExecutor(ctx),
                                ) { imageProxy ->
                                    val mediaImage = imageProxy.image
                                    if (mediaImage != null) {
                                        val inputImage =
                                            InputImage.fromMediaImage(
                                                mediaImage,
                                                imageProxy.imageInfo.rotationDegrees,
                                            )
                                        barcodeScanner.process(inputImage)
                                            .addOnSuccessListener { barcodes ->
                                                barcodes.firstOrNull()?.rawValue?.let { value ->
                                                    val current =
                                                        @Suppress("ktlint:standard:max-line-length")
                                                        (uiState as? ScanViewModel.UiState.Scanning)?.lastCode
                                                    if (value != current) {
                                                        viewModel.onCodeScanned(value)
                                                    }
                                                }
                                            }.addOnCompleteListener {
                                                imageProxy.close()
                                            }
                                    } else {
                                        imageProxy.close()
                                    }
                                }
                            }

                        cameraProviderFuture.addListener({
                            val cameraProvider = cameraProviderFuture.get()
                            try {
                                cameraProvider.unbindAll()
                                cameraProvider.bindToLifecycle(
                                    lifecycleOwner,
                                    cameraSelector,
                                    preview,
                                    imageAnalyzer,
                                )
                            } catch (e: Exception) {
                                Log.e("ScanScreen", "Camera binding failed", e)
                            }
                        }, ContextCompat.getMainExecutor(ctx))

                        previewView
                    })
                }
            }
        }
    }
}

@Composable
private fun PermissionExplanation(onClick: () -> Unit) {
    Column(
        modifier =
            Modifier
                .fillMaxSize()
                .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
    ) {
        Text(
            text = stringResource(R.string.camera_permission_msg),
            style = MaterialTheme.typography.bodyLarge,
            textAlign = TextAlign.Center,
        )
        Spacer(modifier = Modifier.height(16.dp))
        Button(onClick = onClick) {
            Text(text = stringResource(R.string.open_settings))
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun ScanScreenPreview() {
    MaterialTheme {
        ScanScreen()
    }
}
