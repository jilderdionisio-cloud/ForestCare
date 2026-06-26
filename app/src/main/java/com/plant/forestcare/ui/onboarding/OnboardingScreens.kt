package com.plant.forestcare.ui.onboarding

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleIn
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.ArrowForward
import androidx.compose.material.icons.rounded.AccessTime
import androidx.compose.material.icons.rounded.Eco
import androidx.compose.material.icons.rounded.LocalFlorist
import androidx.compose.material.icons.rounded.WaterDrop
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.delay

private val ScreenBg = Color(0xFFFFFCF4)
private val Green = Color(0xFF007F25)
private val GreenDark = Color(0xFF006D1F)
private val GreenSoft = Color(0xFFE9F8E8)
private val TextDark = Color(0xFF20231F)
private val TextMuted = Color(0xFF6F776D)
private val Beige = Color(0xFFEADDBD)
private val Peach = Color(0xFFF3C9AE)

@Composable
fun SplashRoute(
    onFinished: () -> Unit,
    modifier: Modifier = Modifier
) {
    LaunchedEffect(Unit) {
        delay(1600)
        onFinished()
    }
    SplashScreen(modifier = modifier)
}

@Composable
fun SplashScreen(modifier: Modifier = Modifier) {
    Box(
        modifier = modifier
            .fillMaxSize()
            .background(
                Brush.radialGradient(
                    colors = listOf(Color(0xFFEFF9E8), ScreenBg),
                    center = Offset(540f, 1220f),
                    radius = 760f
                )
            )
            .padding(horizontal = 44.dp),
        contentAlignment = Alignment.Center
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Spacer(Modifier.weight(1f))
            Surface(
                modifier = Modifier
                    .size(104.dp)
                    .shadow(24.dp, RoundedCornerShape(30.dp), spotColor = Green.copy(alpha = 0.10f)),
                shape = RoundedCornerShape(30.dp),
                color = Color.White
            ) {
                Box(contentAlignment = Alignment.Center) {
                    LeafLogo(modifier = Modifier.size(48.dp))
                }
            }
            Spacer(Modifier.height(28.dp))
            Text(
                text = "PlantCare",
                color = Green,
                fontSize = 40.sp,
                fontWeight = FontWeight.ExtraBold,
                letterSpacing = (-0.5).sp
            )
            Text(
                text = "NURTURE YOUR SANCTUARY",
                color = Color(0xFF9FA99A),
                fontSize = 10.sp,
                fontWeight = FontWeight.Bold,
                letterSpacing = 2.sp
            )
            Spacer(Modifier.weight(1f))
            Box(
                modifier = Modifier
                    .fillMaxWidth(0.72f)
                    .height(4.dp)
                    .background(
                        Brush.horizontalGradient(listOf(Color(0xFF66BB6A), Green)),
                        RoundedCornerShape(50)
                    )
            )
            Spacer(Modifier.height(14.dp))
            Text(
                text = "Growing your garden...",
                color = Color(0xFF9CA594),
                fontSize = 11.sp,
                fontWeight = FontWeight.Bold
            )
            Spacer(Modifier.height(56.dp))
        }
    }
}

@Composable
fun OnboardingRoute(
    onFinished: () -> Unit,
    modifier: Modifier = Modifier
) {
    var page by remember { mutableIntStateOf(0) }
    val pages = remember {
        listOf(
            OnboardingPage(
                title = "Organiza tus plantas",
                description = "Transforma tu hogar en un oasis urbano con recordatorios inteligentes y consejos de expertos.",
                button = "Siguiente",
                kind = OnboardingArt.Leaf
            ),
            OnboardingPage(
                title = "Recibe recordatorios inteligentes",
                description = "Nunca más olvides regar o fertilizar tus plantas. Te avisaremos en el momento perfecto.",
                button = "Siguiente",
                kind = OnboardingArt.Reminders
            ),
            OnboardingPage(
                title = "Cuida tus plantas como un experto",
                description = "Recibe guías personalizadas y recordatorios inteligentes para que tu jungla urbana siempre esté radiante.",
                button = "Comenzar",
                kind = OnboardingArt.Expert
            )
        )
    }

    OnboardingScreen(
        page = pages[page],
        pageIndex = page,
        pageCount = pages.size,
        onNext = {
            if (page == pages.lastIndex) onFinished() else page += 1
        },
        onSkip = onFinished,
        modifier = modifier
    )
}

