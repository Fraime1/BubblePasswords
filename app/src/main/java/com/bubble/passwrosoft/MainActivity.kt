package com.bubble.passwrosoft

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.TileMode
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.compose.rememberNavController
import com.bubble.passwrosoft.di.ServiceLocator
import com.bubble.passwrosoft.navigation.AppNavHost
import com.bubble.passwrosoft.ui.components.BottomBar
import com.bubble.passwrosoft.ui.theme.BubbleBgEnd
import com.bubble.passwrosoft.ui.theme.BubbleBgLightEnd
import com.bubble.passwrosoft.ui.theme.BubbleBgLightStart
import com.bubble.passwrosoft.ui.theme.BubbleBgStart
import com.bubble.passwrosoft.ui.theme.BubblePasswordsTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        
        // Белые иконки статус-бара на тёмном фоне
        enableEdgeToEdge(
            statusBarStyle = androidx.activity.SystemBarStyle.dark(
                android.graphics.Color.TRANSPARENT
            ),
            navigationBarStyle = androidx.activity.SystemBarStyle.dark(
                android.graphics.Color.TRANSPARENT
            )
        )
        
        setContent {
            val context = LocalContext.current
            val prefsManager = remember { ServiceLocator.preferences(context) }
            val isDarkTheme by prefsManager.isDarkTheme.collectAsStateWithLifecycle(initialValue = true)
            
            BubblePasswordsTheme(darkTheme = isDarkTheme, dynamicColor = false) {
                AppRoot()
            }
        }
    }
}

@Composable
fun AppRoot() {
    val context = LocalContext.current
    val prefsManager = remember { ServiceLocator.preferences(context) }
    val isDarkTheme by prefsManager.isDarkTheme.collectAsStateWithLifecycle(initialValue = true)
    val navController = rememberNavController()
    
    val bgColors = if (isDarkTheme) {
        listOf(BubbleBgStart, BubbleBgEnd)
    } else {
        listOf(BubbleBgLightStart, BubbleBgLightEnd)
    }
    
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                brush = Brush.verticalGradient(
                    colors = bgColors,
                    tileMode = TileMode.Clamp
                )
            )
    ) {
        Scaffold(
            containerColor = Color.Transparent,
            contentColor = Color.White,
            bottomBar = { BottomBar(navController) }
        ) { padding ->
            Box(modifier = Modifier.padding(padding)) {
                AppNavHost(navController = navController)
            }
        }
    }
}
