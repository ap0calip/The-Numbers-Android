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
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedButton
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
import androidx.compose.ui.window.Dialog
import com.example.data.QuizBestTimeEntity
import com.example.ui.theme.AppBorderOrange
import com.example.ui.theme.AppGreen
import com.example.ui.theme.AppTextDark
import com.example.ui.theme.AppYellow
import com.example.ui.viewmodel.MathLevel
import com.example.ui.viewmodel.MathOperator
import com.example.ui.viewmodel.QuizResultData
import com.example.ui.viewmodel.StickerItem

@Composable
fun QuizBestResultsScreen(
    bestResultsList: List<QuizBestTimeEntity>,
    onResetAll: () -> Unit,
    onStartCombination: (MathOperator, MathLevel) -> Unit,
    onBackToPracticeQuiz: () -> Unit
) {
    var showResetDialog by remember { mutableStateOf(false) }
    var selectedResultForModal by remember { mutableStateOf<QuizResultData?>(null) }

    val operators = MathOperator.values()
    val levels = MathLevel.values()

    // Reset Confirmation Dialog
    if (showResetDialog) {
        AlertDialog(
            onDismissRequest = { showResetDialog = false },
            title = {
                Text(
                    text = "Reset All Best Results?",
                    fontWeight = FontWeight.Bold,
                    color = AppTextDark,
                    fontSize = 18.sp
                )
            },
            text = {
                Text(
                    text = "Are you sure you want to clear all your saved best quiz records? This action cannot be undone.",
                    color = Color.DarkGray,
                    fontSize = 14.sp
                )
            },
            confirmButton = {
                Button(
                    onClick = {
                        onResetAll()
                        showResetDialog = false
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFD32F2F)),
                    shape = RoundedCornerShape(10.dp),
                    modifier = Modifier.testTag("confirm_reset_button")
                ) {
                    Text("Reset All", color = Color.White, fontWeight = FontWeight.Bold)
                }
            },
            dismissButton = {
                Button(
                    onClick = { showResetDialog = false },
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFECECEC)),
                    shape = RoundedCornerShape(10.dp),
                    modifier = Modifier.testTag("cancel_reset_button")
                ) {
                    Text("Cancel", color = AppTextDark, fontWeight = FontWeight.Bold)
                }
            },
            shape = RoundedCornerShape(16.dp),
            containerColor = Color.White
        )
    }

    // Modal Dialog to display "Quiz Complete!" card for a clicked best result
    selectedResultForModal?.let { result ->
        Dialog(onDismissRequest = { selectedResultForModal = null }) {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp)
                    .testTag("quiz_complete_modal_card"),
                elevation = CardDefaults.cardElevation(defaultElevation = 10.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                shape = RoundedCornerShape(20.dp)
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    val dateTimeFormatted = androidx.compose.runtime.remember(result.completedTimestamp) {
                        val sdf = java.text.SimpleDateFormat("EEE, MMM d, yyyy • h:mm a", java.util.Locale.getDefault())
                        sdf.format(java.util.Date(result.completedTimestamp))
                    }

                    Text(
                        text = "🎉 Quiz Complete!",
                        color = AppTextDark,
                        fontSize = 22.sp,
                        fontWeight = FontWeight.Black,
                        textAlign = TextAlign.Center
                    )

                    Spacer(modifier = Modifier.height(2.dp))

                    Text(
                        text = "📅 $dateTimeFormatted",
                        color = Color.Gray,
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Medium,
                        textAlign = TextAlign.Center
                    )

                    Spacer(modifier = Modifier.height(4.dp))

                    Text(
                        text = "🏆 Best Record for ${result.operator.label} (${result.level.label})",
                        color = AppBorderOrange,
                        fontSize = 13.sp,
                        fontWeight = FontWeight.Bold,
                        textAlign = TextAlign.Center
                    )

                    Spacer(modifier = Modifier.height(12.dp))

                    // Sticker Won Banner
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        colors = CardDefaults.cardColors(containerColor = Color(0xFFFFF8E1)),
                        shape = RoundedCornerShape(14.dp),
                        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(10.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Box(
                                modifier = Modifier
                                    .size(52.dp)
                                    .background(AppYellow, RoundedCornerShape(12.dp))
                                    .border(2.dp, AppBorderOrange, RoundedCornerShape(12.dp)),
                                contentAlignment = Alignment.Center
                            ) {
                                Text(
                                    text = result.stickerWon.emoji,
                                    fontSize = 32.sp
                                )
                            }

                            Spacer(modifier = Modifier.width(10.dp))

                            Column {
                                Text(
                                    text = "🎁 Won Sticker",
                                    color = AppBorderOrange,
                                    fontSize = 11.sp,
                                    fontWeight = FontWeight.Bold
                                )
                                Text(
                                    text = result.stickerWon.name,
                                    color = AppTextDark,
                                    fontSize = 16.sp,
                                    fontWeight = FontWeight.Black
                                )
                                Text(
                                    text = result.stickerWon.description,
                                    color = Color.Gray,
                                    fontSize = 11.sp
                                )
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(12.dp))

                    // Summary Results Breakdown
                    Text(
                        text = "Quiz Summary Results:",
                        color = AppTextDark,
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.fillMaxWidth()
                    )

                    Spacer(modifier = Modifier.height(6.dp))

                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .background(Color(0xFFF8F9FA), RoundedCornerShape(12.dp))
                            .padding(10.dp),
                        verticalArrangement = Arrangement.spacedBy(6.dp)
                    ) {
                        ResultDetailRow("1. Operator", "${result.operator.symbol} (${result.operator.label})")
                        ResultDetailRow("2. Level", result.level.label)
                        ResultDetailRow("3. Total Stars Gained", "${result.totalStarsGained} ⭐")
                        ResultDetailRow("4. Total Stars Lost", "${result.totalStarsLost} ❌")
                        val pctFormatted = String.format("%.1f%%", result.successPercentage)
                        ResultDetailRow("5. Success Percentage", pctFormatted)
                        val mins = result.totalTimeSeconds / 60
                        val secs = result.totalTimeSeconds % 60
                        val timeStr = String.format("%02d:%02d (%ds)", mins, secs, result.totalTimeSeconds)
                        ResultDetailRow("6. Total Time", timeStr)
                        ResultDetailRow("7. Completed On", dateTimeFormatted)
                    }

                    Spacer(modifier = Modifier.height(14.dp))

                    // Dialog Actions
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Button(
                            onClick = {
                                val op = result.operator
                                val lvl = result.level
                                selectedResultForModal = null
                                onStartCombination(op, lvl)
                            },
                            modifier = Modifier
                                .weight(1f)
                                .height(44.dp)
                                .testTag("modal_practice_again_button"),
                            colors = ButtonDefaults.buttonColors(containerColor = AppGreen),
                            shape = RoundedCornerShape(10.dp)
                        ) {
                            Text("Practice Again 🎯", color = Color.White, fontWeight = FontWeight.Bold, fontSize = 13.sp)
                        }

                        Button(
                            onClick = { selectedResultForModal = null },
                            modifier = Modifier
                                .weight(1f)
                                .height(44.dp)
                                .testTag("modal_close_button"),
                            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFECECEC)),
                            shape = RoundedCornerShape(10.dp)
                        ) {
                            Text("Close ✖️", color = AppTextDark, fontWeight = FontWeight.Bold, fontSize = 13.sp)
                        }
                    }
                }
            }
        }
    }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(12.dp)
            .testTag("best_results_main_card"),
        elevation = CardDefaults.cardElevation(defaultElevation = 8.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        shape = RoundedCornerShape(20.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Header Row with Title and Reset Button
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = "🏆 Best Quiz Results",
                        color = AppTextDark,
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Black
                    )
                    Text(
                        text = "Best records for every combination",
                        color = AppBorderOrange,
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold
                    )
                }

                Button(
                    onClick = { showResetDialog = true },
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFFFEBEE)),
                    shape = RoundedCornerShape(10.dp),
                    modifier = Modifier.testTag("reset_all_results_button")
                ) {
                    Icon(
                        imageVector = Icons.Default.Delete,
                        contentDescription = "Reset All Results",
                        tint = Color(0xFFD32F2F),
                        modifier = Modifier.size(18.dp)
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        text = "Reset",
                        color = Color(0xFFD32F2F),
                        fontWeight = FontWeight.Bold,
                        fontSize = 13.sp
                    )
                }
            }

            Spacer(modifier = Modifier.height(14.dp))

            // 12 Combinations List Grouped by Operator
            Column(
                modifier = Modifier
                    .fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                operators.forEach { op ->
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        colors = CardDefaults.cardColors(containerColor = Color(0xFFFFFDE7)),
                        shape = RoundedCornerShape(14.dp),
                        border = androidx.compose.foundation.BorderStroke(1.dp, AppYellow)
                    ) {
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(12.dp)
                        ) {
                            Text(
                                text = "${op.symbol} ${op.label}",
                                color = AppTextDark,
                                fontSize = 16.sp,
                                fontWeight = FontWeight.Black
                            )

                            Spacer(modifier = Modifier.height(8.dp))

                            levels.forEach { lvl ->
                                val key = "${op.name}_${lvl.name}"
                                val bestEntity = bestResultsList.find { it.key == key || (it.operator == op.name && it.level == lvl.name) }

                                val resultData = bestEntity?.let { entity ->
                                    QuizResultData(
                                        operator = op,
                                        level = lvl,
                                        totalStarsGained = entity.totalStarsGained,
                                        totalStarsLost = entity.totalStarsLost,
                                        successPercentage = entity.successPercentage,
                                        totalTimeSeconds = entity.bestTimeSeconds,
                                        isNewBestTime = false,
                                        previousBestTimeSeconds = null,
                                        stickerWon = StickerItem(entity.stickerName, entity.stickerEmoji, entity.stickerDescription),
                                        completedTimestamp = entity.timestamp
                                    )
                                }

                                Card(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(vertical = 4.dp)
                                        .clickable {
                                            if (resultData != null) {
                                                selectedResultForModal = resultData
                                            } else {
                                                onStartCombination(op, lvl)
                                            }
                                        }
                                        .testTag("best_result_item_${op.name}_${lvl.name}"),
                                    colors = CardDefaults.cardColors(
                                        containerColor = if (resultData != null) Color(0xFFE8F5E9) else Color.White
                                    ),
                                    shape = RoundedCornerShape(10.dp),
                                    border = androidx.compose.foundation.BorderStroke(
                                        1.dp,
                                        if (resultData != null) AppGreen else Color(0xFFE0E0E0)
                                    )
                                ) {
                                    Row(
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .padding(horizontal = 12.dp, vertical = 10.dp),
                                        verticalAlignment = Alignment.CenterVertically,
                                        horizontalArrangement = Arrangement.SpaceBetween
                                    ) {
                                        Column(modifier = Modifier.weight(1f)) {
                                            Text(
                                                text = "Level: ${lvl.label}",
                                                color = AppTextDark,
                                                fontSize = 14.sp,
                                                fontWeight = FontWeight.Bold
                                            )

                                            if (resultData != null) {
                                                Text(
                                                    text = "⚡ ${resultData.totalTimeSeconds}s  •  ⭐ ${resultData.totalStarsGained} Stars  •  🎯 ${String.format("%.0f%%", resultData.successPercentage)}",
                                                    color = Color(0xFF2E7D32),
                                                    fontSize = 12.sp,
                                                    fontWeight = FontWeight.Medium
                                                )
                                                Text(
                                                    text = "Tap to view Quiz Complete! 🔍",
                                                    color = AppBorderOrange,
                                                    fontSize = 11.sp,
                                                    fontWeight = FontWeight.Bold
                                                )
                                            } else {
                                                Text(
                                                    text = "No record yet — Tap to practice! 🎯",
                                                    color = Color.Gray,
                                                    fontSize = 12.sp
                                                )
                                            }
                                        }

                                        if (resultData != null) {
                                            Text(
                                                text = resultData.stickerWon.emoji,
                                                fontSize = 24.sp
                                            )
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(14.dp))

            Button(
                onClick = onBackToPracticeQuiz,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(42.dp)
                    .testTag("best_results_back_quiz"),
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFECECEC)),
                shape = RoundedCornerShape(10.dp)
            ) {
                Text("Back to Practice Quiz 🎯", color = AppTextDark, fontWeight = FontWeight.Bold, fontSize = 13.sp)
            }
        }
    }
}

@Composable
private fun ResultDetailRow(label: String, value: String) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = label,
            color = Color.DarkGray,
            fontSize = 12.sp,
            fontWeight = FontWeight.Medium
        )
        Text(
            text = value,
            color = AppTextDark,
            fontSize = 13.sp,
            fontWeight = FontWeight.Bold
        )
    }
}
