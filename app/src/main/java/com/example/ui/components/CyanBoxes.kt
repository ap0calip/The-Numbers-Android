package com.example.ui.components

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
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
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

@Composable
fun CyanDisplayBox(
    text: String,
    isSelected: Boolean = false,
    onClick: () -> Unit = {},
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
                onClick = onClick
            )
            .testTag(testTag),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = text,
            color = AppTextDark,
            fontSize = 38.sp,
            fontWeight = FontWeight.Black,
            fontFamily = FontFamily.Serif
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
