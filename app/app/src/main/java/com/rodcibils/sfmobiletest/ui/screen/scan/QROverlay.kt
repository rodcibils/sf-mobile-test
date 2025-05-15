package com.rodcibils.sfmobiletest.ui.screen.scan

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
internal fun QrOverlay() {
    Box(
        modifier =
            Modifier
                .fillMaxSize(),
        contentAlignment = Alignment.Center,
    ) {
        val color = MaterialTheme.colorScheme.primary
        androidx.compose.foundation.Canvas(
            modifier =
                Modifier
                    .fillMaxSize()
                    .padding(48.dp),
        ) {
            val minSize = size.minDimension * 0.6f
            val left = (size.width - minSize) / 2f
            val top = (size.height - minSize) / 2f

            val strokeWidth = 6f
            val cornerLength = 40f

            drawLine(
                color = color,
                start = androidx.compose.ui.geometry.Offset(left, top),
                end = androidx.compose.ui.geometry.Offset(left + cornerLength, top),
                strokeWidth = strokeWidth,
            )
            drawLine(
                color = color,
                start = androidx.compose.ui.geometry.Offset(left, top),
                end = androidx.compose.ui.geometry.Offset(left, top + cornerLength),
                strokeWidth = strokeWidth,
            )

            drawLine(
                color = color,
                start = androidx.compose.ui.geometry.Offset(left + minSize, top),
                end = androidx.compose.ui.geometry.Offset(left + minSize - cornerLength, top),
                strokeWidth = strokeWidth,
            )
            drawLine(
                color = color,
                start = androidx.compose.ui.geometry.Offset(left + minSize, top),
                end = androidx.compose.ui.geometry.Offset(left + minSize, top + cornerLength),
                strokeWidth = strokeWidth,
            )

            drawLine(
                color = color,
                start = androidx.compose.ui.geometry.Offset(left, top + minSize),
                end = androidx.compose.ui.geometry.Offset(left + cornerLength, top + minSize),
                strokeWidth = strokeWidth,
            )
            drawLine(
                color = color,
                start = androidx.compose.ui.geometry.Offset(left, top + minSize),
                end = androidx.compose.ui.geometry.Offset(left, top + minSize - cornerLength),
                strokeWidth = strokeWidth,
            )

            drawLine(
                color = color,
                start = androidx.compose.ui.geometry.Offset(left + minSize, top + minSize),
                end =
                    androidx.compose.ui.geometry.Offset(
                        left + minSize - cornerLength,
                        top + minSize,
                    ),
                strokeWidth = strokeWidth,
            )
            drawLine(
                color = color,
                start = androidx.compose.ui.geometry.Offset(left + minSize, top + minSize),
                end =
                    androidx.compose.ui.geometry.Offset(
                        left + minSize,
                        top + minSize - cornerLength,
                    ),
                strokeWidth = strokeWidth,
            )
        }
    }
}