@Composable
private fun OnboardingScreen(
    page: OnboardingPage,
    pageIndex: Int,
    pageCount: Int,
    onNext: () -> Unit,
    onSkip: () -> Unit,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .fillMaxSize()
            .background(
                Brush.radialGradient(
                    colors = listOf(Color(0xFFE6F7E7), ScreenBg),
                    center = Offset(650f, 80f),
                    radius = 820f
                )
            )
            .padding(horizontal = 24.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 28.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "PlantCare",
                color = Green,
                fontSize = if (pageIndex == 0) 14.sp else 18.sp,
                fontWeight = FontWeight.Medium,
                modifier = Modifier.weight(1f),
                textAlign = if (pageIndex == 0) TextAlign.Center else TextAlign.Start
            )
            AnimatedVisibility(visible = pageIndex == pageCount - 1) {
                Text(
                    text = "Saltar",
                    color = TextDark,
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier
                        .padding(end = 8.dp)
                        .clickable(onClick = onSkip)
                )
            }
        }

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(top = 92.dp, bottom = 42.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            AnimatedContent(
                targetState = page,
                transitionSpec = { fadeIn() + scaleIn(initialScale = 0.98f) togetherWith fadeOut() },
                label = "onboarding_art"
            ) { current ->
                OnboardingIllustration(kind = current.kind, pageIndex = pageIndex)
            }
            Spacer(Modifier.height(if (page.kind == OnboardingArt.Reminders) 26.dp else 52.dp))
            Text(
                text = page.title,
                color = TextDark,
                fontSize = 25.sp,
                lineHeight = 31.sp,
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.Center,
                modifier = Modifier.padding(horizontal = 10.dp)
            )
            Spacer(Modifier.height(18.dp))
            Text(
                text = page.description,
                color = TextMuted,
                fontSize = 14.sp,
                lineHeight = 21.sp,
                textAlign = TextAlign.Center,
                modifier = Modifier.padding(horizontal = 28.dp)
            )
            Spacer(Modifier.weight(1f))
            PageIndicator(pageIndex = pageIndex, pageCount = pageCount)
            Spacer(Modifier.height(18.dp))
            Button(
                onClick = onNext,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(58.dp)
                    .shadow(12.dp, RoundedCornerShape(30.dp), spotColor = Green.copy(alpha = 0.28f)),
                shape = RoundedCornerShape(30.dp),
                colors = ButtonDefaults.buttonColors(containerColor = Green)
            ) {
                Text(page.button, color = Color.White, fontSize = 16.sp, fontWeight = FontWeight.Bold)
                Spacer(Modifier.size(10.dp))
                Icon(Icons.AutoMirrored.Rounded.ArrowForward, contentDescription = null, tint = Color.White)
            }
            if (pageIndex == 0) {
                Spacer(Modifier.height(26.dp))
                Text(
                    text = "OMITIR",
                    color = Color(0xFF8A9387),
                    fontSize = 10.sp,
                    fontWeight = FontWeight.Bold,
                    letterSpacing = 2.sp,
                    modifier = Modifier
                        .padding(bottom = 4.dp)
                        .clickable(onClick = onSkip)
                )
            }
        }

        AnimatedVisibility(
            visible = pageIndex == pageCount - 1,
            modifier = Modifier
                .align(Alignment.TopEnd)
                .padding(top = 122.dp)
        ) {
            FloatingCircle(icon = Icons.Rounded.Eco, size = 56, iconSize = 24)
        }

        if (pageIndex == 0) {
            Box(
                modifier = Modifier
                    .align(Alignment.TopEnd)
                    .padding(top = 94.dp, end = 2.dp)
            ) {
                FloatingCircle(icon = Icons.Rounded.LocalFlorist, size = 58, iconSize = 25, dark = true)
            }
        }
    }
}

@Composable
private fun OnboardingIllustration(kind: OnboardingArt, pageIndex: Int) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(if (kind == OnboardingArt.Reminders) 300.dp else 276.dp),
        contentAlignment = Alignment.Center
    ) {
        when (kind) {
            OnboardingArt.Leaf -> LeafCardArt()
            OnboardingArt.Reminders -> ReminderPlantArt()
            OnboardingArt.Expert -> ExpertPlantArt()
        }
    }
}

