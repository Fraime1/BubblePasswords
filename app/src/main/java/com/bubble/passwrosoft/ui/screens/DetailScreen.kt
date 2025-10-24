package com.bubble.passwrosoft.ui.screens

import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ContentCopy
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material.icons.filled.QrCode
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
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
import com.bubble.passwrosoft.ui.components.GlossyBubble
import com.bubble.passwrosoft.ui.theme.BubbleAccentGlow
import com.bubble.passwrosoft.ui.theme.adaptiveTextColor
import com.bubble.passwrosoft.ui.theme.isDarkTheme
import com.bubble.passwrosoft.util.ClipboardHelper

@Composable
fun DetailScreen(
    service: String,
    username: String,
    password: String,
    isFavorite: Boolean = false,
    onFavoriteToggle: () -> Unit = {},
    onCopy: () -> Unit,
    onEdit: () -> Unit,
    onDelete: () -> Unit,
    onShare: () -> Unit,
) {
    val context = LocalContext.current
    val textColor = adaptiveTextColor()
    val isDark = isDarkTheme()
    val (show, setShow) = remember { mutableStateOf(false) }
    val (copied, setCopied) = remember { mutableStateOf(false) }
    val (showQR, setShowQR) = remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(Modifier.height(16.dp))
        
        GlossyBubble(
            bubbleSize = 100.dp,
            label = service.take(2).uppercase(),
            glow = copied
        )

        Text(
            text = service,
            style = MaterialTheme.typography.headlineMedium,
            fontWeight = FontWeight.Bold,
            color = textColor
        )

        Spacer(Modifier.height(8.dp))

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 8.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            InfoRow("Username", username)
            InfoRow("Password", if (show) password else "••••••••••••")
        }

        Row(
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            modifier = Modifier.padding(vertical = 8.dp)
        ) {
            IconButton(
                onClick = { setShow(!show) },
                modifier = Modifier
            ) {
                Icon(
                    if (show) Icons.Filled.VisibilityOff else Icons.Filled.Visibility,
                    contentDescription = if (show) "Hide" else "Show",
                    tint = Color.White
                )
            }
            IconButton(
                onClick = {
                    ClipboardHelper.copyToClipboard(context, "Password", password)
                    setCopied(true)
                    onCopy()
                },
                modifier = Modifier
            ) {
                Icon(
                    Icons.Filled.ContentCopy,
                    contentDescription = "Copy",
                    tint = BubbleAccentGlow
                )
            }
        }

        Spacer(Modifier.height(16.dp))

        // Favorite Button
        OutlinedButton(
            onClick = onFavoriteToggle,
            modifier = Modifier.fillMaxWidth(),
            colors = ButtonDefaults.outlinedButtonColors(
                contentColor = if (isFavorite) Color(0xFFFF4081) else Color.White
            )
        ) {
            Icon(
                if (isFavorite) Icons.Filled.Favorite else Icons.Filled.FavoriteBorder, 
                contentDescription = null
            )
            Text(if (isFavorite) " Remove from Favorites" else " Add to Favorites")
        }

        Spacer(Modifier.height(8.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            OutlinedButton(
                onClick = onEdit,
                modifier = Modifier.weight(1f),
                colors = ButtonDefaults.outlinedButtonColors(
                    contentColor = Color.White
                )
            ) {
                Icon(Icons.Filled.Edit, contentDescription = null)
                Text(" Edit")
            }
            OutlinedButton(
                onClick = { 
                    setShowQR(true)
                    onShare()
                },
                modifier = Modifier.weight(1f),
                colors = ButtonDefaults.outlinedButtonColors(
                    contentColor = Color.White
                )
            ) {
                Icon(Icons.Filled.QrCode, contentDescription = null)
                Text(" QR")
            }
        }

        Button(
            onClick = onDelete,
            colors = ButtonDefaults.buttonColors(
                containerColor = Color(0xFFE74C3C)
            ),
            modifier = Modifier.fillMaxWidth()
        ) {
            Icon(Icons.Filled.Delete, contentDescription = null)
            Text(" Delete Password")
        }

        Spacer(Modifier.height(16.dp))
    }
    
    // QR Dialog
    if (showQR) {
        com.bubble.passwrosoft.ui.components.QRDialog(
            service = service,
            username = username,
            password = password,
            onDismiss = { setShowQR(false) }
        )
    }
}

@Composable
private fun InfoRow(label: String, value: String) {
    Column(
        modifier = Modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(4.dp)
    ) {
        Text(
            text = label,
            style = MaterialTheme.typography.labelMedium,
            color = Color.White.copy(alpha = 0.6f),
            fontSize = 12.sp
        )
        Text(
            text = value,
            style = MaterialTheme.typography.bodyLarge,
            color = Color.White,
            fontSize = 16.sp,
            fontWeight = FontWeight.Medium
        )
    }
}
