package com.rodcibils.sfmobiletest.ui.screen.qrcode

import androidx.compose.foundation.layout.Column
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview

@Composable
fun QRCodeScreen(onBack: () -> Unit) {
    Column {
        Text("This is QR Code Screen!")
        Button(onClick = onBack) { Text("Back from QRCodeScreen") }
    }
}

@Preview(showBackground = true)
@Composable
private fun QRCodeScreenPreview() {
    MaterialTheme {
        QRCodeScreen({})
    }
}