@Composable
private fun LeafCardArt() {
    Box(contentAlignment = Alignment.Center) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(276.dp)
                .padding(horizontal = 0.dp)
                .background(
                    Brush.linearGradient(listOf(Color(0xFFE9D8AE), Color(0xFFE9E1B8))),
                    RoundedCornerShape(38.dp)
                )
        )
        Canvas(modifier = Modifier.size(235.dp)) {
            val leaf = Path().apply {
                moveTo(size.width * 0.18f, size.height * 0.56f)
                cubicTo(size.width * 0.28f, size.height * 0.12f, size.width * 0.76f, size.height * 0.10f, size.width * 0.86f, size.height * 0.52f)
                cubicTo(size.width * 0.68f, size.height * 0.86f, size.width * 0.34f, size.height * 0.90f, size.width * 0.18f, size.height * 0.56f)
                close()
            }
            drawPath(leaf, Brush.linearGradient(listOf(Color(0xFF0E642C), Color(0xFF77AE67))))
            drawLine(Color(0xFFE3E9C9), Offset(size.width * 0.23f, size.height * 0.58f), Offset(size.width * 0.82f, size.height * 0.42f), 4.5f)
            repeat(7) { index ->
                val y = size.height * (0.30f + index * 0.065f)
                drawLine(
                    Color(0xFFD3DFC0),
                    Offset(size.width * 0.44f, y),
                    Offset(size.width * (0.22f + index * 0.035f), y + 32f),
                    2.4f
                )
                drawLine(
                    Color(0xFFD3DFC0),
                    Offset(size.width * 0.52f, y + 2f),
                    Offset(size.width * (0.78f - index * 0.012f), y + 28f),
                    2.4f
                )
            }
            listOf(
                Offset(size.width * 0.42f, size.height * 0.22f),
                Offset(size.width * 0.64f, size.height * 0.27f),
                Offset(size.width * 0.31f, size.height * 0.43f),
                Offset(size.width * 0.72f, size.height * 0.51f)
            ).forEach {
                drawOval(Color(0xFFE7DDB8), topLeft = Offset(it.x - 10f, it.y - 22f), size = Size(40f, 70f))
            }
        }
    }
}

@Composable
private fun ReminderPlantArt() {
    Box(contentAlignment = Alignment.Center) {
        Canvas(modifier = Modifier.size(254.dp)) {
            drawCircle(
                color = Color.White,
                radius = size.minDimension * 0.45f,
                center = center,
                style = Stroke(width = 16f)
            )
            drawCircle(
                brush = Brush.radialGradient(listOf(Color(0xFF8BB894), Color(0xFF2F6953))),
                radius = size.minDimension * 0.43f,
                center = center
            )
            drawRoundRect(
                color = Color.White,
                topLeft = Offset(size.width * 0.43f, size.height * 0.56f),
                size = Size(size.width * 0.20f, size.height * 0.26f),
                cornerRadius = CornerRadius(10f, 10f)
            )
            repeat(9) { index ->
                val angle = -1.9f + index * 0.33f
                val sx = size.width * 0.53f
                val sy = size.height * 0.62f
                val ex = sx + kotlin.math.cos(angle) * size.width * 0.28f
                val ey = sy + kotlin.math.sin(angle) * size.height * 0.32f
                drawLine(Color(0xFF164F2B), Offset(sx, sy), Offset(ex, ey), 5f)
                drawOval(
                    color = if (index % 2 == 0) Color(0xFF1E7C3B) else Color(0xFF3F9353),
                    topLeft = Offset(ex - 24f, ey - 18f),
                    size = Size(50f, 36f)
                )
            }
        }
        FloatingCircle(
            icon = Icons.Rounded.AccessTime,
            size = 78,
            iconSize = 32,
            modifier = Modifier.offset(x = (-82).dp, y = (-96).dp)
        )
        FloatingCircle(
            icon = Icons.Rounded.WaterDrop,
            size = 70,
            iconSize = 30,
            modifier = Modifier.offset(x = 88.dp, y = 38.dp)
        )
        ProgressBubble(modifier = Modifier.offset(x = 88.dp, y = 104.dp))
    }
}

@Composable
private fun ExpertPlantArt() {
    Box(contentAlignment = Alignment.Center) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(258.dp)
                .background(Peach, RoundedCornerShape(36.dp))
        )
        Canvas(modifier = Modifier.size(300.dp)) {
            repeat(6) { index ->
                val x = size.width * (0.12f + index * 0.15f)
                drawLine(Color(0xFF8D6A4E), Offset(x, size.height * 0.18f), Offset(x - 22f, size.height * 0.44f), 2f)
                drawLine(Color(0xFF8D6A4E), Offset(x, size.height * 0.18f), Offset(x + 22f, size.height * 0.44f), 2f)
                drawRoundRect(
                    color = Color(0xFFC97948),
                    topLeft = Offset(x - 22f, size.height * 0.40f),
                    size = Size(44f, 34f),
                    cornerRadius = CornerRadius(10f, 10f)
                )
                drawCircle(Color(0xFF246E3B), radius = 22f, center = Offset(x, size.height * 0.35f))
            }
            repeat(5) { index ->
                val x = size.width * (0.12f + index * 0.19f)
                val baseY = size.height * 0.72f
                drawRoundRect(Color(0xFFC77847), Offset(x - 24f, baseY), Size(48f, 40f), CornerRadius(8f, 8f))
                repeat(6) { leaf ->
                    drawOval(
                        color = if (leaf % 2 == 0) Color(0xFF1E6339) else Color(0xFF2D8A52),
                        topLeft = Offset(x - 45f + leaf * 14f, baseY - 64f + (leaf % 2) * 8f),
                        size = Size(44f, 78f)
                    )
                }
            }
            drawCircle(Color(0xFFE2A381), radius = 22f, center = Offset(size.width * 0.70f, size.height * 0.51f))
            drawOval(Color(0xFF8B4A34), Offset(size.width * 0.66f, size.height * 0.39f), Size(48f, 56f))
            drawRoundRect(Color(0xFF286B45), Offset(size.width * 0.62f, size.height * 0.58f), Size(72f, 96f), CornerRadius(26f, 26f))
            drawLine(Color(0xFFE2A381), Offset(size.width * 0.62f, size.height * 0.63f), Offset(size.width * 0.54f, size.height * 0.56f), 8f)
            drawLine(Color(0xFFE2A381), Offset(size.width * 0.70f, size.height * 0.63f), Offset(size.width * 0.58f, size.height * 0.57f), 8f)
            drawRoundRect(Color(0xFF17251E), Offset(size.width * 0.53f, size.height * 0.53f), Size(28f, 42f), CornerRadius(4f, 4f))
        }
    }
}

