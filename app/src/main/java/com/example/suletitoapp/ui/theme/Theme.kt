package com.example.suletitoapp.ui.theme

import androidx.compose.material3.*
import androidx.compose.runtime.Composable

private val LightColors = lightColorScheme(
    primary = TealPrimary,
    onPrimary = White,
    secondary = TealSecondary,
    onSecondary = White,
    background = White,
    onBackground = Black,
    surface = White,
    onSurface = Black
)

private val DarkColors = darkColorScheme(
    primary = TealPrimary,
    onPrimary = White,
    secondary = TealSecondary,
    onSecondary = Black,
    background = Black,
    onBackground = White,
    surface = Black,
    onSurface = White
)

@Composable
fun SuletitoAppTheme(
    darkTheme: Boolean = false, // puedes leer del sistema si quieres
    content: @Composable () -> Unit
) {
    val colors = if (darkTheme) DarkColors else LightColors

    MaterialTheme(
        colorScheme = colors,
        typography = Typography,
        content = content
    )
}
