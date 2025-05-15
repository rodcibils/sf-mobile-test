package com.rodcibils.sfmobiletest.ui.screen.scan

import androidx.compose.foundation.layout.Column
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview

@Composable
fun ScanScreen(onBack: () -> Unit) {
    Column {
        Text("This is Scan Screen!")
        Button(onClick = onBack) { Text("Back from ScanScreen") }
    }
}

@Preview(showBackground = true)
@Composable
private fun ScanScreenPreview() {
    MaterialTheme {
        ScanScreen({})
    }
}
