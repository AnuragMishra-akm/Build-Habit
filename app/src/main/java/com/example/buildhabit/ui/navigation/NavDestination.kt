package com.example.buildhabit.ui.navigation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.BarChart
import androidx.compose.ui.graphics.vector.ImageVector
import kotlinx.serialization.Serializable

@Serializable
sealed class NavDestination {
    @Serializable
    data object Home : NavDestination()
    
    @Serializable
    data object CreateHabit : NavDestination()
    
    @Serializable
    data object TrackHabits : NavDestination()
    
    @Serializable
    data class HabitDetail(val habitId: Long) : NavDestination()

    companion object {
        // Use a lazy property or a function to avoid null initialization issues
        val bottomNavItems by lazy {
            listOf(
                BottomNavItem("Home", Home, Icons.Default.Home),
                BottomNavItem("Track", TrackHabits, Icons.Default.BarChart)
            )
        }
    }
}

data class BottomNavItem(
    val label: String,
    val destination: NavDestination,
    val icon: ImageVector
)
