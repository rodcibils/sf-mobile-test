package com.rodcibils.sfmobiletest.ui.screen.qrcode

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import com.rodcibils.sfmobiletest.R
import com.rodcibils.sfmobiletest.util.DateUtils
import kotlinx.coroutines.delay

@Composable
internal fun ExpirationCountdown(
    expiresAt: String,
    modifier: Modifier = Modifier,
) {
    var countdownText by remember(expiresAt) { mutableStateOf("") }

    LaunchedEffect(expiresAt) {
        while (true) {
            countdownText = DateUtils.calculateCountdownText(expiresAt)
            delay(1000)
        }
    }

    Text(
        text = stringResource(R.string.countdown, countdownText),
        style = MaterialTheme.typography.bodyLarge,
        modifier = modifier,
    )
}
