package com.plant.forestcare.ui.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.scaleIn
import androidx.compose.animation.slideInVertically
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.rotate
import androidx.compose.ui.unit.dp

@Composable
fun PremiumPlantBackground(
    modifier: Modifier = Modifier,
    topColor: Color = Color(0xFFE8F5E9),
    middleColor: Color = Color(0xFFFBF6EA),
    bottomColor: Color = Color(0xFFF8FAF7),
    content: @Composable BoxScope.() -> Unit
) {
    val transition = rememberInfiniteTransition(label = "premium_plant_background")
    val drift by transition.animateFloat(
        initialValue = -18f,
        targetValue = 18f,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = 5400),
            repeatMode = RepeatMode.Reverse
        ),
        label = "background_drift"
    )

    Box(modifier = modifier) {
        Canvas(modifier = Modifier.fillMaxSize()) {
            drawRect(
                brush = Brush.verticalGradient(
                    listOf(topColor, middleColor, bottomColor)
                )
            )
            drawCircle(
                color = Color.White.copy(alpha = 0.58f),
                radius = size.minDimension * 0.42f,
                center = Offset(size.width * 0.88f + drift, size.height * 0.08f)
            )
            drawCircle(
                color = Color(0xFFBFE7C2).copy(alpha = 0.20f),
                radius = size.minDimension * 0.34f,
                center = Offset(size.width * 0.08f - drift, size.height * 0.78f)
            )
            repeat(7) { index ->
                val baseX = size.width * (0.06f + index * 0.15f)
                val baseY = size.height * (0.12f + (index % 3) * 0.26f)
                rotate(
                    degrees = -24f + index * 8f,
                    pivot = Offset(baseX + drift * 0.35f, baseY)
                ) {
                    val leaf = Path().apply {
                        moveTo(baseX, baseY + 42f)
                        cubicTo(baseX - 34f, baseY + 8f, baseX - 8f, baseY - 42f, baseX + 48f, baseY - 30f)
                        cubicTo(baseX + 42f, baseY + 16f, baseX + 18f, baseY + 38f, baseX, baseY + 42f)
                        close()
                    }
                    drawPath(leaf, Color(0xFF2E7D32).copy(alpha = 0.045f))
                }
            }
        }
        content()
    }
}

@Composable
fun AnimatedEntry(
    modifier: Modifier = Modifier,
    delayMillis: Int = 0,
    content: @Composable () -> Unit
) {
    var visible by remember { mutableStateOf(false) }
    LaunchedEffect(Unit) {
        kotlinx.coroutines.delay(delayMillis.toLong())
        visible = true
    }
    AnimatedVisibility(
        visible = visible,
        enter = fadeIn(tween(280)) + slideInVertically(
            animationSpec = tween(320),
            initialOffsetY = { it / 5 }
        ) + scaleIn(tween(320), initialScale = 0.98f),
        modifier = modifier
    ) {
        content()
    }
}

@Composable
fun BotanicalHeroArt(
    modifier: Modifier = Modifier,
    leafColor: Color = Color(0xFF2E7D32),
    accentColor: Color = Color(0xFF81C784)
) {
    val transition = rememberInfiniteTransition(label = "botanical_hero_art")
    val sway by transition.animateFloat(
        initialValue = -5f,
        targetValue = 5f,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = 2400),
            repeatMode = RepeatMode.Reverse
        ),
        label = "plant_sway"
    )

    Canvas(modifier = modifier.size(130.dp)) {
        drawCircle(
            color = Color.White.copy(alpha = 0.18f),
            radius = size.minDimension * 0.46f,
            center = Offset(size.width * 0.54f, size.height * 0.47f)
        )
        drawRoundRect(
            color = Color(0xFFD8BC95),
            topLeft = Offset(size.width * 0.35f, size.height * 0.72f),
            size = Size(size.width * 0.34f, size.height * 0.10f),
            cornerRadius = androidx.compose.ui.geometry.CornerRadius(14f, 14f)
        )
        drawRoundRect(
            color = Color(0xFFF0DDC1),
            topLeft = Offset(size.width * 0.39f, size.height * 0.80f),
            size = Size(size.width * 0.26f, size.height * 0.15f),
            cornerRadius = androidx.compose.ui.geometry.CornerRadius(16f, 16f)
        )
        repeat(9) { index ->
            val stemStart = Offset(size.width * 0.51f, size.height * 0.74f)
            val leafCenter = Offset(
                size.width * (0.25f + index * 0.062f) + sway * (index % 3),
                size.height * (0.25f + (index % 4) * 0.10f)
            )
            drawLine(
                color = leafColor.copy(alpha = 0.9f),
                start = stemStart,
                end = leafCenter,
                strokeWidth = 3f
            )
            drawOval(
                color = if (index % 2 == 0) leafColor else accentColor,
                topLeft = Offset(leafCenter.x - 16f, leafCenter.y - 11f),
                size = Size(32f, 22f)
            )
        }
    }
}
