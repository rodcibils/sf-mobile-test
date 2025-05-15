package com.rodcibils.sfmobiletest.ui.screen.scan

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview

@Composable
fun ScanScreen() {
    Text("This is Scan Screen!")
}

@Preview(showBackground = true)
@Composable
private fun ScanScreenPreview() {
    MaterialTheme {
        ScanScreen()
    }
}
