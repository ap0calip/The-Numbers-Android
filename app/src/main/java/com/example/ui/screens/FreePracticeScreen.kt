package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
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
import com.example.ui.viewmodel.MathOperator
import com.example.ui.viewmodel.MathUiState

@Composable
fun FreePracticeScreen(
    uiState: MathUiState,
    onSelectOperator: (MathOperator) -> Unit,
    onSelectLevel: (MathLevel) -> Unit,
    onCharacterTapped: (Int) -> Unit
) {
    val problem = uiState.quizProblem
    val showVisualAid = uiState.level == MathLevel.RANGE_10 || uiState.level == MathLevel.RANGE_12

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .testTag("free_practice_screen"),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Operator & Level Selector Card for Free Practice
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 8.dp, vertical = 2.dp),
            elevation = CardDefaults.cardElevation(defaultElevation = 3.dp),
            colors = CardDefaults.cardColors(containerColor = Color.White),
            shape = RoundedCornerShape(16.dp)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp)
            ) {
                // Operator Selection Row
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "Operator:",
                        color = AppTextDark,
                        fontSize = 13.sp,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.width(68.dp)
                    )
                    Row(
                        modifier = Modifier.weight(1f),
                        horizontalArrangement = Arrangement.spacedBy(6.dp)
                    ) {
                        MathOperator.entries.forEach { op ->
                            val isSelected = uiState.operator == op
                            Box(
                                modifier = Modifier
                                    .weight(1f)
                                    .height(36.dp)
                                    .background(
                                        if (isSelected) AppYellow else Color(0xFFF0F0F0),
                                        RoundedCornerShape(8.dp)
                                    )
                                    .border(
                                        width = if (isSelected) 2.dp else 1.dp,
                                        color = if (isSelected) AppBorderOrange else Color.LightGray,
                                        shape = RoundedCornerShape(8.dp)
                                    )
                                    .clickable { onSelectOperator(op) }
                                    .testTag("free_practice_operator_${op.name.lowercase()}"),
                                contentAlignment = Alignment.Center
                            ) {
                                Text(
                                    text = op.symbol,
                                    color = AppTextDark,
                                    fontSize = 20.sp,
                                    fontWeight = FontWeight.Black
                                )
                            }
                        }
                    }
                }

                Spacer(modifier = Modifier.height(6.dp))

                // Level Selection Row
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "Level:",
                        color = AppTextDark,
                        fontSize = 13.sp,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.width(68.dp)
                    )
                    Row(
                        modifier = Modifier.weight(1f),
                        horizontalArrangement = Arrangement.spacedBy(6.dp)
                    ) {
                        MathLevel.entries.forEach { level ->
                            val isSelected = uiState.level == level
                            Box(
                                modifier = Modifier
                                    .weight(1f)
                                    .height(36.dp)
                                    .background(
                                        if (isSelected) AppYellow else Color(0xFFF0F0F0),
                                        RoundedCornerShape(12.dp)
                                    )
                                    .border(
                                        width = if (isSelected) 2.dp else 1.dp,
                                        color = if (isSelected) AppBorderOrange else Color.LightGray,
                                        shape = RoundedCornerShape(12.dp)
                                    )
                                    .clickable { onSelectLevel(level) }
                                    .testTag("free_practice_level_${level.name.lowercase()}"),
                                contentAlignment = Alignment.Center
                            ) {
                                Text(
                                    text = level.label,
                                    color = AppTextDark,
                                    fontSize = 12.sp,
                                    fontWeight = FontWeight.Bold
                                )
                            }
                        }
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(6.dp))

        // Free Practice Main Problem Container
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
                            testTag = "free_practice_term1_display"
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        CyanVisualBox(
                            count = problem.term1,
                            isSelected = false,
                            onClick = {},
                            onCharacterTap = onCharacterTapped,
                            modifier = Modifier
                                .fillMaxWidth()
                                .weight(1f),
                            testTag = "free_practice_term1_visual"
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
                            testTag = "free_practice_term2_display"
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        CyanVisualBox(
                            count = problem.term2,
                            isSelected = false,
                            onClick = {},
                            onCharacterTap = onCharacterTapped,
                            modifier = Modifier
                                .fillMaxWidth()
                                .weight(1f),
                            testTag = "free_practice_term2_visual"
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

                    CyanDisplayBox(
                        text = if (uiState.quizUserAnswer.isEmpty()) "?" else uiState.quizUserAnswer,
                        isSelected = true,
                        onClick = {},
                        modifier = Modifier
                            .height(54.dp)
                            .width(120.dp),
                        testTag = "free_practice_answer_input"
                    )
                }
            } else {
                // Compact Row for Level 0 to 100
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Center
                ) {
                    CyanDisplayBox(
                        text = problem.term1.toString(),
                        isSelected = false,
                        onClick = {},
                        modifier = Modifier.size(54.dp),
                        testTag = "free_practice_term1_compact"
                    )

                    Spacer(modifier = Modifier.width(8.dp))

                    Text(
                        text = problem.operator.symbol,
                        color = AppTextDark,
                        fontSize = 38.sp,
                        fontWeight = FontWeight.Black
                    )

                    Spacer(modifier = Modifier.width(8.dp))

                    CyanDisplayBox(
                        text = problem.term2.toString(),
                        isSelected = false,
                        onClick = {},
                        modifier = Modifier.size(54.dp),
                        testTag = "free_practice_term2_compact"
                    )

                    Spacer(modifier = Modifier.width(8.dp))

                    Text(
                        text = "=",
                        color = AppTextDark,
                        fontSize = 38.sp,
                        fontWeight = FontWeight.Black
                    )

                    Spacer(modifier = Modifier.width(8.dp))

                    CyanDisplayBox(
                        text = if (uiState.quizUserAnswer.isEmpty()) "?" else uiState.quizUserAnswer,
                        isSelected = true,
                        onClick = {},
                        modifier = Modifier
                            .height(54.dp)
                            .width(100.dp),
                        testTag = "free_practice_answer_input"
                    )
                }
            }

            // Feedback Banner
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
