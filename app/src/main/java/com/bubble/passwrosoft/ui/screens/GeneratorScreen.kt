package com.bubble.passwrosoft.ui.screens

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
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Slider
import androidx.compose.material3.SliderDefaults
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
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
import com.bubble.passwrosoft.ui.theme.BubblePinkStart
import com.bubble.passwrosoft.ui.theme.adaptiveTextColor
import com.bubble.passwrosoft.ui.theme.isDarkTheme
import com.bubble.passwrosoft.util.ClipboardHelper

@Composable
fun GeneratorScreen(
    onGenerate: (Int, Boolean, Boolean, Boolean, Boolean) -> String,
) {
    val context = LocalContext.current
    val textColor = adaptiveTextColor()
    val isDark = isDarkTheme()
    val (uppercase, setUppercase) = remember { mutableStateOf(true) }
    val (lowercase, setLowercase) = remember { mutableStateOf(true) }
    val (numbers, setNumbers) = remember { mutableStateOf(true) }
    val (symbols, setSymbols) = remember { mutableStateOf(false) }
    val (length, setLength) = remember { mutableStateOf(16f) }
    val (result, setResult) = remember { mutableStateOf("") }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "Password Generator",
            style = MaterialTheme.typography.headlineMedium,
            fontWeight = FontWeight.Bold,
            color = textColor,
            modifier = Modifier.padding(vertical = 12.dp)
        )

        if (result.isNotBlank()) {
            GlossyBubble(
                bubbleSize = 120.dp,
                label = result.take(8) + "...",
                glow = true
            )
            
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp),
                colors = CardDefaults.cardColors(
                    containerColor = if (isDark) Color(0x22FFFFFF) else Color(0xFFE8D5F2)
                )
            ) {
                Text(
                    text = result,
                    style = MaterialTheme.typography.bodyLarge,
                    color = textColor,
                    fontWeight = FontWeight.Medium,
                    modifier = Modifier.padding(16.dp)
                )
            }
        } else {
            GlossyBubble(
                bubbleSize = 120.dp,
                label = "?",
                glow = false
            )
            Text(
                text = "Configure options and generate",
                color = textColor.copy(alpha = 0.7f),
                style = MaterialTheme.typography.bodyMedium
            )
        }

        Spacer(Modifier.height(8.dp))

        Column(
            modifier = Modifier.fillMaxWidth(),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            RowOption("🔠 Uppercase (A-Z)", uppercase, setUppercase)
            RowOption("🔡 Lowercase (a-z)", lowercase, setLowercase)
            RowOption("🔢 Numbers (0-9)", numbers, setNumbers)
            RowOption("🔣 Symbols (!@#)", symbols, setSymbols)
        }

        Spacer(Modifier.height(8.dp))

        Column(modifier = Modifier.fillMaxWidth()) {
            Text(
                "Length: ${length.toInt()} characters",
                style = MaterialTheme.typography.bodyLarge,
                color = textColor,
                fontWeight = FontWeight.Medium
            )
            Slider(
                value = length,
                onValueChange = setLength,
                valueRange = 8f..32f,
                steps = 23,
                colors = SliderDefaults.colors(
                    thumbColor = BubblePinkStart,
                    activeTrackColor = BubblePinkStart,
                    inactiveTrackColor = textColor.copy(alpha = 0.3f)
                )
            )
        }

        Spacer(Modifier.weight(1f))
        Spacer(Modifier.height(16.dp))

        Button(
            onClick = {
                setResult(onGenerate(length.toInt(), uppercase, lowercase, numbers, symbols))
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Generate Password")
        }

        if (result.isNotBlank()) {
            Button(
                onClick = {
                    ClipboardHelper.copyToClipboard(context, "Generated Password", result)
                },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Copy to Clipboard")
            }
        }

        Spacer(Modifier.height(16.dp))
    }
}

@Composable
private fun RowOption(label: String, checked: Boolean, onCheckedChange: (Boolean) -> Unit) {
    val textColor = adaptiveTextColor()
    val isDark = isDarkTheme()
    
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = if (isDark) {
                if (checked) Color(0x22FFFFFF) else Color(0x11FFFFFF)
            } else {
                if (checked) Color(0xFFE8D5F2) else Color(0xFFFFE6F5)
            }
        )
    ) {
        Row(
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 8.dp)
        ) {
            Text(
                label,
                color = textColor,
                style = MaterialTheme.typography.bodyLarge
            )
            Switch(
                checked = checked,
                onCheckedChange = onCheckedChange,
                colors = SwitchDefaults.colors(
                    checkedThumbColor = BubblePinkStart,
                    checkedTrackColor = BubblePinkStart.copy(alpha = 0.5f),
                    uncheckedThumbColor = Color.Gray,
                    uncheckedTrackColor = Color.Gray.copy(alpha = 0.3f)
                )
            )
        }
    }
}
