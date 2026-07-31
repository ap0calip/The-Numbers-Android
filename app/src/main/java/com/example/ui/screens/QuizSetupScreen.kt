package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CheckboxDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.theme.AppBorderOrange
import com.example.ui.theme.AppGreen
import com.example.ui.theme.AppTextDark
import com.example.ui.theme.AppYellow
import com.example.ui.viewmodel.MathLevel
import com.example.ui.viewmodel.MathUiState
import com.example.ui.viewmodel.MathOperator

@Composable
fun QuizSetupScreen(
    uiState: MathUiState,
    onSelectOperator: (MathOperator) -> Unit,
    onSelectLevel: (MathLevel) -> Unit,
    onToggleShowTimer: (Boolean) -> Unit,
    onStartQuiz: () -> Unit,
    onOpenStickers: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(12.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 6.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        shape = RoundedCornerShape(16.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "🎯 Practice Quiz Setup",
                color = AppTextDark,
                fontSize = 22.sp,
                fontWeight = FontWeight.Black,
                textAlign = TextAlign.Center
            )

            Text(
                text = "Choose your math challenge rules!",
                color = Color.Gray,
                fontSize = 14.sp,
                fontWeight = FontWeight.Medium,
                textAlign = TextAlign.Center
            )

            Spacer(modifier = Modifier.height(16.dp))

            // 1. Operator Selection
            Text(
                text = "Select Operator:",
                color = AppTextDark,
                fontSize = 15.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(8.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                MathOperator.entries.forEach { op ->
                    val isSelected = uiState.operator == op
                    Box(
                        modifier = Modifier
                            .weight(1f)
                            .height(50.dp)
                            .background(
                                if (isSelected) AppYellow else Color(0xFFF5F5F5),
                                RoundedCornerShape(12.dp)
                            )
                            .border(
                                width = if (isSelected) 2.5.dp else 1.dp,
                                color = if (isSelected) AppBorderOrange else Color.LightGray,
                                shape = RoundedCornerShape(12.dp)
                            )
                            .clickable { onSelectOperator(op) }
                            .testTag("quiz_setup_op_${op.name.lowercase()}"),
                        contentAlignment = Alignment.Center
                    ) {
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Text(
                                text = op.symbol,
                                color = AppTextDark,
                                fontSize = 22.sp,
                                fontWeight = FontWeight.Black
                            )
                            Text(
                                text = op.label,
                                color = AppTextDark,
                                fontSize = 10.sp,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // 2. Level Selection
            Text(
                text = "Select Level Range:",
                color = AppTextDark,
                fontSize = 15.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(8.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                MathLevel.entries.forEach { level ->
                    val isSelected = uiState.level == level
                    Box(
                        modifier = Modifier
                            .weight(1f)
                            .height(44.dp)
                            .background(
                                if (isSelected) AppYellow else Color(0xFFF5F5F5),
                                RoundedCornerShape(12.dp)
                            )
                            .border(
                                width = if (isSelected) 2.5.dp else 1.dp,
                                color = if (isSelected) AppBorderOrange else Color.LightGray,
                                shape = RoundedCornerShape(12.dp)
                            )
                            .clickable { onSelectLevel(level) }
                            .testTag("quiz_setup_level_${level.name.lowercase()}"),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = level.label,
                            color = AppTextDark,
                            fontSize = 13.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // 3. Show Timer Checkbox
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Color(0xFFFFF9C4), RoundedCornerShape(12.dp))
                    .padding(horizontal = 12.dp, vertical = 6.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Checkbox(
                    checked = uiState.showTimerDuringTest,
                    onCheckedChange = { onToggleShowTimer(it) },
                    colors = CheckboxDefaults.colors(
                        checkedColor = AppBorderOrange,
                        uncheckedColor = Color.Gray
                    ),
                    modifier = Modifier.testTag("quiz_setup_show_timer_checkbox")
                )
                Spacer(modifier = Modifier.width(4.dp))
                Text(
                    text = "Show timer on screen during test",
                    color = AppTextDark,
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Bold
                )
            }

            Spacer(modifier = Modifier.height(12.dp))

            // Best Time Display
            val bestStr = uiState.currentBestTimeSeconds?.let { "${it}s" } ?: "None yet"
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Color(0xFFE8F5E9), RoundedCornerShape(8.dp))
                    .padding(horizontal = 12.dp, vertical = 8.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "🏆 Best Time (${uiState.operator.symbol} ${uiState.operator.label}, ${uiState.level.label}):",
                    color = Color(0xFF2E7D32),
                    fontSize = 13.sp,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = bestStr,
                    color = Color(0xFF1B5E20),
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Black
                )
            }

            Spacer(modifier = Modifier.height(20.dp))

            // Start Quiz Button
            Button(
                onClick = onStartQuiz,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(52.dp)
                    .testTag("quiz_setup_start_button"),
                colors = ButtonDefaults.buttonColors(containerColor = AppGreen),
                shape = RoundedCornerShape(12.dp)
            ) {
                Text(
                    text = "START QUIZ 🚀",
                    color = AppTextDark,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Black
                )
            }

            Spacer(modifier = Modifier.height(10.dp))

            // Sticker Album Button
            Button(
                onClick = onOpenStickers,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(44.dp)
                    .testTag("quiz_setup_stickers_button"),
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFE0F7FA)),
                shape = RoundedCornerShape(12.dp)
            ) {
                Text(
                    text = "🎨 My Sticker Album (${uiState.unlockedStickersList.size} unlocked)",
                    color = Color(0xFF006064),
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Bold
                )
            }
        }
    }

    Spacer(modifier = Modifier.height(12.dp))

    // Instructions Card Below
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 12.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
        colors = CardDefaults.cardColors(containerColor = Color(0xFFFFF8E1)),
        shape = RoundedCornerShape(16.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            Text(
                text = "💡 Quiz Instructions",
                color = AppTextDark,
                fontSize = 15.sp,
                fontWeight = FontWeight.Black
            )
            Spacer(modifier = Modifier.height(6.dp))
            Text(
                text = "• Answer 10 math problems to complete the quiz.\n• Tap a mystery block to reveal characters if you need help.\n• Use the number pad & Enter to submit your answers.\n• You earn 1 star for each correct answer.\n• A wrong answer will take away 1 star, if you have any.\n• Collect all 10 stars to unlock special stickers in your album!",
                color = Color.DarkGray,
                fontSize = 13.sp,
                fontWeight = FontWeight.Medium
            )
        }
    }
}
