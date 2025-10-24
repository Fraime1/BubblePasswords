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
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.bubble.passwrosoft.ui.components.GlossyBubble
import com.bubble.passwrosoft.ui.theme.BubbleGreenStrong
import com.bubble.passwrosoft.ui.theme.BubbleRedWeak
import com.bubble.passwrosoft.ui.theme.BubbleYellowMedium
import com.bubble.passwrosoft.ui.theme.adaptiveTextColor
import com.bubble.passwrosoft.ui.theme.isDarkTheme

@Composable
fun StatsScreen(
    total: Int,
    strong: Int,
    medium: Int,
    weak: Int,
) {
    val textColor = adaptiveTextColor()
    val isDark = isDarkTheme()
    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(20.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "Statistics",
            style = MaterialTheme.typography.headlineMedium,
            fontWeight = FontWeight.Bold,
            color = textColor,
            modifier = Modifier.padding(vertical = 12.dp)
        )

        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(
                containerColor = if (isDark) Color(0x22FFFFFF) else Color(0xFFE8D5F2)
            )
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(24.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = "$total",
                    style = MaterialTheme.typography.displayLarge,
                    fontWeight = FontWeight.Bold,
                    fontSize = 64.sp,
                    color = Color.White
                )
                Text(
                    text = "Saved Passwords",
                    style = MaterialTheme.typography.bodyLarge,
                    color = Color.White.copy(alpha = 0.7f)
                )
            }
        }

        Spacer(Modifier.height(8.dp))

        Text(
            text = "Password Strength",
            style = MaterialTheme.typography.titleLarge,
            fontWeight = FontWeight.SemiBold,
            color = Color.White
        )

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                GlossyBubble(bubbleSize = 72.dp, label = "$strong")
                Text(
                    text = "Strong",
                    style = MaterialTheme.typography.labelLarge,
                    color = BubbleGreenStrong,
                    fontWeight = FontWeight.SemiBold
                )
            }
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                GlossyBubble(bubbleSize = 72.dp, label = "$medium")
                Text(
                    text = "Medium",
                    style = MaterialTheme.typography.labelLarge,
                    color = BubbleYellowMedium,
                    fontWeight = FontWeight.SemiBold
                )
            }
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                GlossyBubble(bubbleSize = 72.dp, label = "$weak")
                Text(
                    text = "Weak",
                    style = MaterialTheme.typography.labelLarge,
                    color = BubbleRedWeak,
                    fontWeight = FontWeight.SemiBold
                )
            }
        }

        Spacer(Modifier.weight(1f))

        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(
                containerColor = if (isDark) Color(0x11FFFFFF) else Color(0xFFFFE6F5)
            )
        ) {
            Text(
                text = "💡 Tip: Use the generator to create strong passwords",
                style = MaterialTheme.typography.bodyMedium,
                color = textColor.copy(alpha = 0.8f),
                modifier = Modifier.padding(16.dp)
            )
        }

        Spacer(Modifier.height(16.dp))
    }
}
