package com.bubble.passwrosoft.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.bubble.passwrosoft.data.entity.VaultItem
import com.bubble.passwrosoft.di.ServiceLocator
import com.bubble.passwrosoft.ui.components.GlossyBubble
import com.bubble.passwrosoft.ui.theme.BubblePinkStart
import com.bubble.passwrosoft.ui.theme.BubblePinkStartLight

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    items: List<VaultItem>,
    onAddClick: () -> Unit,
    onItemClick: (Long) -> Unit,
) {
    val context = LocalContext.current
    val prefsManager = remember { ServiceLocator.preferences(context) }
    val isDarkTheme by prefsManager.isDarkTheme.collectAsStateWithLifecycle(initialValue = true)
    
    val (query, setQuery) = remember { mutableStateOf("") }
    val (filter, setFilter) = remember { mutableStateOf(0) } // 0=All, 1=Most used, 2=Favorites
    
    val filtered = remember(items, query, filter) {
        val searched = items.filter { it.service.contains(query, ignoreCase = true) }
        when (filter) {
            1 -> searched.sortedByDescending { it.useCount } // Most used
            2 -> searched.filter { it.favorite } // Favorites only
            else -> searched // All
        }
    }
    
    val textColor = if (isDarkTheme) Color.White else Color.Black
    val accentColor = if (isDarkTheme) BubblePinkStart else BubblePinkStartLight

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp, vertical = 12.dp)
    ) {
        Text(
            text = "My Bubble Vault",
            style = MaterialTheme.typography.headlineMedium,
            fontWeight = FontWeight.Bold,
            color = textColor,
            modifier = Modifier.padding(bottom = 16.dp, top = 8.dp)
        )

        OutlinedTextField(
            value = query,
            onValueChange = setQuery,
            placeholder = { Text("Search passwords", color = if (isDarkTheme) Color.Gray else Color.Gray) },
            leadingIcon = {
                Icon(
                    Icons.Filled.Search,
                    contentDescription = null,
                    tint = textColor.copy(alpha = 0.7f)
                )
            },
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 12.dp),
            colors = OutlinedTextFieldDefaults.colors(
                focusedTextColor = textColor,
                unfocusedTextColor = textColor,
                focusedBorderColor = accentColor,
                unfocusedBorderColor = textColor.copy(alpha = 0.3f),
                cursorColor = accentColor
            ),
            singleLine = true
        )

        androidx.compose.foundation.layout.Row(
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            modifier = Modifier.padding(bottom = 12.dp)
        ) {
            FilterChip(
                selected = filter == 0,
                onClick = { setFilter(0) },
                label = { Text("All") },
                colors = FilterChipDefaults.filterChipColors(
                    selectedContainerColor = accentColor.copy(alpha = 0.3f),
                    selectedLabelColor = if (isDarkTheme) Color.White else accentColor,
                    labelColor = textColor.copy(alpha = 0.7f),
                    containerColor = if (isDarkTheme) Color.Transparent else Color.White.copy(alpha = 0.5f)
                )
            )
            FilterChip(
                selected = filter == 1,
                onClick = { setFilter(1) },
                label = { Text("Most used") },
                colors = FilterChipDefaults.filterChipColors(
                    selectedContainerColor = accentColor.copy(alpha = 0.3f),
                    selectedLabelColor = if (isDarkTheme) Color.White else accentColor,
                    labelColor = textColor.copy(alpha = 0.7f),
                    containerColor = if (isDarkTheme) Color.Transparent else Color.White.copy(alpha = 0.5f)
                )
            )
            FilterChip(
                selected = filter == 2,
                onClick = { setFilter(2) },
                label = { Text("Favorites") },
                colors = FilterChipDefaults.filterChipColors(
                    selectedContainerColor = accentColor.copy(alpha = 0.3f),
                    selectedLabelColor = if (isDarkTheme) Color.White else accentColor,
                    labelColor = textColor.copy(alpha = 0.7f),
                    containerColor = if (isDarkTheme) Color.Transparent else Color.White.copy(alpha = 0.5f)
                )
            )
        }

        if (filtered.isEmpty() && items.isEmpty()) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(vertical = 32.dp),
                contentAlignment = Alignment.Center
            ) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    GlossyBubble(bubbleSize = 100.dp, label = "+", onClick = onAddClick, glow = true)
                    Text(
                        text = "No passwords yet",
                        style = MaterialTheme.typography.titleLarge,
                        color = textColor,
                        fontWeight = FontWeight.SemiBold
                    )
                    Text(
                        text = "Tap + to add your first password",
                        style = MaterialTheme.typography.bodyMedium,
                        color = textColor.copy(alpha = 0.7f)
                    )
                }
            }
        } else {
            LazyVerticalGrid(
                columns = GridCells.Adaptive(minSize = 90.dp),
                contentPadding = PaddingValues(vertical = 8.dp),
                verticalArrangement = Arrangement.spacedBy(20.dp),
                horizontalArrangement = Arrangement.spacedBy(16.dp),
                modifier = Modifier.fillMaxSize()
            ) {
                items(filtered) { item ->
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        GlossyBubble(
                            bubbleSize = 72.dp,
                            label = item.service.take(2).uppercase(),
                            onClick = { onItemClick(item.id) },
                            glow = item.favorite // Glow for favorites
                        )
                        Text(
                            text = item.service,
                            style = MaterialTheme.typography.labelMedium,
                            color = textColor,
                            fontSize = 13.sp,
                            maxLines = 1
                        )
                    }
                }
                item {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        GlossyBubble(
                            bubbleSize = 72.dp,
                            label = "+",
                            onClick = onAddClick,
                            glow = true
                        )
                        Text(
                            text = "Add",
                            style = MaterialTheme.typography.labelMedium,
                            color = textColor,
                            fontSize = 13.sp
                        )
                    }
                }
            }
        }
    }
}
