package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
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
import com.example.ui.theme.AppBorderOrange
import com.example.ui.theme.AppGreen
import com.example.ui.theme.AppTextDark
import com.example.ui.theme.AppYellow
import com.example.ui.viewmodel.QuizResultData

@Composable
fun QuizResultsScreen(
    result: QuizResultData?,
    onPlayAgain: () -> Unit,
    onOpenStickers: () -> Unit,
    onBackToPracticeQuiz: () -> Unit
) {
    if (result == null) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp),
            elevation = CardDefaults.cardElevation(defaultElevation = 8.dp),
            colors = CardDefaults.cardColors(containerColor = Color.White),
            shape = RoundedCornerShape(20.dp)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(20.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = "📊 Last Results",
                    color = AppTextDark,
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Black,
                    textAlign = TextAlign.Center
                )

                Spacer(modifier = Modifier.height(16.dp))

                Text(
                    text = "🎯 No Quiz Completed Yet!",
                    color = AppBorderOrange,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    textAlign = TextAlign.Center
                )

                Spacer(modifier = Modifier.height(8.dp))

                Text(
                    text = "Take a Practice Quiz to earn stars, win cool stickers, and view your performance results here!",
                    color = Color.DarkGray,
                    fontSize = 14.sp,
                    textAlign = TextAlign.Center
                )

                Spacer(modifier = Modifier.height(20.dp))

                Button(
                    onClick = onPlayAgain,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(48.dp)
                        .testTag("start_first_quiz_button"),
                    colors = ButtonDefaults.buttonColors(containerColor = AppGreen),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Text("Start Practice Quiz ⭐", color = Color.White, fontWeight = FontWeight.Bold, fontSize = 15.sp)
                }

                Spacer(modifier = Modifier.height(10.dp))

                Button(
                    onClick = onBackToPracticeQuiz,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(40.dp)
                        .testTag("quiz_empty_results_back_quiz"),
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFECECEC)),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Text("Back to Practice Quiz 🎯", color = AppTextDark, fontWeight = FontWeight.Bold, fontSize = 13.sp)
                }
            }
        }
        return
    }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(12.dp),
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
            val dateTimeFormatted = androidx.compose.runtime.remember(result.completedTimestamp) {
                val sdf = java.text.SimpleDateFormat("EEE, MMM d, yyyy • h:mm a", java.util.Locale.getDefault())
                sdf.format(java.util.Date(result.completedTimestamp))
            }

            Text(
                text = "🎉 Quiz Complete!",
                color = AppTextDark,
                fontSize = 24.sp,
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

            Spacer(modifier = Modifier.height(10.dp))

            // Sticker Won Banner
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(containerColor = Color(0xFFFFF8E1)),
                shape = RoundedCornerShape(16.dp),
                elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(12.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Box(
                        modifier = Modifier
                            .size(60.dp)
                            .background(AppYellow, RoundedCornerShape(12.dp))
                            .border(2.dp, AppBorderOrange, RoundedCornerShape(12.dp)),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = result.stickerWon.emoji,
                            fontSize = 36.sp
                        )
                    }

                    Spacer(modifier = Modifier.padding(horizontal = 8.dp))

                    Column {
                        Text(
                            text = "🎁 You Won a Sticker!",
                            color = AppBorderOrange,
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Bold
                        )
                        Text(
                            text = result.stickerWon.name,
                            color = AppTextDark,
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Black
                        )
                        Text(
                            text = result.stickerWon.description,
                            color = Color.Gray,
                            fontSize = 12.sp
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(14.dp))

            // Quiz Results Breakdown Table (1-6 required items)
            Text(
                text = "Quiz Summary Results:",
                color = AppTextDark,
                fontSize = 15.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(8.dp))

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Color(0xFFF8F9FA), RoundedCornerShape(12.dp))
                    .padding(12.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                // 1. Operator
                ResultRow(label = "1. Operator", value = "${result.operator.symbol} (${result.operator.label})")

                // 2. Level
                ResultRow(label = "2. Level", value = result.level.label)

                // 3. Total Star Gain
                ResultRow(label = "3. Total Stars Gained", value = "${result.totalStarsGained} ⭐")

                // 4. Total Star Lost
                ResultRow(label = "4. Total Stars Lost", value = "${result.totalStarsLost} ❌")

                // 5. Percentage of Success
                val pctFormatted = String.format("%.1f%%", result.successPercentage)
                ResultRow(label = "5. Success Percentage", value = pctFormatted)

                // 6. Total Time
                val mins = result.totalTimeSeconds / 60
                val secs = result.totalTimeSeconds % 60
                val timeStr = String.format("%02d:%02d (%ds)", mins, secs, result.totalTimeSeconds)
                ResultRow(label = "6. Total Time", value = timeStr)

                // 7. Completed Date & Time
                ResultRow(label = "7. Completed On", value = dateTimeFormatted)

                if (result.isNewBestTime) {
                    Spacer(modifier = Modifier.height(4.dp))
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .background(Color(0xFFE8F5E9), RoundedCornerShape(8.dp))
                            .padding(8.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = "🏆 NEW BEST TIME RECORD!",
                            color = Color(0xFF2E7D32),
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Black
                        )
                    }
                } else if (result.previousBestTimeSeconds != null) {
                    Text(
                        text = "Best record: ${result.previousBestTimeSeconds}s",
                        color = Color.Gray,
                        fontSize = 12.sp,
                        textAlign = TextAlign.End,
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Action Buttons
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Button(
                    onClick = onPlayAgain,
                    modifier = Modifier
                        .weight(1f)
                        .height(48.dp)
                        .testTag("quiz_results_play_again"),
                    colors = ButtonDefaults.buttonColors(containerColor = AppGreen),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Text("Play Again 🔄", color = AppTextDark, fontWeight = FontWeight.Bold, fontSize = 14.sp)
                }

                Button(
                    onClick = onOpenStickers,
                    modifier = Modifier
                        .weight(1f)
                        .height(48.dp)
                        .testTag("quiz_results_view_stickers"),
                    colors = ButtonDefaults.buttonColors(containerColor = AppYellow),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Text("Stickers 🎨", color = AppTextDark, fontWeight = FontWeight.Bold, fontSize = 14.sp)
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            Button(
                onClick = onBackToPracticeQuiz,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(40.dp)
                    .testTag("quiz_results_back_quiz"),
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFECECEC)),
                shape = RoundedCornerShape(12.dp)
            ) {
                Text("Back to Practice Quiz 🎯", color = AppTextDark, fontWeight = FontWeight.Bold, fontSize = 13.sp)
            }
        }
    }
}

@Composable
private fun ResultRow(label: String, value: String) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = label,
            color = Color.DarkGray,
            fontSize = 13.sp,
            fontWeight = FontWeight.Medium
        )
        Text(
            text = value,
            color = AppTextDark,
            fontSize = 14.sp,
            fontWeight = FontWeight.Bold
        )
    }
}
