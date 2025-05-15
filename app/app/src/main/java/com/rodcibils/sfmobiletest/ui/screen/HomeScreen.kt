package com.rodcibils.sfmobiletest.ui.screen

import androidx.compose.animation.*
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen() {
    var isExpanded by remember { mutableStateOf(false) }

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                        Text(
                            text = "Home",
                            fontSize = 20.sp,
                            color = MaterialTheme.colorScheme.onPrimary,
                        )
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primary
                )
            )
        },
        floatingActionButton = {
            Column(
                horizontalAlignment = Alignment.End,
                verticalArrangement = Arrangement.Bottom,
                modifier = Modifier.padding(bottom = 16.dp, end = 16.dp)
            ) {
                AnimatedVisibility(
                    visible = isExpanded,
                    enter = fadeIn() + slideInVertically(),
                    exit = fadeOut() + slideOutVertically()
                ) {
                    FloatingActionButton(
                        onClick = { /* Handle QR Code click */ },
                        containerColor = MaterialTheme.colorScheme.secondary
                    ) {
                        Text("QR Code")
                    }
                }

                Spacer(modifier = Modifier.height(12.dp))

                AnimatedVisibility(
                    visible = isExpanded,
                    enter = fadeIn() + slideInVertically(),
                    exit = fadeOut() + slideOutVertically()
                ) {
                    FloatingActionButton(
                        onClick = { /* Handle Scan click */ },
                        containerColor = MaterialTheme.colorScheme.secondary
                    ) {
                        Text("Scan")
                    }
                }

                Spacer(modifier = Modifier.height(12.dp))

                FloatingActionButton(onClick = { isExpanded = !isExpanded }) {
                    Icon(Icons.Default.Add, contentDescription = "Add")
                }
            }
        },
        content = { innerPadding ->
            // Empty background that takes the rest of the screen
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding)
            )
        }
    )
}

@Preview(showBackground = true)
@Composable
private fun HomeScreenPreview() {
    MaterialTheme {
        HomeScreen()
    }
}
