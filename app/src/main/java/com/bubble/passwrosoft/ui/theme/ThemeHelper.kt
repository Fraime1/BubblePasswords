package com.bubble.passwrosoft.ui.theme

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.bubble.passwrosoft.di.ServiceLocator

@Composable
fun isDarkTheme(): Boolean {
    val context = LocalContext.current
    val prefsManager = remember { ServiceLocator.preferences(context) }
    val isDark by prefsManager.isDarkTheme.collectAsStateWithLifecycle(initialValue = true)
    return isDark
}

@Composable
fun adaptiveTextColor(): Color {
    return if (isDarkTheme()) Color.White else Color.Black
}

@Composable
fun adaptiveTextColorAlpha(alpha: Float): Color {
    return if (isDarkTheme()) Color.White.copy(alpha = alpha) else Color.Black.copy(alpha = alpha)
}

