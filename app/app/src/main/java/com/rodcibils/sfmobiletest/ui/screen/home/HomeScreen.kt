package com.rodcibils.sfmobiletest.ui.screen.home

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Face
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.rodcibils.sfmobiletest.R
import com.rodcibils.sfmobiletest.ui.common.CustomTopAppBar

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    onNavigateToQRCode: () -> Unit,
    onNavigateToScan: () -> Unit,
) {
    var isExpanded by remember { mutableStateOf(false) }

    Scaffold(
        topBar = {
            CustomTopAppBar(stringResource(R.string.home))
        },
        floatingActionButton = {
            Column(
                horizontalAlignment = Alignment.End,
                verticalArrangement = Arrangement.Bottom,
                modifier = Modifier.padding(bottom = 16.dp, end = 16.dp),
            ) {
                HomeOption(
                    isExpanded,
                    stringResource(R.string.scan),
                    onPress = onNavigateToScan,
                    icon = Icons.Default.Search,
                )
                HomeOption(
                    isExpanded,
                    stringResource(R.string.qr_code),
                    onPress = onNavigateToQRCode,
                    icon = Icons.Default.Face,
                )
                FloatingActionButton(onClick = { isExpanded = !isExpanded }) {
                    Icon(Icons.Default.Add, contentDescription = "Add")
                }
            }
        },
        content = { innerPadding ->
            Box(
                modifier =
                    Modifier
                        .fillMaxSize()
                        .padding(innerPadding),
            )
        },
    )
}

@Preview(showBackground = true)
@Composable
private fun HomeScreenPreview() {
    MaterialTheme {
        HomeScreen({}, {})
    }
}
