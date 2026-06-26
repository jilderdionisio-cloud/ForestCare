package com.plant.forestcare.ui.auth

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.ArrowForward
import androidx.compose.material.icons.rounded.LocalFlorist
import androidx.compose.material.icons.rounded.Visibility
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.plant.forestcare.ui.components.AnimatedEntry

private val AuthBackground = Color(0xFFFFFCF5)
private val AuthGreen = Color(0xFF007F25)
private val AuthGreenSoft = Color(0xFFEAF6E7)
private val AuthText = Color(0xFF232821)
private val AuthMuted = Color(0xFF7B8479)
private val FieldBackground = Color(0xFFF5F0E8)
private val FieldHint = Color(0xFFB9C4B8)

@Composable
fun LoginRoute(
    onLoginSuccess: () -> Unit,
    onRegisterClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    LoginScreen(
        onLoginSuccess = onLoginSuccess,
        onRegisterClick = onRegisterClick,
        modifier = modifier
    )
}

@Composable
fun RegisterRoute(
    onRegisterSuccess: () -> Unit,
    onLoginClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    RegisterScreen(
        onRegisterSuccess = onRegisterSuccess,
        onLoginClick = onLoginClick,
        modifier = modifier
    )
}

@Composable
fun LoginScreen(
    onLoginSuccess: () -> Unit,
    onRegisterClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var error by remember { mutableStateOf<String?>(null) }

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(AuthBackground)
    ) {
        LoginPlantBackdrop()
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 20.dp, vertical = 58.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            AnimatedEntry {
                Surface(
                    modifier = Modifier
                        .fillMaxWidth()
                        .shadow(18.dp, RoundedCornerShape(34.dp), spotColor = Color.Black.copy(alpha = 0.08f)),
                    shape = RoundedCornerShape(34.dp),
                    color = Color.White.copy(alpha = 0.96f)
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .animateContentSize()
                            .padding(horizontal = 30.dp, vertical = 32.dp),
                        verticalArrangement = Arrangement.spacedBy(18.dp)
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                            Icon(Icons.Rounded.LocalFlorist, contentDescription = null, tint = AuthGreen, modifier = Modifier.size(28.dp))
                            Text("PlantCare", color = AuthGreen, fontSize = 24.sp, fontWeight = FontWeight.Medium)
                        }
                        Column(verticalArrangement = Arrangement.spacedBy(5.dp)) {
                            Text("Bienvenido de nuevo", color = AuthText, fontSize = 25.sp, fontWeight = FontWeight.Bold)
                            Text("Cultiva tu calma, una hoja a la vez.", color = AuthMuted, fontSize = 14.sp, fontWeight = FontWeight.Medium)
                        }
                        Spacer(Modifier.height(8.dp))
                        AuthField(
                            label = "Email",
                            value = email,
                            placeholder = "tu@email.com",
                            onValueChange = {
                                email = it
                                error = null
                            }
                        )
                        AuthField(
                            label = "Contraseña",
                            value = password,
                            placeholder = "••••••••",
                            onValueChange = {
                                password = it
                                error = null
                            },
                            isPassword = true,
                            trailingLabel = "¿Olvidaste tu contraseña?"
                        )
                        AnimatedVisibility(error != null) {
                            Text(error.orEmpty(), color = Color(0xFFB3261E), fontSize = 12.sp, fontWeight = FontWeight.SemiBold)
                        }
                        Button(
                            onClick = {
                                error = when {
                                    email.isBlank() -> "Ingresa tu correo."
                                    !email.contains("@") -> "Ingresa un correo válido."
                                    password.isBlank() -> "Ingresa tu contraseña."
                                    else -> null
                                }
                                if (error == null) onLoginSuccess()
                            },
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(54.dp)
                                .shadow(10.dp, RoundedCornerShape(28.dp), spotColor = AuthGreen.copy(alpha = 0.28f)),
                            shape = RoundedCornerShape(28.dp),
                            colors = ButtonDefaults.buttonColors(containerColor = AuthGreen)
                        ) {
                            Text("Iniciar sesión", color = Color.White, fontSize = 15.sp, fontWeight = FontWeight.Bold)
                        }
                        SeparatorText()
                        Surface(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(52.dp)
                                .border(1.dp, Color(0xFFD8DED4), RoundedCornerShape(28.dp))
                                .clickable(onClick = onLoginSuccess),
                            shape = RoundedCornerShape(28.dp),
                            color = Color.White
                        ) {
                            Row(
                                modifier = Modifier.fillMaxSize(),
                                horizontalArrangement = Arrangement.Center,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                GoogleMark()
                                Spacer(Modifier.size(14.dp))
                                Text("Google Login", color = Color(0xFF5D645D), fontSize = 15.sp, fontWeight = FontWeight.Bold)
                            }
                        }
                        Spacer(Modifier.height(18.dp))
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.Center,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text("¿No tienes una cuenta? ", color = AuthMuted, fontSize = 14.sp, fontWeight = FontWeight.SemiBold)
                            Text(
                                "Registrarse",
                                color = AuthGreen,
                                fontSize = 14.sp,
                                fontWeight = FontWeight.Bold,
                                modifier = Modifier.clickable(onClick = onRegisterClick)
                            )
                        }
                    }
                }
            }
            Spacer(Modifier.height(42.dp))
            AuthDots()
        }
    }
}

