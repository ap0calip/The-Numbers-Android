package com.example.ui.components

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.keyframes
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.graphics.lerp
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.R
import com.example.ui.theme.AppBorderOrange
import com.example.ui.theme.AppCyan
import com.example.ui.theme.AppCyanFocused
import com.example.ui.theme.AppTextDark
import kotlinx.coroutines.launch

@Composable
fun CyanDisplayBox(
    text: String,
    isSelected: Boolean = false,
    onClick: () -> Unit = {},
    modifier: Modifier = Modifier,
    testTag: String = "",
    shakeTrigger: Long = 0L
) {
    val offsetX = remember { Animatable(0f) }
    val scale = remember { Animatable(1f) }
    val rotation = remember { Animatable(0f) }

    LaunchedEffect(shakeTrigger) {
        if (shakeTrigger > 0L) {
            offsetX.snapTo(0f)
            scale.snapTo(1f)
            rotation.snapTo(0f)

            launch {
                offsetX.animateTo(
                    targetValue = 0f,
                    animationSpec = keyframes {
                        durationMillis = 450
                        0f at 0
                        (-12f) at 50
                        12f at 100
                        (-10f) at 160
                        10f at 220
                        (-6f) at 290
                        6f at 360
                        (-2f) at 410
                        0f at 450
                    }
                )
            }

            launch {
                scale.animateTo(
                    targetValue = 1f,
                    animationSpec = keyframes {
                        durationMillis = 450
                        1f at 0
                        1.45f at 90
                        0.88f at 200
                        1.2f at 300
                        0.96f at 390
                        1f at 450
                    }
                )
            }

            launch {
                rotation.animateTo(
                    targetValue = 0f,
                    animationSpec = keyframes {
                        durationMillis = 450
                        0f at 0
                        (-18f) at 70
                        18f at 170
                        (-10f) at 270
                        10f at 360
                        0f at 450
                    }
                )
            }
        }
    }

    val borderColor = if (isSelected) Color(0xFFFFD700) else AppBorderOrange
    val borderWidth = if (isSelected) 3.5.dp else 1.5.dp
    val bgColor = if (isSelected) AppCyanFocused else AppCyan
    val boxShape = RoundedCornerShape(16.dp)

    val textColor = AppTextDark

    val interactionSource = remember { MutableInteractionSource() }

    Box(
        modifier = modifier
            .graphicsLayer {
                translationX = offsetX.value
            }
            .background(bgColor, boxShape)
            .border(borderWidth, borderColor, boxShape)
            .clickable(
                interactionSource = interactionSource,
                indication = null,
                onClick = onClick
            )
            .testTag(testTag),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = text,
            color = textColor,
            fontSize = 38.sp,
            fontWeight = FontWeight.Black,
            fontFamily = FontFamily.Serif,
            modifier = if (text == "?") {
                Modifier.graphicsLayer {
                    scaleX = scale.value
                    scaleY = scale.value
                    rotationZ = rotation.value
                }
            } else {
                Modifier
            }
        )
    }
}

@Composable
fun CyanVisualBox(
    count: Int,
    isSelected: Boolean = false,
    onClick: () -> Unit = {},
    onCharacterTap: (Int) -> Unit = {},
    isBlocked: Boolean = false,
    onBlockClick: () -> Unit = {},
    modifier: Modifier = Modifier,
    testTag: String = ""
) {
    val borderColor = if (isSelected) Color(0xFFFFD700) else AppBorderOrange
    val borderWidth = if (isSelected) 3.5.dp else 1.5.dp
    val bgColor = if (isSelected) AppCyanFocused else AppCyan
    val boxShape = RoundedCornerShape(16.dp)

    val interactionSource = remember { MutableInteractionSource() }

    Box(
        modifier = modifier
            .background(bgColor, boxShape)
            .border(borderWidth, borderColor, boxShape)
            .clickable(
                interactionSource = interactionSource,
                indication = null,
                onClick = {
                    if (isBlocked) {
                        onBlockClick()
                    } else {
                        onClick()
                    }
                }
            )
            .testTag(testTag),
        contentAlignment = Alignment.Center
    ) {
        if (isBlocked) {
            Image(
                painter = painterResource(id = R.drawable.block),
                contentDescription = "Hidden Block",
                modifier = Modifier
                    .fillMaxSize()
                    .padding(8.dp),
                contentScale = ContentScale.Fit
            )
        } else {
            CharacterGrid(
                count = count,
                onCharacterTap = onCharacterTap
            )
        }
    }
}
