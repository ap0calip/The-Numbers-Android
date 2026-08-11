package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.components.CyanDisplayBox
import com.example.ui.components.CyanVisualBox
import com.example.ui.theme.AppBorderOrange
import com.example.ui.theme.AppGreen
import com.example.ui.theme.AppTextDark
import com.example.ui.theme.AppYellow
import com.example.ui.viewmodel.MathLevel
import com.example.ui.viewmodel.MathUiState

@Composable
fun QuizActiveScreen(
    uiState: MathUiState,
    onCharacterTapped: (Int) -> Unit
) {
    val problem = uiState.quizProblem
    val showVisualAid = uiState.level == MathLevel.RANGE_10 || uiState.level == MathLevel.RANGE_12

    var isTerm1Revealed by remember(problem) { mutableStateOf(false) }
    var isTerm2Revealed by remember(problem) { mutableStateOf(false) }

    Column(
        modifier = Modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Top Header: Stars Progress & Timer
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 8.dp, vertical = 4.dp),
            elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
            colors = CardDefaults.cardColors(containerColor = Color.White),
            shape = RoundedCornerShape(12.dp)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(10.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "⭐ Stars: ${uiState.currentStars} / 10",
                        color = AppTextDark,
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Black,
                        modifier = Modifier.testTag("quiz_stars_counter")
                    )

                    if (uiState.showTimerDuringTest) {
                        val mins = uiState.elapsedTimeSeconds / 60
                        val secs = uiState.elapsedTimeSeconds % 60
                        val timeFormatted = String.format("%02d:%02d", mins, secs)
                        Text(
                            text = "⏱️ $timeFormatted",
                            color = Color(0xFFD84315),
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Black,
                            modifier = Modifier.testTag("quiz_active_timer")
                        )
                    } else {
                        Text(
                            text = "⏱️ Hidden",
                            color = Color.Gray,
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }

                Spacer(modifier = Modifier.height(6.dp))

                // Stars indicator bar
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceEvenly
                ) {
                    for (i in 1..10) {
                        val isEarned = i <= uiState.currentStars
                        Text(
                            text = if (isEarned) "⭐" else "☆",
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(6.dp))

        // Equation & Answer Input Container
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 8.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            if (showVisualAid) {
                // Practice-mode style visual aid grid (Term1, Operator, Term2)
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(210.dp)
                        .padding(horizontal = 4.dp)
                ) {
                    // Left Column (Term 1)
                    Column(
                        modifier = Modifier
                            .weight(1f)
                            .fillMaxHeight()
                    ) {
                        CyanDisplayBox(
                            text = problem.term1.toString(),
                            isSelected = false,
                            onClick = {},
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(54.dp),
                            testTag = "quiz_term1_display"
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        CyanVisualBox(
                            count = problem.term1,
                            isSelected = false,
                            onClick = { isTerm1Revealed = true },
                            onCharacterTap = onCharacterTapped,
                            isBlocked = !isTerm1Revealed,
                            onBlockClick = { isTerm1Revealed = true },
                            modifier = Modifier
                                .fillMaxWidth()
                                .weight(1f),
                            testTag = "quiz_term1_visual"
                        )
                    }

                    // Middle Column (Operator)
                    Column(
                        modifier = Modifier
                            .width(50.dp)
                            .fillMaxHeight(),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(54.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = problem.operator.symbol,
                                color = AppTextDark,
                                fontSize = 38.sp,
                                fontWeight = FontWeight.Black
                            )
                        }
                        Spacer(modifier = Modifier.height(4.dp))
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .weight(1f),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = problem.operator.symbol,
                                color = AppTextDark,
                                fontSize = 38.sp,
                                fontWeight = FontWeight.Black
                            )
                        }
                    }

                    // Right Column (Term 2)
                    Column(
                        modifier = Modifier
                            .weight(1f)
                            .fillMaxHeight()
                    ) {
                        CyanDisplayBox(
                            text = problem.term2.toString(),
                            isSelected = false,
                            onClick = {},
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(54.dp),
                            testTag = "quiz_term2_display"
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        CyanVisualBox(
                            count = problem.term2,
                            isSelected = false,
                            onClick = { isTerm2Revealed = true },
                            onCharacterTap = onCharacterTapped,
                            isBlocked = !isTerm2Revealed,
                            onBlockClick = { isTerm2Revealed = true },
                            modifier = Modifier
                                .fillMaxWidth()
                                .weight(1f),
                            testTag = "quiz_term2_visual"
                        )
                    }
                }

                Spacer(modifier = Modifier.height(8.dp))

                // Answer Input Row
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(54.dp)
                        .padding(horizontal = 4.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Center
                ) {
                    Text(
                        text = "=",
                        color = AppTextDark,
                        fontSize = 38.sp,
                        fontWeight = FontWeight.Black,
                        modifier = Modifier.padding(end = 10.dp)
                    )

                    // Answer Input Box
                    CyanDisplayBox(
                        text = if (uiState.quizUserAnswer.isEmpty()) "?" else uiState.quizUserAnswer,
                        isSelected = true,
                        onClick = {},
                        modifier = Modifier
                            .height(54.dp)
                            .width(120.dp),
                        testTag = "quiz_answer_input"
                    )
                }

                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = "💡 Tap the brick to show characters",
                    color = AppTextDark,
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Bold,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.testTag("quiz_brick_explanation")
                )
            } else {
                // Compact Row for Level 0 to 100
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Center
                ) {
                    // Term 1
                    CyanDisplayBox(
                        text = problem.term1.toString(),
                        isSelected = false,
                        onClick = {},
                        modifier = Modifier.size(54.dp),
                        testTag = "quiz_term1_compact"
                    )

                    Spacer(modifier = Modifier.width(8.dp))

                    Text(
                        text = problem.operator.symbol,
                        color = AppTextDark,
                        fontSize = 38.sp,
                        fontWeight = FontWeight.Black
                    )

                    Spacer(modifier = Modifier.width(8.dp))

                    // Term 2
                    CyanDisplayBox(
                        text = problem.term2.toString(),
                        isSelected = false,
                        onClick = {},
                        modifier = Modifier.size(54.dp),
                        testTag = "quiz_term2_compact"
                    )

                    Spacer(modifier = Modifier.width(8.dp))

                    Text(
                        text = "=",
                        color = AppTextDark,
                        fontSize = 38.sp,
                        fontWeight = FontWeight.Black
                    )

                    Spacer(modifier = Modifier.width(8.dp))

                    // Answer Input Field
                    CyanDisplayBox(
                        text = if (uiState.quizUserAnswer.isEmpty()) "?" else uiState.quizUserAnswer,
                        isSelected = true,
                        onClick = {},
                        modifier = Modifier
                            .height(54.dp)
                            .width(100.dp),
                        testTag = "quiz_answer_input"
                    )
                }
            }

            // Feedback message banner
            uiState.quizFeedback?.let { feedback ->
                Spacer(modifier = Modifier.height(10.dp))
                Text(
                    text = feedback,
                    color = if (feedback.contains("Correct")) Color(0xFF2E7D32) else Color(0xFFC62828),
                    fontSize = 15.sp,
                    fontWeight = FontWeight.Bold,
                    textAlign = TextAlign.Center
                )
            }
        }
    }
}
