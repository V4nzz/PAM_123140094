package com.example.demop4app

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material.icons.filled.List
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.demop4app.navigation.AppNavHost
import com.example.demop4app.navigation.Route
import com.example.demop4app.viewmodel.NoteViewModel

/** Deskripsi item bottom navigation bar */
private data class BottomNavItem(
    val label: String,
    val icon: ImageVector,
    val route: String
)

private val bottomNavItems = listOf(
    BottomNavItem(label = "Notes",     icon = Icons.Default.List,           route = Route.NoteList.route),
    BottomNavItem(label = "Favorites", icon = Icons.Default.FavoriteBorder, route = Route.Favorites.route),
    BottomNavItem(label = "Profile",   icon = Icons.Default.Person,          route = Route.Profile.route)
)

@Composable
fun App() {
    MaterialTheme {
        // ViewModel tunggal — dibagikan ke seluruh NavHost
        val noteViewModel: NoteViewModel = viewModel { NoteViewModel() }
        val navController = rememberNavController()

        val currentBackStackEntry by navController.currentBackStackEntryAsState()
        val currentRoute = currentBackStackEntry?.destination?.route

        // Route-route yang menyembunyikan bottom nav
        val fullScreenRoutes = setOf(
            Route.NoteDetail.ROUTE_PATTERN,
            Route.AddNote.route,
            Route.EditNote.ROUTE_PATTERN
        )

        Scaffold(
            modifier = Modifier.fillMaxSize(),
            bottomBar = {
                if (currentRoute !in fullScreenRoutes) {
                    NavigationBar {
                        bottomNavItems.forEach { item ->
                            NavigationBarItem(
                                selected = currentRoute == item.route,
                                onClick = {
                                    navController.navigate(item.route) {
                                        popUpTo(Route.NoteList.route) { saveState = true }
                                        launchSingleTop = true
                                        restoreState = true
                                    }
                                },
                                icon = {
                                    Icon(
                                        imageVector = item.icon,
                                        contentDescription = item.label
                                    )
                                },
                                label = { Text(item.label) }
                            )
                        }
                    }
                }
            }
        ) { innerPadding ->
            AppNavHost(
                navController = navController,
                viewModel = noteViewModel,
                modifier = Modifier.padding(innerPadding)
            )
        }
    }
}