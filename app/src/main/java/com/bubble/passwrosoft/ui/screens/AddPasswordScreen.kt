package com.bubble.passwrosoft.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.bubble.passwrosoft.ui.components.GlossyBubble
import com.bubble.passwrosoft.ui.theme.BubblePinkStart
import com.bubble.passwrosoft.ui.theme.BubblePinkStartLight
import com.bubble.passwrosoft.ui.theme.adaptiveTextColor
import com.bubble.passwrosoft.ui.theme.adaptiveTextColorAlpha
import com.bubble.passwrosoft.ui.theme.isDarkTheme

@Composable
fun AddPasswordScreen(
    onSave: (service: String, username: String, password: String) -> Unit,
    onGenerate: (Int, Boolean, Boolean, Boolean, Boolean) -> String,
) {
    val textColor = adaptiveTextColor()
    val isDark = isDarkTheme()
    val accentColor = if (isDark) BubblePinkStart else BubblePinkStartLight
    val (service, setService) = remember { mutableStateOf("") }
    val (username, setUsername) = remember { mutableStateOf("") }
    val (password, setPassword) = remember { mutableStateOf("") }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(16.dp)
            .imePadding(),
        verticalArrangement = Arrangement.spacedBy(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "Add Password",
            style = MaterialTheme.typography.headlineMedium,
            fontWeight = FontWeight.Bold,
            color = textColor,
            modifier = Modifier.padding(vertical = 12.dp)
        )

        OutlinedTextField(
            value = service,
            onValueChange = setService,
            label = { Text("Service name", color = textColor.copy(alpha = 0.7f)) },
            placeholder = { Text("e.g. Gmail", color = Color.Gray) },
            modifier = Modifier.fillMaxWidth(),
            colors = OutlinedTextFieldDefaults.colors(
                focusedTextColor = textColor,
                unfocusedTextColor = textColor,
                focusedBorderColor = accentColor,
                unfocusedBorderColor = textColor.copy(alpha = 0.3f),
                cursorColor = accentColor
            ),
            singleLine = true
        )

        OutlinedTextField(
            value = username,
            onValueChange = setUsername,
            label = { Text("Username / Email", color = textColor.copy(alpha = 0.7f)) },
            placeholder = { Text("user@example.com", color = Color.Gray) },
            modifier = Modifier.fillMaxWidth(),
            colors = OutlinedTextFieldDefaults.colors(
                focusedTextColor = textColor,
                unfocusedTextColor = textColor,
                focusedBorderColor = accentColor,
                unfocusedBorderColor = textColor.copy(alpha = 0.3f),
                cursorColor = accentColor
            ),
            singleLine = true
        )

        OutlinedTextField(
            value = password,
            onValueChange = setPassword,
            label = { Text("Password", color = textColor.copy(alpha = 0.7f)) },
            placeholder = { Text("Enter or generate", color = Color.Gray) },
            modifier = Modifier.fillMaxWidth(),
            colors = OutlinedTextFieldDefaults.colors(
                focusedTextColor = textColor,
                unfocusedTextColor = textColor,
                focusedBorderColor = accentColor,
                unfocusedBorderColor = textColor.copy(alpha = 0.3f),
                cursorColor = accentColor
            ),
            singleLine = true
        )

        if (password.isNotBlank()) {
            GlossyBubble(
                bubbleSize = 80.dp,
                label = password.take(6) + "...",
                glow = true
            )
        }

        TextButton(
            onClick = {
                val generated = onGenerate(16, true, true, true, true)
                setPassword(generated)
            }
        ) {
            Icon(
                Icons.Filled.AutoAwesome,
                contentDescription = null,
                tint = accentColor
            )
            Text(
                " Generate Strong Password",
                color = textColor
            )
        }

        Spacer(Modifier.weight(1f))
        Spacer(Modifier.height(16.dp))

        Button(
            onClick = { onSave(service, username, password) },
            enabled = service.isNotBlank() && username.isNotBlank() && password.isNotBlank(),
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Save Password")
        }

        Spacer(Modifier.height(16.dp))
    }
}
