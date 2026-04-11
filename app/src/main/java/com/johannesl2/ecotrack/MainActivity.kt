package com.johannesl2.ecotrack

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.BarChart
import androidx.compose.material.icons.filled.Home
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHost
import androidx.compose.material3.Icon
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.johannesl2.ecotrack.ui.components.overlay.AddTravelSheet
import com.johannesl2.ecotrack.ui.screens.HomeScreen
import com.johannesl2.ecotrack.ui.theme.EcotrackTheme
import androidx.navigation.compose.composable
import androidx.navigation.compose.NavHost
import androidx.compose.runtime.getValue
import androidx.navigation.NavDestination.Companion.hierarchy


class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            EcotrackTheme {
                val navController = rememberNavController()

                val items = listOf(
                    NavigationItem("home", route = "home", icon = Icons.Default.Home),
                    NavigationItem("add_trip", route = "add_trip", icon = Icons.Default.Add),
                    NavigationItem("stats", route = "stats", icon = Icons.Default.BarChart)
                )

                Scaffold(
                    bottomBar = {
                        NavigationBar {
                            val navBackStackEntry by navController.currentBackStackEntryAsState()
                            val currentDestination = navBackStackEntry?.destination

                            items.forEach { item ->
                                NavigationBarItem(
                                    icon = { Icon(item.icon, contentDescription = item.label) },
                                    label = { Text(item.label) },
                                    selected = currentDestination?.hierarchy?.any { it.route == item.route } == true,
                                    onClick = {
                                        navController.navigate(item.route) {
                                            popUpTo(navController.graph.findStartDestination().id) {
                                                saveState = true
                                            }
                                            launchSingleTop = true
                                            restoreState = true
                                        }
                                    }
                                )
                            }
                        }
                    }
                ) { innerPadding ->
                    NavHost(
                        navController = navController,
                        startDestination = "home",
                        modifier = Modifier.padding(innerPadding)
                    ) {
                        composable("home") { HomeScreen() }
                        composable("add_trip") { AddTravelSheet(
                            transportType = "Bil", // Eller vad du vill ha som standard
                            onDismiss = { navController.popBackStack() },
                            onSave = {
                                navController.popBackStack()
                                }
                            )
                        }
                        composable("stats") {
                            Text("This view is going to show stats")
                        }
                    }
                }
            }
        }
    }
}
data class NavigationItem(val label: String, val route: String, val icon: androidx.compose.ui.graphics.vector.ImageVector)