@Composable
fun RegisterScreen(
    onRegisterSuccess: () -> Unit,
    onLoginClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    var name by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var confirmPassword by remember { mutableStateOf("") }
    var error by remember { mutableStateOf<String?>(null) }

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(
                Brush.radialGradient(
                    colors = listOf(Color(0xFFEAF8E9), AuthBackground),
                    center = Offset(700f, 60f),
                    radius = 760f
                )
            )
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 22.dp, vertical = 44.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Surface(
                modifier = Modifier.size(58.dp),
                shape = CircleShape,
                color = Color(0xFF4CAF50)
            ) {
                Box(contentAlignment = Alignment.Center) {
                    Icon(Icons.Rounded.LocalFlorist, contentDescription = null, tint = Color(0xFF0B421D), modifier = Modifier.size(30.dp))
                }
            }
            Spacer(Modifier.height(24.dp))
            Text("Comienza tu jardín", color = AuthGreen, fontSize = 26.sp, fontWeight = FontWeight.Bold)
            Spacer(Modifier.height(10.dp))
            Text(
                text = "Únete a nuestra comunidad de entusiastas de\nlas plantas y cuida de tu colección.",
                color = Color(0xFF30382F),
                fontSize = 13.sp,
                lineHeight = 19.sp,
                textAlign = TextAlign.Center
            )
            Spacer(Modifier.height(42.dp))
            AnimatedEntry(delayMillis = 100) {
                Surface(
                modifier = Modifier
                    .fillMaxWidth()
                    .shadow(14.dp, RoundedCornerShape(28.dp), spotColor = Color.Black.copy(alpha = 0.06f)),
                shape = RoundedCornerShape(28.dp),
                color = Color.White
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .animateContentSize()
                        .padding(horizontal = 22.dp, vertical = 24.dp),
                    verticalArrangement = Arrangement.spacedBy(15.dp)
                ) {
                    AuthField("Nombre", name, "Tu nombre completo", {
                        name = it
                        error = null
                    })
                    AuthField("Correo electrónico", email, "ejemplo@correo.com", {
                        email = it
                        error = null
                    })
                    AuthField("Contraseña", password, "Mínimo 8 caracteres", {
                        password = it
                        error = null
                    }, isPassword = true, showEye = true)
                    AuthField("Confirmar contraseña", confirmPassword, "Repite tu contraseña", {
                        confirmPassword = it
                        error = null
                    }, isPassword = true)
                    AnimatedVisibility(error != null) {
                        Text(error.orEmpty(), color = Color(0xFFB3261E), fontSize = 12.sp, fontWeight = FontWeight.SemiBold)
                    }
                    Button(
                        onClick = {
                            error = when {
                                name.isBlank() -> "Ingresa tu nombre."
                                !email.contains("@") -> "Ingresa un correo válido."
                                password.length < 8 -> "La contraseña debe tener mínimo 8 caracteres."
                                password != confirmPassword -> "Las contraseñas no coinciden."
                                else -> null
                            }
                            if (error == null) onRegisterSuccess()
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(58.dp)
                            .shadow(14.dp, RoundedCornerShape(30.dp), spotColor = AuthGreen.copy(alpha = 0.30f)),
                        shape = RoundedCornerShape(30.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = AuthGreen)
                    ) {
                        Text("Crear cuenta", color = Color.White, fontSize = 19.sp, fontWeight = FontWeight.Bold)
                        Spacer(Modifier.size(10.dp))
                        Icon(Icons.AutoMirrored.Rounded.ArrowForward, contentDescription = null, tint = Color.White)
                    }
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.Center,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text("¿Ya tienes una cuenta? ", color = Color(0xFF263026), fontSize = 12.sp)
                        Text(
                            "Inicia sesión",
                            color = AuthGreen,
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Bold,
                            modifier = Modifier.clickable(onClick = onLoginClick)
                        )
                    }
                }
            }
            }
            Spacer(Modifier.height(44.dp))
            Text(
                text = "Al registrarte, aceptas nuestros Términos de\nservicio y Política de privacidad.",
                color = Color(0xFF70796D),
                fontSize = 11.sp,
                lineHeight = 15.sp,
                textAlign = TextAlign.Center,
                textDecoration = TextDecoration.Underline
            )
        }
    }
}

