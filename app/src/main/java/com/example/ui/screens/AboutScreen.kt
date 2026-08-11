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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.audio.AudioHelper
import com.example.ui.components.CyanVisualBox
import com.example.ui.theme.AppTextDark
import com.example.ui.theme.AppYellow

@Composable
fun AboutScreen(
    onBackToPracticeQuiz: () -> Unit
) {
    val context = LocalContext.current
    val audioHelper = remember { AudioHelper(context) }
    DisposableEffect(audioHelper) {
        onDispose {
            audioHelper.shutdown()
        }
    }
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(12.dp)
            .testTag("about_screen_card"),
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
            Text(
                text = "ℹ️ About The Numbers",
                color = AppTextDark,
                fontSize = 22.sp,
                fontWeight = FontWeight.Black,
                textAlign = TextAlign.Center
            )

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = "Designed to help users build a strong foundation, this app focuses exclusively on basic arithmetic.",
                color = Color(0xFF555555),
                fontSize = 14.sp,
                fontWeight = FontWeight.Medium,
                textAlign = TextAlign.Center,
                modifier = Modifier.padding(horizontal = 4.dp)
            )

            Spacer(modifier = Modifier.height(16.dp))

            // SECTION 1: ARITHMETIC FOUNDATION
            AboutFeatureCard(
                emoji = "🧱",
                title = "1. Strong Arithmetic Foundation",
                description = "Focuses on the 4 core math operations: Addition (+), Subtraction (-), Multiplication (×), and Division (÷).",
                visualTitle = "Visual Example:",
                visualContent = "3 + 2 = 5\n🍎🍎🍎 + 🍎🍎 = 🍎🍎🍎🍎🍎"
            )

            Spacer(modifier = Modifier.height(12.dp))

            // SECTION 2: NO NEGATIVES OR FRACTIONS
            AboutFeatureCard(
                emoji = "🌟",
                title = "2. Accessible & Kid-Friendly",
                description = "To keep concepts clear and approachable, it excludes negative numbers and fractions.",
                visualTitle = "Visual Example:",
                visualContent = "Only positive numbers! (1, 2, 3... 10)\n🚫 No negative numbers (-5) or fractions (1/2)."
            )

            Spacer(modifier = Modifier.height(12.dp))

            // SECTION 3: VISUAL AIDS UP TO 12
            AboutFeatureCard(
                emoji = "🎨",
                title = "3. Helpful Visual Aids (Up to 12)",
                description = "Includes interactive visual blocks and counter grids for exercises up to 12 to make learning intuitive.",
                visualTitle = "Visual Example (12 Characters Box):",
                visualContentComposable = {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        CyanVisualBox(
                            count = 12,
                            isSelected = false,
                            onClick = {},
                            onCharacterTap = { count -> audioHelper.speakNumber(count) },
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(110.dp)
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = "💡 Tap characters to hear numbers spoken aloud!",
                            color = AppTextDark,
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Bold,
                            textAlign = TextAlign.Center,
                            modifier = Modifier.fillMaxWidth()
                        )
                    }
                }
            )

            Spacer(modifier = Modifier.height(20.dp))

            Button(
                onClick = onBackToPracticeQuiz,
                colors = ButtonDefaults.buttonColors(containerColor = AppYellow),
                shape = RoundedCornerShape(14.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp)
                    .testTag("about_back_button")
            ) {
                Text(
                    text = "🔙 Back to Practice Quiz",
                    color = AppTextDark,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold
                )
            }
        }
    }
}

@Composable
fun AboutFeatureCard(
    emoji: String,
    title: String,
    description: String,
    visualTitle: String,
    visualContent: String = "",
    visualContentComposable: @Composable (() -> Unit)? = null
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = Color(0xFFF9F9F9)),
        shape = RoundedCornerShape(14.dp),
        border = androidx.compose.foundation.BorderStroke(1.dp, Color(0xFFE8E8E8))
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp)
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Text(text = emoji, fontSize = 20.sp)
                Text(
                    text = title,
                    color = AppTextDark,
                    fontSize = 15.sp,
                    fontWeight = FontWeight.Bold
                )
            }

            Spacer(modifier = Modifier.height(4.dp))

            Text(
                text = description,
                color = Color(0xFF666666),
                fontSize = 13.sp,
                fontWeight = FontWeight.Normal
            )

            Spacer(modifier = Modifier.height(8.dp))

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Color(0xFFFFF8E1), RoundedCornerShape(8.dp))
                    .border(1.dp, Color(0xFFFFE082), RoundedCornerShape(8.dp))
                    .padding(8.dp)
            ) {
                Column {
                    Text(
                        text = visualTitle,
                        color = Color(0xFF8D6E63),
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    if (visualContentComposable != null) {
                        visualContentComposable()
                    } else {
                        Text(
                            text = visualContent,
                            color = AppTextDark,
                            fontSize = 13.sp,
                            fontWeight = FontWeight.SemiBold
                        )
                    }
                }
            }
        }
    }
}
