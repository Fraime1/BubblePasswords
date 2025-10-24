package com.bubble.passwrosoft.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.bubble.passwrosoft.di.ServiceLocator
import com.bubble.passwrosoft.ui.screens.AddPasswordScreen
import com.bubble.passwrosoft.ui.screens.DetailScreen
import com.bubble.passwrosoft.ui.screens.EditPasswordScreen
import com.bubble.passwrosoft.ui.screens.GeneratorScreen
import com.bubble.passwrosoft.ui.screens.HomeScreen
import com.bubble.passwrosoft.ui.screens.SettingsScreen
import com.bubble.passwrosoft.ui.screens.StatsScreen
import com.bubble.passwrosoft.util.PasswordGenerator
import kotlinx.coroutines.launch

object Routes {
    const val HOME = "home"
    const val DETAIL = "detail/{id}"
    const val EDIT = "edit/{id}"
    const val ADD = "add"
    const val GENERATOR = "generator"
    const val STATS = "stats"
    const val SETTINGS = "settings"
    
    fun detailRoute(id: Long) = "detail/$id"
    fun editRoute(id: Long) = "edit/$id"
}

@Composable
fun AppNavHost(
    navController: NavHostController,
) {
    val context = LocalContext.current
    val scope = rememberCoroutineScope()
    val repo = remember { ServiceLocator.repository(context) }
    
    NavHost(navController = navController, startDestination = Routes.HOME) {
        composable(Routes.HOME) {
            val items by repo.observeAll().collectAsStateWithLifecycle(initialValue = emptyList())
            
            HomeScreen(
                items = items,
                onAddClick = { navController.navigate(Routes.ADD) },
                onItemClick = { id -> navController.navigate(Routes.detailRoute(id)) }
            )
        }
        
        composable(Routes.ADD) {
            AddPasswordScreen(
                onSave = { service, username, password ->
                    scope.launch {
                        repo.add(service, username, password)
                    }
                    navController.popBackStack()
                },
                onGenerate = { len, up, low, num, sym -> 
                    PasswordGenerator.generate(len, up, low, num, sym) 
                }
            )
        }
        
        composable(Routes.GENERATOR) {
            GeneratorScreen(
                onGenerate = { len, up, low, num, sym -> 
                    PasswordGenerator.generate(len, up, low, num, sym) 
                }
            )
        }
        
        composable(Routes.STATS) {
            val items by repo.observeAll().collectAsStateWithLifecycle(initialValue = emptyList())
            val strongCount = items.count { it.password.length >= 12 }
            val mediumCount = items.count { it.password.length in 8..11 }
            val weakCount = items.count { it.password.length < 8 }
            
            StatsScreen(
                total = items.size,
                strong = strongCount,
                medium = mediumCount,
                weak = weakCount
            )
        }
        
        composable(Routes.SETTINGS) { 
            SettingsScreen() 
        }
        
        composable(
            route = Routes.DETAIL,
            arguments = listOf(navArgument("id") { type = NavType.LongType })
        ) { backStackEntry ->
            val id = backStackEntry.arguments?.getLong("id") ?: return@composable
            val item by repo.observeById(id).collectAsStateWithLifecycle(initialValue = null)
            
            item?.let { vaultItem ->
                DetailScreen(
                    service = vaultItem.service,
                    username = vaultItem.username,
                    password = vaultItem.password,
                    isFavorite = vaultItem.favorite,
                    onFavoriteToggle = {
                        scope.launch {
                            repo.update(vaultItem.copy(favorite = !vaultItem.favorite))
                        }
                    },
                    onCopy = { 
                        // Increment use count when copying password
                        scope.launch {
                            repo.update(vaultItem.copy(useCount = vaultItem.useCount + 1))
                        }
                    },
                    onEdit = { 
                        navController.navigate(Routes.editRoute(id))
                    },
                    onDelete = {
                        scope.launch {
                            repo.delete(vaultItem)
                            navController.popBackStack()
                        }
                    },
                    onShare = { }
                )
            }
        }
        
        composable(
            route = Routes.EDIT,
            arguments = listOf(navArgument("id") { type = NavType.LongType })
        ) { backStackEntry ->
            val id = backStackEntry.arguments?.getLong("id") ?: return@composable
            val item by repo.observeById(id).collectAsStateWithLifecycle(initialValue = null)
            
            item?.let { vaultItem ->
                EditPasswordScreen(
                    initialService = vaultItem.service,
                    initialUsername = vaultItem.username,
                    initialPassword = vaultItem.password,
                    onSave = { service, username, password ->
                        scope.launch {
                            repo.update(vaultItem.copy(
                                service = service,
                                username = username,
                                password = password
                            ))
                            navController.popBackStack()
                        }
                    },
                    onCancel = {
                        navController.popBackStack()
                    },
                    onGenerate = { len, up, low, num, sym -> 
                        PasswordGenerator.generate(len, up, low, num, sym) 
                    }
                )
            }
        }
    }
}
