/**
 * Allow PascalCase for Jetpack Compose functions in this file
 * TODO: Look for another linter that has better Jetpack Compose support
 */
@file:Suppress("FunctionName")

package com.rodcibils.sfmobiletest

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.rodcibils.sfmobiletest.ui.screen.home.HomeScreen
import com.rodcibils.sfmobiletest.ui.theme.SFMobileTestTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            SFMobileTestTheme {
                HomeScreen()
            }
        }
    }
}
