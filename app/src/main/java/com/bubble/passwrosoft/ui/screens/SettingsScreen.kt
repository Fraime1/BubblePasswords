package com.bubble.passwrosoft.ui.screens

import android.content.Intent
import android.net.Uri
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Cloud
import androidx.compose.material.icons.filled.Fingerprint
import androidx.compose.material.icons.filled.NearMe
import androidx.compose.material.icons.filled.Palette
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.bubble.passwrosoft.di.ServiceLocator
import com.bubble.passwrosoft.ui.components.GlossyBubble
import com.bubble.passwrosoft.ui.theme.BubblePinkStart
import com.bubble.passwrosoft.ui.theme.BubblePinkStartLight
import kotlinx.coroutines.launch

@Composable
fun SettingsScreen() {
    val context = LocalContext.current
    val prefsManager = remember { ServiceLocator.preferences(context) }
    val scope = rememberCoroutineScope()
    
    val isDarkTheme by prefsManager.isDarkTheme.collectAsStateWithLifecycle(initialValue = true)
    val accentColor = if (isDarkTheme) BubblePinkStart else BubblePinkStartLight
    val isBiometricEnabled by prefsManager.isBiometricEnabled.collectAsStateWithLifecycle(initialValue = false)
    val isCloudSyncEnabled by prefsManager.isCloudSyncEnabled.collectAsStateWithLifecycle(initialValue = false)

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "Profile & Settings",
            style = MaterialTheme.typography.headlineMedium,
            fontWeight = FontWeight.Bold,
            color = if (isDarkTheme) Color.White else Color.Black,
            modifier = Modifier.padding(vertical = 12.dp)
        )

        GlossyBubble(bubbleSize = 80.dp, label = "BP", glow = isDarkTheme)

        Spacer(Modifier.height(8.dp))

//        Text(
//            text = "Security",
//            style = MaterialTheme.typography.titleMedium,
//            fontWeight = FontWeight.SemiBold,
//            color = if (isDarkTheme) Color.White else Color.Black,
//            modifier = Modifier
//                .fillMaxWidth()
//                .padding(top = 8.dp)
//        )
//
//        SettingCard(
//            icon = Icons.Filled.Fingerprint,
//            title = "Biometric Lock",
//            subtitle = "Face ID / Touch ID",
//            checked = isBiometricEnabled,
//            onCheckedChange = {
//                scope.launch { prefsManager.setBiometricEnabled(it) }
//            },
//            isDarkTheme = isDarkTheme
//        )
//
//        Text(
//            text = "Sync & Backup",
//            style = MaterialTheme.typography.titleMedium,
//            fontWeight = FontWeight.SemiBold,
//            color = if (isDarkTheme) Color.White else Color.Black,
//            modifier = Modifier
//                .fillMaxWidth()
//                .padding(top = 8.dp)
//        )
//
//        SettingCard(
//            icon = Icons.Filled.Cloud,
//            title = "Cloud Sync",
//            subtitle = "Sync across devices",
//            checked = isCloudSyncEnabled,
//            onCheckedChange = {
//                scope.launch { prefsManager.setCloudSyncEnabled(it) }
//            },
//            isDarkTheme = isDarkTheme
//        )

        Text(
            text = "Appearance",
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.SemiBold,
            color = if (isDarkTheme) Color.White else Color.Black,
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 8.dp)
        )

        SettingCard(
            icon = Icons.Filled.Palette,
            title = if (isDarkTheme) "Bubble Glow Theme (Dark)" else "Light Theme",
            subtitle = if (isDarkTheme) "Neon pink bubbles on dark" else "Soft colors on light",
            checked = isDarkTheme,
            onCheckedChange = {
                scope.launch { prefsManager.setDarkTheme(it) }
            },
            isDarkTheme = isDarkTheme
        )

        Text(
            text = "Policy",
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.SemiBold,
            color = if (isDarkTheme) Color.White else Color.Black,
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 8.dp)
        )

        PolicyCard(
            icon = Icons.Filled.NearMe,
            title = "Privacy Policy",
            subtitle = "Tap to read",
            isDarkTheme = isDarkTheme
        )

        Spacer(Modifier.weight(1f))

        Text(
            text = "Bubble Passwords v1.0",
            style = MaterialTheme.typography.bodySmall,
            color = if (isDarkTheme) Color.White.copy(alpha = 0.5f) else Color.Black.copy(alpha = 0.5f)
        )

        Spacer(Modifier.height(16.dp))
    }
}

@Composable
private fun PolicyCard(
    icon: ImageVector,
    title: String,
    subtitle: String,
    isDarkTheme: Boolean
) {
    val context = LocalContext.current
    Card(
        modifier = Modifier.fillMaxWidth().clickable {
            val intent = Intent(Intent.ACTION_VIEW, Uri.parse("https://bubblepasswords.com/privacy-policy.html"))
            context.startActivity(intent)
        },
        colors = CardDefaults.cardColors(
            containerColor = if (isDarkTheme) Color(0x22FFFFFF) else Color(0xFFE8D5F2) // Светлый фиолетовый для светлой темы
        )
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row(
                horizontalArrangement = Arrangement.spacedBy(12.dp),
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.weight(1f)
            ) {
                Icon(
                    icon,
                    contentDescription = null,
                    tint = if (isDarkTheme) Color.White else Color.Black,
                    modifier = Modifier.size(28.dp)
                )
                Column {
                    Text(
                        text = title,
                        style = MaterialTheme.typography.bodyLarge,
                        color = if (isDarkTheme) Color.White else Color.Black,
                        fontWeight = FontWeight.SemiBold
                    )
                    Text(
                        text = subtitle,
                        style = MaterialTheme.typography.bodySmall,
                        color = if (isDarkTheme) Color.White.copy(alpha = 0.6f) else Color.Black.copy(alpha = 0.6f)
                    )
                }
            }
        }
    }
}

@Composable
private fun SettingCard(
    icon: ImageVector,
    title: String,
    subtitle: String,
    checked: Boolean,
    onCheckedChange: (Boolean) -> Unit,
    isDarkTheme: Boolean
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = if (isDarkTheme) Color(0x22FFFFFF) else Color(0xFFE8D5F2) // Светлый фиолетовый для светлой темы
        )
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row(
                horizontalArrangement = Arrangement.spacedBy(12.dp),
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.weight(1f)
            ) {
                Icon(
                    icon,
                    contentDescription = null,
                    tint = if (isDarkTheme) Color.White else Color.Black,
                    modifier = Modifier.size(28.dp)
                )
                Column {
                    Text(
                        text = title,
                        style = MaterialTheme.typography.bodyLarge,
                        color = if (isDarkTheme) Color.White else Color.Black,
                        fontWeight = FontWeight.SemiBold
                    )
                    Text(
                        text = subtitle,
                        style = MaterialTheme.typography.bodySmall,
                        color = if (isDarkTheme) Color.White.copy(alpha = 0.6f) else Color.Black.copy(alpha = 0.6f)
                    )
                }
            }
            val accentColor = if (isDarkTheme) BubblePinkStart else BubblePinkStartLight
            Switch(
                checked = checked,
                onCheckedChange = onCheckedChange,
                colors = SwitchDefaults.colors(
                    checkedThumbColor = accentColor,
                    checkedTrackColor = accentColor.copy(alpha = 0.5f),
                    uncheckedThumbColor = Color.Gray,
                    uncheckedTrackColor = Color.Gray.copy(alpha = 0.3f)
                )
            )
        }
    }
}
