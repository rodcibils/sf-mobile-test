package com.rodcibils.sfmobiletest.ui.screen.scan

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.rodcibils.sfmobiletest.R

@Composable
internal fun ResultContent(
    message: String,
    onReset: () -> Unit,
) {
    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Text(
                text = message,
                style = MaterialTheme.typography.titleMedium,
                modifier = Modifier.padding(16.dp),
            )
            Button(onClick = onReset) {
                Text(stringResource(R.string.reset_scanner))
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun ResultContentPreview() {
    MaterialTheme {
        ResultContent(message = "Preview test message") {
            /**
             * Nothing to do here
             */
        }
    }
}
