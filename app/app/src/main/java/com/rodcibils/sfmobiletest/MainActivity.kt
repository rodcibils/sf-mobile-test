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
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.rodcibils.sfmobiletest.ui.screen.home.HomeScreen
import com.rodcibils.sfmobiletest.ui.screen.qrcode.QRCodeScreen
import com.rodcibils.sfmobiletest.ui.screen.scan.ScanScreen
import com.rodcibils.sfmobiletest.ui.theme.SFMobileTestTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            SFMobileTestTheme {
                val navController = rememberNavController()

                Surface(color = MaterialTheme.colorScheme.background) {
                    NavHost(
                        navController = navController,
                        startDestination = Screen.Home.route,
                    ) {
                        composable(Screen.Home.route) {
                            HomeScreen(
                                onNavigateToQRCode = {
                                    navController.navigate(
                                        Screen.QRCode.route,
                                    )
                                },
                                onNavigateToScan = { navController.navigate(Screen.Scan.route) },
                            )
                        }
                        composable(Screen.QRCode.route) {
                            QRCodeScreen(onBack = { navController.popBackStack() })
                        }
                        composable(Screen.Scan.route) {
                            ScanScreen(onBack = { navController.popBackStack() })
                        }
                    }
                }
            }
        }
    }
}

private sealed class Screen(val route: String) {
    data object Home : Screen("home")

    data object QRCode : Screen("qr_code")

    data object Scan : Screen("scan")
}