@Composable
private fun FloatingCircle(
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    size: Int,
    iconSize: Int,
    modifier: Modifier = Modifier,
    dark: Boolean = false
) {
    Surface(
        modifier = modifier
            .size(size.dp)
            .shadow(16.dp, CircleShape, spotColor = Color.Black.copy(alpha = 0.12f)),
        color = if (dark) Green else Color(0xFFE1EFE3),
        shape = CircleShape
    ) {
        Box(contentAlignment = Alignment.Center) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                tint = if (dark) Color.White else Green,
                modifier = Modifier.size(iconSize.dp)
            )
        }
    }
}

@Composable
private fun ProgressBubble(modifier: Modifier = Modifier) {
    Surface(
        modifier = modifier
            .size(72.dp)
            .shadow(12.dp, CircleShape, spotColor = Green.copy(alpha = 0.3f)),
        shape = CircleShape,
        color = Green
    ) {
        Box(contentAlignment = Alignment.Center) {
            Canvas(modifier = Modifier.size(54.dp)) {
                drawCircle(Color.White.copy(alpha = 0.35f), radius = size.minDimension / 2, style = Stroke(width = 8f))
                drawArc(Color.White, -90f, 250f, false, style = Stroke(width = 8f))
            }
            Text("60%", color = Color.White, fontSize = 11.sp, fontWeight = FontWeight.Bold)
        }
    }
}

@Composable
private fun PageIndicator(pageIndex: Int, pageCount: Int) {
    Row(horizontalArrangement = Arrangement.spacedBy(8.dp), verticalAlignment = Alignment.CenterVertically) {
        repeat(pageCount) { index ->
            Box(
                modifier = Modifier
                    .size(width = if (index == pageIndex) 34.dp else 7.dp, height = 7.dp)
                    .background(
                        color = if (index == pageIndex) Green else Color(0xFFC4D1C2),
                        shape = RoundedCornerShape(50)
                    )
            )
        }
    }
}

@Composable
private fun LeafLogo(modifier: Modifier = Modifier) {
    Canvas(modifier = modifier) {
        val path = Path().apply {
            moveTo(size.width * 0.50f, size.height * 0.94f)
            cubicTo(size.width * 0.12f, size.height * 0.55f, size.width * 0.28f, size.height * 0.12f, size.width * 0.86f, size.height * 0.08f)
            cubicTo(size.width * 0.92f, size.height * 0.54f, size.width * 0.74f, size.height * 0.84f, size.width * 0.50f, size.height * 0.94f)
            close()
        }
        drawPath(path, Green)
        drawLine(Color.White.copy(alpha = 0.85f), Offset(size.width * 0.50f, size.height * 0.88f), Offset(size.width * 0.77f, size.height * 0.28f), 3.2f)
    }
}

private data class OnboardingPage(
    val title: String,
    val description: String,
    val button: String,
    val kind: OnboardingArt
)

private enum class OnboardingArt {
    Leaf,
    Reminders,
    Expert
}

@Preview(showBackground = true, widthDp = 360, heightDp = 800)
@Composable
private fun SplashPreview() {
    SplashScreen()
}

@Preview(showBackground = true, widthDp = 360, heightDp = 800)
@Composable
private fun OnboardingPreview() {
    OnboardingScreen(
        page = OnboardingPage(
            title = "Organiza tus plantas",
            description = "Transforma tu hogar en un oasis urbano con recordatorios inteligentes y consejos de expertos.",
            button = "Siguiente",
            kind = OnboardingArt.Leaf
        ),
        pageIndex = 0,
        pageCount = 3,
        onNext = {},
        onSkip = {}
    )
}
