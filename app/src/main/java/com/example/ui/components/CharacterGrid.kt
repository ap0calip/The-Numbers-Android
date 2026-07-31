package com.example.ui.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.scaleIn
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun CharacterGrid(
    count: Int,
    onCharacterTap: (Int) -> Unit,
    modifier: Modifier = Modifier,
    spriteSize: Dp = 34.dp
) {
    Box(
        modifier = modifier
            .fillMaxSize()
            .padding(4.dp),
        contentAlignment = Alignment.Center
    ) {
        if (count <= 0) {
            // Empty box visual indicator or subtle count 0 text
            Text(
                text = "",
                color = Color.Black.copy(alpha = 0.3f),
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold
            )
        } else {
            FlowRow(
                modifier = Modifier
                    .fillMaxSize()
                    .verticalScroll(rememberScrollState())
                    .padding(2.dp)
                    .testTag("character_grid_flow"),
                horizontalArrangement = Arrangement.Center,
                verticalArrangement = Arrangement.Center,
                maxItemsInEachRow = if (count <= 6) 3 else if (count <= 12) 4 else 5
            ) {
                for (i in 1..count) {
                    AnimatedVisibility(
                        visible = true,
                        enter = fadeIn() + scaleIn()
                    ) {
                        CharacterSprite(
                            indexNumber = i,
                            size = spriteSize,
                            onTap = onCharacterTap,
                            modifier = Modifier.padding(2.dp)
                        )
                    }
                }
            }
        }
    }
}
