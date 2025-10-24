package com.bubble.passwrosoft.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.BarChart
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.Tune
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import com.bubble.passwrosoft.navigation.Routes
import com.bubble.passwrosoft.ui.theme.BubblePinkStart

@Composable
fun BottomBar(navController: NavHostController) {
    val backStackEntry by navController.currentBackStackEntryAsState()
    val current = backStackEntry?.destination?.route
    
    NavigationBar(
        containerColor = Color(0xFF0A0A14),
        contentColor = Color.White,
        tonalElevation = 8.dp
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 8.dp, vertical = 4.dp),
            horizontalArrangement = Arrangement.SpaceEvenly,
            verticalAlignment = Alignment.CenterVertically
        ) {
            NavItem(
                label = "Vault",
                route = Routes.HOME,
                selected = current == Routes.HOME,
                navController = navController
            ) {
                Icon(
                    Icons.Filled.Home,
                    contentDescription = null,
                    modifier = Modifier.size(24.dp)
                )
            }
            NavItem(
                label = "Gen",
                route = Routes.GENERATOR,
                selected = current == Routes.GENERATOR,
                navController = navController
            ) {
                Icon(
                    Icons.Filled.Tune,
                    contentDescription = null,
                    modifier = Modifier.size(24.dp)
                )
            }
            NavItem(
                label = "Stats",
                route = Routes.STATS,
                selected = current == Routes.STATS,
                navController = navController
            ) {
                Icon(
                    Icons.Filled.BarChart,
                    contentDescription = null,
                    modifier = Modifier.size(24.dp)
                )
            }
            NavItem(
                label = "Settings",
                route = Routes.SETTINGS,
                selected = current == Routes.SETTINGS,
                navController = navController
            ) {
                Icon(
                    Icons.Filled.Settings,
                    contentDescription = null,
                    modifier = Modifier.size(24.dp)
                )
            }
        }
    }
}

@Composable
private fun NavItem(
    label: String,
    route: String,
    selected: Boolean,
    navController: NavHostController,
    icon: @Composable () -> Unit
) {
    Box(
        modifier = Modifier
            .clip(RoundedCornerShape(12.dp))
            .background(
                if (selected) BubblePinkStart.copy(alpha = 0.2f) else Color.Transparent
            )
            .clickable {
                if (!selected) navController.navigate(route) {
                    launchSingleTop = true
                    restoreState = true
                    popUpTo(Routes.HOME) { saveState = true }
                }
            }
            .padding(horizontal = 12.dp, vertical = 8.dp),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            Box(
                modifier = Modifier.size(24.dp),
                contentAlignment = Alignment.Center
            ) {
                icon()
            }
            Text(
                text = label,
                color = if (selected) Color.White else Color.White.copy(alpha = 0.6f),
                fontSize = 11.sp,
                fontWeight = if (selected) FontWeight.Bold else FontWeight.Normal
            )
        }
    }
}
