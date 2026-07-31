package com.example.ui.components

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.spring
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.example.R
import kotlinx.coroutines.launch

@Composable
fun CharacterSprite(
    indexNumber: Int? = null,
    size: Dp = 38.dp,
    onTap: ((Int) -> Unit)? = null,
    modifier: Modifier = Modifier
) {
    val scale = remember { Animatable(1f) }
    val scope = rememberCoroutineScope()
    val interactionSource = remember { MutableInteractionSource() }

    Box(
        modifier = modifier
            .size(size)
            .scale(scale.value)
            .clickable(
                interactionSource = interactionSource,
                indication = null
            ) {
                scope.launch {
                    scale.animateTo(1.35f, spring(dampingRatio = 0.4f))
                    scale.animateTo(1f, spring(dampingRatio = 0.6f))
                }
                if (indexNumber != null && onTap != null) {
                    onTap(indexNumber)
                }
            }
            .testTag("character_sprite_${indexNumber ?: 0}"),
        contentAlignment = Alignment.Center
    ) {
        // Render generated image sprite or vector mascot
        Image(
            painter = painterResource(id = R.drawable.character),
            contentDescription = "Cute Character ${indexNumber ?: ""}",
            modifier = Modifier
                .fillMaxSize()
                .padding(1.dp)
                .clip(CircleShape),
            contentScale = ContentScale.Fit
        )
    }
}

@Composable
fun VectorGreenCharacter(modifier: Modifier = Modifier) {
    Canvas(modifier = modifier) {
        val w = size.width
        val h = size.height

        // Body gradient/color - Glossy Lime Green
        val bodyColor = Color(0xFF4CAF50)
        val bodyColorLight = Color(0xFF81C784)
        val eyeColor = Color(0xFF1B5E20)
        val cheekColor = Color(0xFF81C784)

        // Draw Jelly Body (Round blob)
        drawCircle(
            color = bodyColor,
            radius = w * 0.42f,
            center = Offset(w * 0.5f, h * 0.52f)
        )
        // Highlight shine
        drawCircle(
            color = Color.White.copy(alpha = 0.4f),
            radius = w * 0.12f,
            center = Offset(w * 0.38f, h * 0.35f)
        )

        // Eyes
        val eyeRadius = w * 0.09f
        // Left eye
        drawCircle(
            color = Color.White,
            radius = eyeRadius,
            center = Offset(w * 0.36f, h * 0.45f)
        )
        drawCircle(
            color = eyeColor,
            radius = eyeRadius * 0.55f,
            center = Offset(w * 0.36f, h * 0.45f)
        )
        // Right eye
        drawCircle(
            color = Color.White,
            radius = eyeRadius,
            center = Offset(w * 0.64f, h * 0.45f)
        )
        drawCircle(
            color = eyeColor,
            radius = eyeRadius * 0.55f,
            center = Offset(w * 0.64f, h * 0.45f)
        )

        // Cheeks
        drawCircle(
            color = cheekColor.copy(alpha = 0.6f),
            radius = w * 0.08f,
            center = Offset(w * 0.25f, h * 0.58f)
        )
        drawCircle(
            color = cheekColor.copy(alpha = 0.6f),
            radius = w * 0.08f,
            center = Offset(w * 0.75f, h * 0.58f)
        )

        // Happy Mouth Smile
        val path = Path().apply {
            moveTo(w * 0.4f, h * 0.62f)
            quadraticTo(w * 0.5f, h * 0.74f, w * 0.6f, h * 0.62f)
        }
        drawPath(
            path = path,
            color = eyeColor,
            style = Stroke(width = w * 0.06f)
        )
    }
}
