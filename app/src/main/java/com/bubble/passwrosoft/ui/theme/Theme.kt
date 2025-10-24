package com.bubble.passwrosoft.ui.theme

import android.app.Activity
import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.ColorScheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.graphics.Color

private val DarkColorScheme: ColorScheme = darkColorScheme(
    primary = BubblePinkStart,
    onPrimary = Color.White,
    secondary = BubbleAccentGlow,
    onSecondary = Color.Black,
    tertiary = BubblePinkEnd,
    background = Color(0xFF0E0B15),
    onBackground = Color(0xFFEAE8F5),
    surface = Color(0xFF0E0B15),
    onSurface = Color(0xFFEAE8F5)
)

private val LightColorScheme: ColorScheme = lightColorScheme(
    primary = BubblePinkStartLight,
    onPrimary = Color.White,
    secondary = BubbleAccentGlowLight,
    onSecondary = Color.White,
    tertiary = BubblePinkEndLight,
    background = BubbleBgLightStart,
    onBackground = Color.Black,
    surface = BubbleBgLightEnd,
    onSurface = Color.Black
)

@Composable
fun BubblePasswordsTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    // Dynamic color is available on Android 12+
    dynamicColor: Boolean = true,
    content: @Composable () -> Unit
) {
    val colorScheme = when {
        dynamicColor && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S -> {
            val context = LocalContext.current
            if (darkTheme) dynamicDarkColorScheme(context) else dynamicLightColorScheme(context)
        }

        darkTheme -> DarkColorScheme
        else -> LightColorScheme
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}