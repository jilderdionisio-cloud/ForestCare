package com.plant.forestcare.ui.design

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Add
import androidx.compose.material.icons.rounded.CheckCircle
import androidx.compose.material.icons.rounded.Spa
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun PlantCareSurfaceCard(
    modifier: Modifier = Modifier,
    content: @Composable () -> Unit
) {
    Surface(
        modifier = modifier
            .fillMaxWidth()
            .animateContentSize(),
        color = PlantCareColors.Surface,
        shape = RoundedCornerShape(PlantCareShape.Card),
        shadowElevation = PlantCareElevation.Card,
        content = content
    )
}

@Composable
fun PlantCard(
    modifier: Modifier = Modifier,
    content: @Composable () -> Unit
) = PlantCareSurfaceCard(modifier = modifier, content = content)

@Composable
fun CarePlanCard(
    modifier: Modifier = Modifier,
    content: @Composable () -> Unit
) = PlantCareSurfaceCard(modifier = modifier, content = content)

@Composable
fun PlantHeaderCard(
    modifier: Modifier = Modifier,
    content: @Composable () -> Unit
) = PlantCareSurfaceCard(modifier = modifier, content = content)

@Composable
fun ReminderCard(
    modifier: Modifier = Modifier,
    content: @Composable () -> Unit
) = PlantCareSurfaceCard(modifier = modifier, content = content)

@Composable
fun StatusBadge(
    text: String,
    color: Color = PlantCareColors.PrimaryGreen
) {
    Row(
        modifier = Modifier
            .clip(RoundedCornerShape(PlantCareShape.Pill))
            .background(color.copy(alpha = 0.12f))
            .padding(horizontal = PlantCareSpacing.Md, vertical = PlantCareSpacing.Xs),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(PlantCareSpacing.Xs)
    ) {
        Box(modifier = Modifier.size(6.dp).clip(CircleShape).background(color))
        Text(text, color = color, style = PlantCareTextStyles.State, maxLines = 1, overflow = TextOverflow.Ellipsis)
    }
}

@Composable
fun InfoChip(text: String, icon: ImageVector? = null) {
    Row(
        modifier = Modifier
            .clip(RoundedCornerShape(PlantCareShape.Pill))
            .background(PlantCareColors.LightGreen)
            .padding(horizontal = PlantCareSpacing.Md, vertical = PlantCareSpacing.Sm),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(PlantCareSpacing.Xs)
    ) {
        if (icon != null) Icon(icon, contentDescription = null, tint = PlantCareColors.PrimaryGreen, modifier = Modifier.size(PlantCareSize.Icon))
        Text(text, color = PlantCareColors.TextPrimary, style = PlantCareTextStyles.Secondary)
    }
}

@Composable
fun ActionButton(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    icon: ImageVector? = null
) {
    Button(
        onClick = onClick,
        enabled = enabled,
        modifier = modifier.height(PlantCareSize.MinTouchTarget),
        shape = RoundedCornerShape(PlantCareShape.Button),
        colors = ButtonDefaults.buttonColors(containerColor = PlantCareColors.PrimaryGreen)
    ) {
        if (icon != null) {
            Icon(icon, contentDescription = null, modifier = Modifier.size(PlantCareSize.Icon))
            Spacer(Modifier.width(PlantCareSpacing.Sm))
        }
        Text(text, fontWeight = FontWeight.SemiBold)
    }
}

@Composable
fun PlantCareFloatingActionButton(onClick: () -> Unit) {
    FloatingActionButton(
        onClick = onClick,
        containerColor = PlantCareColors.PrimaryGreen,
        contentColor = PlantCareColors.Surface,
        shape = CircleShape,
        modifier = Modifier.size(PlantCareSize.Fab)
    ) {
        Icon(
            imageVector = Icons.Rounded.Add,
            contentDescription = "Agregar nueva planta",
            tint = PlantCareColors.Surface,
            modifier = Modifier.size(26.dp)
        )
    }
}

@Composable
fun EmptyStateCard(
    title: String,
    message: String,
    modifier: Modifier = Modifier
) {
    PlantCareSurfaceCard(modifier = modifier) {
        Column(
            modifier = Modifier.padding(PlantCareSpacing.Xl),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(PlantCareSpacing.Sm)
        ) {
            Icon(Icons.Rounded.Spa, contentDescription = null, tint = PlantCareColors.PrimaryGreen, modifier = Modifier.size(36.dp))
            Text(title, color = PlantCareColors.TextPrimary, style = PlantCareTextStyles.CardTitle)
            Text(message, color = PlantCareColors.TextSecondary, style = PlantCareTextStyles.Secondary)
        }
    }
}

@Composable
fun LoadingCard(message: String, visible: Boolean = true) {
    AnimatedVisibility(visible = visible) {
        PlantCareSurfaceCard {
            Row(
                modifier = Modifier.padding(PlantCareSpacing.Xl),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(PlantCareSpacing.Md)
            ) {
                CircularProgressIndicator(color = PlantCareColors.PrimaryGreen, modifier = Modifier.size(24.dp))
                Text(message, color = PlantCareColors.TextPrimary, style = PlantCareTextStyles.Body)
            }
        }
    }
}

@Composable
fun RecommendationCard(title: String, message: String) {
    PlantCareSurfaceCard {
        Column(modifier = Modifier.padding(PlantCareSpacing.Lg), verticalArrangement = Arrangement.spacedBy(PlantCareSpacing.Sm)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(Icons.Rounded.CheckCircle, contentDescription = null, tint = PlantCareColors.Success, modifier = Modifier.size(PlantCareSize.Icon))
                Spacer(Modifier.width(PlantCareSpacing.Sm))
                Text(title, color = PlantCareColors.TextPrimary, style = PlantCareTextStyles.CardTitle)
            }
            Text(message, color = PlantCareColors.TextSecondary, style = PlantCareTextStyles.Body)
        }
    }
}
