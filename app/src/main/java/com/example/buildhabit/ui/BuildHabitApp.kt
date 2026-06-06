package com.example.buildhabit.ui

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavDestination.Companion.hasRoute
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.toRoute
import com.example.buildhabit.HabitApplication
import com.example.buildhabit.ui.navigation.NavDestination
import com.example.buildhabit.ui.screens.CreateHabitScreen
import com.example.buildhabit.ui.screens.HabitDetailScreen
import com.example.buildhabit.ui.screens.HomeScreen
import com.example.buildhabit.ui.screens.TrackHabitScreen
import com.example.buildhabit.ui.viewmodel.ViewModelFactory

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BuildHabitApp() {
    val context = LocalContext.current
    val repository = (context.applicationContext as HabitApplication).habitRepository
    val navController = rememberNavController()
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentDestination = navBackStackEntry?.destination

    val showBottomBar = listOf(
        NavDestination.Home::class,
        NavDestination.TrackHabits::class
    ).any { route ->
        currentDestination?.hierarchy?.any { it.hasRoute(route) } == true
    }

    Scaffold(
        topBar = {
            val title = when {
                currentDestination?.hasRoute(NavDestination.Home::class) == true -> "Build Habit"
                currentDestination?.hasRoute(NavDestination.TrackHabits::class) == true -> "Build Habit"
                currentDestination?.hasRoute(NavDestination.CreateHabit::class) == true -> "Build Habit"
                currentDestination?.hasRoute(NavDestination.HabitDetail::class) == true -> "Habit Details"
                else -> "Build Habit"
            }

            TopAppBar(
                title = {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        if (!showBottomBar) {
                            IconButton(onClick = { navController.popBackStack() }) {
                                Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                            }
                        } else {
                            Surface(
                                modifier = Modifier.size(36.dp),
                                shape = CircleShape,
                                color = Color.Gray
                            ) { /* Avatar */ }
                            Spacer(modifier = Modifier.width(12.dp))
                        }
                        Text(
                            text = title,
                            style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold)
                        )
                    }
                },
                actions = {
                    IconButton(onClick = { /* TODO */ }) {
                        Icon(Icons.Default.Settings, contentDescription = "Settings")
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.background,
                    scrolledContainerColor = MaterialTheme.colorScheme.background
                ),
                windowInsets = TopAppBarDefaults.windowInsets // Restored to respect status bars
            )
        },
        bottomBar = {
            if (showBottomBar) {
                BottomAppBar(
                    containerColor = MaterialTheme.colorScheme.surface,
                    contentColor = MaterialTheme.colorScheme.primary,
                    tonalElevation = 8.dp,
                    windowInsets = BottomAppBarDefaults.windowInsets, // Restored to respect navigation bars
                    actions = {
                        val homeItem = NavDestination.bottomNavItems[0]
                        val trackItem = NavDestination.bottomNavItems[1]

                        // Home Icon
                        val homeSelected = currentDestination?.hierarchy?.any { it.hasRoute(homeItem.destination::class) } == true
                        Box(modifier = Modifier.weight(1f), contentAlignment = Alignment.Center) {
                            IconButton(
                                onClick = {
                                    navController.navigate(homeItem.destination) {
                                        popUpTo(navController.graph.findStartDestination().id) { saveState = true }
                                        launchSingleTop = true
                                        restoreState = true
                                    }
                                }
                            ) {
                                Icon(
                                    homeItem.icon, 
                                    contentDescription = homeItem.label,
                                    tint = if (homeSelected) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurfaceVariant
                                )
                            }
                        }

                        // Center FAB
                        Box(modifier = Modifier.weight(1f), contentAlignment = Alignment.Center) {
                            FloatingActionButton(
                                onClick = { navController.navigate(NavDestination.CreateHabit) },
                                containerColor = MaterialTheme.colorScheme.primary,
                                contentColor = MaterialTheme.colorScheme.onPrimary,
                                shape = RoundedCornerShape(18.dp),
                                elevation = FloatingActionButtonDefaults.elevation(0.dp) // Flat inside the bar
                            ) {
                                Icon(Icons.Default.Add, contentDescription = "Add Habit")
                            }
                        }

                        // Track Icon
                        val trackSelected = currentDestination?.hierarchy?.any { it.hasRoute(trackItem.destination::class) } == true
                        Box(modifier = Modifier.weight(1f), contentAlignment = Alignment.Center) {
                            IconButton(
                                onClick = {
                                    navController.navigate(trackItem.destination) {
                                        popUpTo(navController.graph.findStartDestination().id) { saveState = true }
                                        launchSingleTop = true
                                        restoreState = true
                                    }
                                }
                            ) {
                                Icon(
                                    trackItem.icon, 
                                    contentDescription = trackItem.label,
                                    tint = if (trackSelected) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurfaceVariant
                                )
                            }
                        }
                    }
                )
            }
        },
    ) { innerPadding ->
        NavHost(
            navController = navController,
            startDestination = NavDestination.Home,
            modifier = Modifier.padding(innerPadding)
        ) {
            composable<NavDestination.Home> {
                val viewModel: com.example.buildhabit.ui.viewmodel.HomeViewModel = viewModel(
                    factory = ViewModelFactory(repository)
                )
                HomeScreen(
                    viewModel = viewModel,
                    onSeeAllHabits = { navController.navigate(NavDestination.TrackHabits) },
                    onHabitClick = { habitId -> navController.navigate(NavDestination.HabitDetail(habitId)) }
                )
            }
            composable<NavDestination.CreateHabit> {
                val viewModel: com.example.buildhabit.ui.viewmodel.CreateHabitViewModel = viewModel(
                    factory = ViewModelFactory(repository)
                )
                CreateHabitScreen(
                    viewModel = viewModel,
                    onHabitCreated = { navController.popBackStack() }
                )
            }
            composable<NavDestination.TrackHabits> {
                val viewModel: com.example.buildhabit.ui.viewmodel.TrackHabitViewModel = viewModel(
                    factory = ViewModelFactory(repository)
                )
                TrackHabitScreen(
                    viewModel = viewModel,
                    onHabitClick = { habitId -> navController.navigate(NavDestination.HabitDetail(habitId)) }
                )
            }
            composable<NavDestination.HabitDetail> { backStackEntry ->
                val detailRoute: NavDestination.HabitDetail = backStackEntry.toRoute()
                val viewModel: com.example.buildhabit.ui.viewmodel.HabitDetailViewModel = viewModel(
                    key = "detail_${detailRoute.habitId}",
                    factory = ViewModelFactory(repository, detailRoute.habitId)
                )
                HabitDetailScreen(
                    viewModel = viewModel,
                    onHabitDeleted = { navController.popBackStack() }
                )
            }
        }
    }
}