@Composable
private fun AuthField(
    label: String,
    value: String,
    placeholder: String,
    onValueChange: (String) -> Unit,
    isPassword: Boolean = false,
    showEye: Boolean = false,
    trailingLabel: String? = null
) {
    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Text(label, color = Color(0xFF6A7367), fontSize = 11.sp, fontWeight = FontWeight.Bold, modifier = Modifier.weight(1f))
            trailingLabel?.let {
                Text(it, color = AuthGreen, fontSize = 11.sp, fontWeight = FontWeight.Bold)
            }
        }
        BasicTextField(
            value = value,
            onValueChange = onValueChange,
            singleLine = true,
            visualTransformation = if (isPassword) PasswordVisualTransformation() else VisualTransformation.None,
            textStyle = TextStyle(color = AuthText, fontSize = 15.sp, fontWeight = FontWeight.SemiBold),
            modifier = Modifier
                .fillMaxWidth()
                .height(52.dp)
                .background(FieldBackground, RoundedCornerShape(18.dp))
                .padding(horizontal = 18.dp),
            decorationBox = { inner ->
                Row(
                    modifier = Modifier.fillMaxSize(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Box(modifier = Modifier.weight(1f), contentAlignment = Alignment.CenterStart) {
                        if (value.isBlank()) {
                            Text(placeholder, color = FieldHint, fontSize = 15.sp, fontWeight = FontWeight.Medium)
                        }
                        inner()
                    }
                    if (showEye) {
                        Icon(Icons.Rounded.Visibility, contentDescription = null, tint = Color(0xFFB8C2B6), modifier = Modifier.size(19.dp))
                    }
                }
            }
        )
    }
}

@Composable
private fun SeparatorText() {
    Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(14.dp)) {
        Box(
            modifier = Modifier
                .weight(1f)
                .height(1.dp)
                .background(Color(0xFFDDE4D9))
        )
        Text("O continúa con", color = Color(0xFF9AA298), fontSize = 13.sp, fontWeight = FontWeight.Bold)
        Box(
            modifier = Modifier
                .weight(1f)
                .height(1.dp)
                .background(Color(0xFFDDE4D9))
        )
    }
}

@Composable
private fun GoogleMark() {
    Box(modifier = Modifier.size(20.dp), contentAlignment = Alignment.Center) {
        Text("G", color = Color(0xFF4285F4), fontSize = 18.sp, fontWeight = FontWeight.ExtraBold)
        Text("G", color = Color(0xFFEA4335), fontSize = 18.sp, fontWeight = FontWeight.ExtraBold, modifier = Modifier.padding(start = 1.dp))
    }
}

@Composable
private fun AuthDots() {
    Row(horizontalArrangement = Arrangement.spacedBy(14.dp)) {
        repeat(3) { index ->
            Box(
                modifier = Modifier
                    .size(7.dp)
                    .background(
                        if (index == 0) Color(0xFF5CAC78) else Color(0xFF8DB795),
                        CircleShape
                    )
            )
        }
    }
}

@Composable
private fun LoginPlantBackdrop() {
    Canvas(modifier = Modifier.fillMaxSize()) {
        drawRect(Color(0xFFF5F1E8))
        repeat(9) { index ->
            val x = size.width * (0.05f + index * 0.12f)
            drawLine(
                color = Color(0xFFDDE5D7).copy(alpha = 0.55f),
                start = Offset(x, 0f),
                end = Offset(x + size.width * 0.34f, size.height),
                strokeWidth = 7f
            )
        }
        repeat(10) { index ->
            val center = Offset(size.width * (0.08f + (index % 5) * 0.22f), size.height * (0.08f + (index / 5) * 0.72f))
            val leaf = Path().apply {
                moveTo(center.x, center.y + 70f)
                cubicTo(center.x - 70f, center.y, center.x - 20f, center.y - 82f, center.x + 78f, center.y - 60f)
                cubicTo(center.x + 62f, center.y + 16f, center.x + 32f, center.y + 58f, center.x, center.y + 70f)
                close()
            }
            drawPath(leaf, Color(0xFFCAD8C5).copy(alpha = 0.28f))
        }
        drawRect(
            Brush.verticalGradient(
                colors = listOf(Color.Transparent, AuthBackground.copy(alpha = 0.94f)),
                startY = size.height * 0.46f,
                endY = size.height
            )
        )
    }
}

@Preview(showBackground = true, widthDp = 360, heightDp = 800)
@Composable
private fun LoginPreview() {
    LoginScreen(onLoginSuccess = {}, onRegisterClick = {})
}

@Preview(showBackground = true, widthDp = 360, heightDp = 800)
@Composable
private fun RegisterPreview() {
    RegisterScreen(onRegisterSuccess = {}, onLoginClick = {})
}
