package com.example.metro_mate.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import com.example.metromate.ui.theme.Black
import com.example.metromate.ui.theme.BottomNavColor
import com.example.metromate.ui.theme.ColorPrimary
import com.example.metromate.ui.theme.ColorPrimaryAccent
import com.example.metromate.ui.theme.ColorPrimaryVariant
import com.example.metromate.ui.theme.ColorSecondary
import com.example.metromate.ui.theme.White

private val DarkColorScheme = darkColorScheme(
    primary = ColorPrimaryVariant,
    onPrimary = White,
    secondary = ColorSecondary,
    onSecondary = Black,
    background = Black,
    onBackground = White,
    surface = ColorPrimaryAccent,
    onSurface = White
)

private val LightColorScheme = lightColorScheme(
    primary = ColorPrimary,
    onPrimary = Black,
    secondary = ColorSecondary,
    onSecondary = Black,
    background = White,
    onBackground = Black,
    surface = BottomNavColor,
    onSurface = Black
)

@Composable
fun MetroMateTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    val colors = if (darkTheme) DarkColorScheme else LightColorScheme

    MaterialTheme(
        colorScheme = colors,
        typography = Typography,
        content = content
    )
}
