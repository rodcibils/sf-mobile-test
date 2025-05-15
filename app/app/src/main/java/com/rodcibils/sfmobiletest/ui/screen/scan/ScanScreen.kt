package com.rodcibils.sfmobiletest.ui.screen.scan

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import com.rodcibils.sfmobiletest.R
import com.rodcibils.sfmobiletest.ui.common.CustomTopAppBar

@Composable
fun ScanScreen(onBack: (() -> Unit)? = null) {
    Scaffold(
        topBar = {
            CustomTopAppBar(stringResource(R.string.scan), onBackPressed = onBack)
        },
    ) { innerPadding ->
        Column(
            modifier =
                Modifier
                    .fillMaxSize()
                    .padding(innerPadding),
        ) {
            Text("This is Scan Screen!")
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
