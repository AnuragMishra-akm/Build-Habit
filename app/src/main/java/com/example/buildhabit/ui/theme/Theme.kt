package com.example.buildhabit.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.runtime.Composable

private val MidnightColorScheme = darkColorScheme(
    primary = MidnightPrimary,
    secondary = MidnightSecondary,
    background = MidnightBackground,
    surface = MidnightSurface,
    onPrimary = MidnightTextPrimary,
    onSecondary = MidnightTextPrimary,
    onBackground = MidnightTextPrimary,
    onSurface = MidnightTextPrimary,
    surfaceVariant = MidnightCard,
    onSurfaceVariant = MidnightTextSecondary,
    error = Error,
    onError = MidnightTextPrimary
)

@Composable
fun BuildHabitTheme(
    content: @Composable () -> Unit
) {
    MaterialTheme(
        colorScheme = MidnightColorScheme,
        typography = Typography,
        content = content
    )
}
