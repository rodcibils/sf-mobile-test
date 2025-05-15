package com.rodcibils.sfmobiletest.ui.screen.qrcode

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview

@Composable
fun QRCodeScreen() {
    Text("This is QR Code Screen!")
}

@Preview(showBackground = true)
@Composable
private fun QRCodeScreenPreview() {
    MaterialTheme {
        QRCodeScreen()
    }
}
