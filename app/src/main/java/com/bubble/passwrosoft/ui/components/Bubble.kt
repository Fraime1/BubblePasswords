package com.bubble.passwrosoft.ui.components

import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.BlendMode
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.Fill
import androidx.compose.ui.graphics.drawscope.withTransform
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.bubble.passwrosoft.ui.theme.BubbleAccentGlow
import com.bubble.passwrosoft.ui.theme.BubblePinkEnd
import com.bubble.passwrosoft.ui.theme.BubblePinkStart

@Composable
fun GlossyBubble(
    modifier: Modifier = Modifier,
    bubbleSize: Dp = 72.dp,
    label: String? = null,
    onClick: (() -> Unit)? = null,
    glow: Boolean = false,
) {
    val radius = bubbleSize / 2
    val infinite = rememberInfiniteTransition(label = "bubble-bounce")
    val bounce by infinite.animateFloat(
        initialValue = 0f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(
            animation = tween(1800, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "bounce"
    )

    Box(
        modifier = modifier
            .size(bubbleSize)
            .clip(CircleShape)
            .background(Color.Transparent)
            .then(if (onClick != null) Modifier.clickable { onClick() } else Modifier),
        contentAlignment = Alignment.Center
    ) {
        Canvas(modifier = Modifier.matchParentSize()) {
            val bubblePx = with(this) { bubbleSize.toPx() }
            val radiusPx = with(this) { radius.toPx() }
            // Base pink gradient
            drawCircle(
                brush = Brush.linearGradient(
                    colors = listOf(BubblePinkStart, BubblePinkEnd)
                ),
                style = Fill
            )

            // Glossy highlight
            withTransform({
                translate(left = bubblePx * 0.1f, top = bubblePx * 0.1f)
                scale(scaleX = 1.2f, scaleY = 0.6f)
            }) {
                drawCircle(
                    color = Color.White.copy(alpha = 0.20f + 0.10f * bounce),
                    radius = radiusPx * 0.7f,
                    style = Fill,
                    blendMode = BlendMode.Lighten
                )
            }

            // Rim light
            drawCircle(
                brush = Brush.radialGradient(
                    colors = listOf(Color.White.copy(alpha = 0.25f), Color.Transparent)
                ),
                radius = radiusPx * 0.95f,
                style = Fill
            )

            if (glow) {
                // Cyan glow
                drawCircle(
                    brush = Brush.radialGradient(
                        colors = listOf(BubbleAccentGlow.copy(alpha = 0.4f), Color.Transparent)
                    ),
                    radius = radiusPx * 1.2f,
                    style = Fill
                )
            }
        }

        if (label != null) {
            androidx.compose.material3.Text(
                text = label,
                style = TextStyle(
                    color = Color.White,
                    fontSize = 12.sp,
                    fontWeight = FontWeight.SemiBold,
                    textAlign = TextAlign.Center
                ),
                modifier = Modifier
            )
        }
    }
}


