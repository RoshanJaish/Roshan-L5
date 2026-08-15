package com.example.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val DarkColorScheme = darkColorScheme(
    primary = BrandPurpleLight,
    onPrimary = Color.White,
    primaryContainer = BrandPurpleDark,
    onPrimaryContainer = Color.White,
    secondary = BrandYellow,
    onSecondary = TextPrimary,
    tertiary = BrandCyan,
    background = Color(0xFF141218),
    surface = Color(0xFF1D1B20),
    surfaceVariant = Color(0xFF2B2830),
    onBackground = Color(0xFFE6E1E5),
    onSurface = Color(0xFFE6E1E5),
    outline = Color(0xFF49454F)
)

private val LightColorScheme = lightColorScheme(
    primary = BrandPurple,
    onPrimary = Color.White,
    primaryContainer = Color(0xFFEADDFF),
    onPrimaryContainer = Color(0xFF21005D),
    secondary = BrandYellow,
    onSecondary = TextPrimary,
    secondaryContainer = BrandYellowLight,
    onSecondaryContainer = Color(0xFF594100),
    tertiary = BrandCyan,
    background = BgSoft,
    surface = CardWhite,
    surfaceVariant = CardSubtle,
    onBackground = TextPrimary,
    onSurface = TextPrimary,
    outline = DividerColor
)

@Composable
fun MyApplicationTheme(
    darkTheme: Boolean = false, // Keep the crisp vibrant UI look as default
    content: @Composable () -> Unit
) {
    val colorScheme = if (darkTheme) DarkColorScheme else LightColorScheme
    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}

