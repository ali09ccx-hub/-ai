package com.example.ui.theme

import android.app.Activity
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalView
import androidx.core.view.WindowCompat

private val DarkColorScheme = darkColorScheme(
    primary = BayanGold,
    onPrimary = BayanNavyDark,
    primaryContainer = BayanGoldDark,
    onPrimaryContainer = BayanGoldLight,
    secondary = BayanCyanAccent,
    onSecondary = BayanNavyDark,
    secondaryContainer = BayanNavySurfaceVariant,
    onSecondaryContainer = BayanCyanAccent,
    tertiary = BayanEmeraldAccent,
    onTertiary = BayanNavyDark,
    background = BayanNavyDark,
    onBackground = TextPrimaryLight,
    surface = BayanNavyBase,
    onSurface = TextPrimaryLight,
    surfaceVariant = BayanNavySurface,
    onSurfaceVariant = TextSecondaryLight,
    outline = BayanNavyBorder,
    error = RedError,
    onError = White
)

private val LightColorScheme = lightColorScheme(
    primary = BayanGoldDark,
    onPrimary = White,
    primaryContainer = BayanGoldLight,
    onPrimaryContainer = BayanNavyDark,
    secondary = BayanNavyBase,
    onSecondary = White,
    secondaryContainer = BayanNavySurfaceVariant,
    onSecondaryContainer = White,
    tertiary = BayanEmeraldAccent,
    onTertiary = White,
    background = Color(0xFFF8FAFC),
    onBackground = TextPrimaryDark,
    surface = White,
    onSurface = TextPrimaryDark,
    surfaceVariant = Color(0xFFE2E8F0),
    onSurfaceVariant = TextSecondaryDark,
    outline = Color(0xFFCBD5E1),
    error = RedError,
    onError = White
)

@Composable
fun BayanAiTheme(
    darkTheme: Boolean = true, // Default to stunning dark AI research studio look
    content: @Composable () -> Unit
) {
    val colorScheme = if (darkTheme) DarkColorScheme else LightColorScheme
    val view = LocalView.current
    if (!view.isInEditMode) {
        SideEffect {
            val window = (view.context as? Activity)?.window
            if (window != null) {
                window.statusBarColor = colorScheme.background.toArgb()
                window.navigationBarColor = colorScheme.background.toArgb()
                WindowCompat.getInsetsController(window, view).isAppearanceLightStatusBars = !darkTheme
                WindowCompat.getInsetsController(window, view).isAppearanceLightNavigationBars = !darkTheme
            }
        }
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}
