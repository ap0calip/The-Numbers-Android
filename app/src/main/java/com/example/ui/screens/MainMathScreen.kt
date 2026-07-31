package com.example.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.School
import androidx.compose.material.icons.filled.VolumeMute
import androidx.compose.material.icons.filled.VolumeUp
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.NavigationDrawerItem
import androidx.compose.material3.NavigationDrawerItemDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import com.example.R
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.components.CharacterGrid
import com.example.ui.components.CyanDisplayBox
import com.example.ui.components.CyanVisualBox
import com.example.ui.components.NumberPad
import com.example.ui.theme.AppBorderOrange
import com.example.ui.theme.AppCyan
import com.example.ui.theme.AppCyanFocused
import com.example.ui.theme.AppGreen
import com.example.ui.theme.AppOrange
import com.example.ui.theme.AppTextDark
import com.example.ui.theme.AppYellow
import com.example.ui.viewmodel.AppMode
import com.example.ui.viewmodel.MathViewModel
import com.example.ui.viewmodel.QuizScreenState
import com.example.ui.viewmodel.SelectedField
import kotlinx.coroutines.launch

@Composable
fun MainMathScreen(
    viewModel: MathViewModel,
    modifier: Modifier = Modifier
) {
    val uiState by viewModel.uiState.collectAsState()
    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val scope = rememberCoroutineScope()

    ModalNavigationDrawer(
        drawerState = drawerState,
        drawerContent = {
            ModalDrawerSheet(
                drawerContainerColor = Color.White,
                modifier = Modifier.width(280.dp)
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(16.dp)
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.padding(bottom = 12.dp, top = 8.dp)
                    ) {
                        Text(
                            text = "🧮",
                            fontSize = 28.sp,
                            modifier = Modifier.padding(end = 8.dp)
                        )
                        Column {
                            Text(
                                text = "The Numbers",
                                color = AppTextDark,
                                fontSize = 20.sp,
                                fontWeight = FontWeight.Black
                            )
                            Text(
                                text = "Navigation Menu",
                                color = Color.Gray,
                                fontSize = 12.sp,
                                fontWeight = FontWeight.Medium
                            )
                        }
                    }

                    HorizontalDivider(modifier = Modifier.padding(vertical = 8.dp), color = Color(0xFFE0E0E0))

                    // SECTION 1: MODE
                    Text(
                        text = "MODE",
                        color = AppBorderOrange,
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.padding(start = 12.dp, top = 8.dp, bottom = 4.dp)
                    )

                    val isCalcSelected = uiState.mode == AppMode.CALCULATOR
                    NavigationDrawerItem(
                        label = { Text("🧮 Calculator", fontWeight = if (isCalcSelected) FontWeight.Bold else FontWeight.Medium, color = Color(0xFF222222)) },
                        selected = isCalcSelected,
                        onClick = {
                            viewModel.setAppMode(AppMode.CALCULATOR)
                            scope.launch { drawerState.close() }
                        },
                        colors = NavigationDrawerItemDefaults.colors(
                            selectedContainerColor = AppYellow,
                            selectedTextColor = AppTextDark,
                            unselectedTextColor = Color(0xFF222222)
                        ),
                        modifier = Modifier.padding(vertical = 2.dp).testTag("menu_mode_calculator")
                    )

                    val isFreePracticeSelected = uiState.mode == AppMode.FREE_PRACTICE
                    NavigationDrawerItem(
                        label = { Text("✏️ Free Practice", fontWeight = if (isFreePracticeSelected) FontWeight.Bold else FontWeight.Medium, color = Color(0xFF222222)) },
                        selected = isFreePracticeSelected,
                        onClick = {
                            viewModel.setAppMode(AppMode.FREE_PRACTICE)
                            scope.launch { drawerState.close() }
                        },
                        colors = NavigationDrawerItemDefaults.colors(
                            selectedContainerColor = AppYellow,
                            selectedTextColor = AppTextDark,
                            unselectedTextColor = Color(0xFF222222)
                        ),
                        modifier = Modifier.padding(vertical = 2.dp).testTag("menu_mode_free_practice")
                    )

                    val isPracticeQuizSelected = uiState.mode == AppMode.PRACTICE_QUIZ &&
                        uiState.quizScreenState != QuizScreenState.STICKER_ALBUM &&
                        uiState.quizScreenState != QuizScreenState.RESULTS
                    NavigationDrawerItem(
                        label = { Text("🎯 Practice Quiz", fontWeight = if (isPracticeQuizSelected) FontWeight.Bold else FontWeight.Medium, color = Color(0xFF222222)) },
                        selected = isPracticeQuizSelected,
                        onClick = {
                            viewModel.setAppMode(AppMode.PRACTICE_QUIZ)
                            scope.launch { drawerState.close() }
                        },
                        colors = NavigationDrawerItemDefaults.colors(
                            selectedContainerColor = AppYellow,
                            selectedTextColor = AppTextDark,
                            unselectedTextColor = Color(0xFF222222)
                        ),
                        modifier = Modifier.padding(vertical = 2.dp).testTag("menu_mode_practice_quiz")
                    )

                    HorizontalDivider(modifier = Modifier.padding(vertical = 12.dp), color = Color(0xFFE0E0E0))

                    // SECTION 2: MY STICKER ALBUM & QUIZ RESULTS
                    val isStickerSelected = uiState.mode == AppMode.PRACTICE_QUIZ && uiState.quizScreenState == QuizScreenState.STICKER_ALBUM
                    NavigationDrawerItem(
                        label = { Text("🎨 My Sticker Album", fontWeight = if (isStickerSelected) FontWeight.Bold else FontWeight.Medium, color = Color(0xFF222222)) },
                        selected = isStickerSelected,
                        onClick = {
                            viewModel.openStickerAlbumFromMenu()
                            scope.launch { drawerState.close() }
                        },
                        colors = NavigationDrawerItemDefaults.colors(
                            selectedContainerColor = AppYellow,
                            selectedTextColor = AppTextDark,
                            unselectedTextColor = Color(0xFF222222)
                        ),
                        modifier = Modifier.padding(vertical = 2.dp).testTag("menu_sticker_album")
                    )

                    val isResultsSelected = uiState.mode == AppMode.PRACTICE_QUIZ && uiState.quizScreenState == QuizScreenState.RESULTS
                    NavigationDrawerItem(
                        label = { Text("📊 Last Results", fontWeight = if (isResultsSelected) FontWeight.Bold else FontWeight.Medium, color = Color(0xFF222222)) },
                        selected = isResultsSelected,
                        onClick = {
                            viewModel.openQuizResultsFromMenu()
                            scope.launch { drawerState.close() }
                        },
                        colors = NavigationDrawerItemDefaults.colors(
                            selectedContainerColor = AppYellow,
                            selectedTextColor = AppTextDark,
                            unselectedTextColor = Color(0xFF222222)
                        ),
                        modifier = Modifier.padding(vertical = 2.dp).testTag("menu_quiz_results")
                    )

                    val isBestResultsSelected = uiState.mode == AppMode.PRACTICE_QUIZ && uiState.quizScreenState == QuizScreenState.BEST_RESULTS
                    NavigationDrawerItem(
                        label = { Text("🏆 Best Results", fontWeight = if (isBestResultsSelected) FontWeight.Bold else FontWeight.Medium, color = Color(0xFF222222)) },
                        selected = isBestResultsSelected,
                        onClick = {
                            viewModel.openBestResultsFromMenu()
                            scope.launch { drawerState.close() }
                        },
                        colors = NavigationDrawerItemDefaults.colors(
                            selectedContainerColor = AppYellow,
                            selectedTextColor = AppTextDark,
                            unselectedTextColor = Color(0xFF222222)
                        ),
                        modifier = Modifier.padding(vertical = 2.dp).testTag("menu_best_results")
                    )

                    Spacer(modifier = Modifier.weight(1f))
                    Spacer(modifier = Modifier.height(16.dp))

                    Image(
                        painter = painterResource(id = R.drawable.logo2_con_borde),
                        contentDescription = "App Logo",
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 8.dp)
                            .testTag("menu_logo_image"),
                        contentScale = ContentScale.Fit
                    )
                }
            }
        }
    ) {
        Surface(
            modifier = modifier
                .fillMaxSize()
                .testTag("main_math_screen"),
            color = AppOrange
        ) {
            BoxWithConstraints(modifier = Modifier.fillMaxSize()) {
                val maxHeight = maxHeight

                // Scrollable wrapper for compact screens, or flex box
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(AppOrange),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .weight(1f)
                            .verticalScroll(rememberScrollState()),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                    // Top Action Bar: Menu Toggle, Mode Title Badge & Mute Toggle
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .background(Color(0xFFD97700))
                            .padding(horizontal = 12.dp, vertical = 4.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        // Hamburger Menu Icon
                        IconButton(
                            onClick = { scope.launch { drawerState.open() } },
                            modifier = Modifier
                                .size(36.dp)
                                .testTag("navigation_drawer_toggle")
                        ) {
                            Icon(
                                imageVector = Icons.Default.Menu,
                                contentDescription = "Open Navigation Menu",
                                tint = Color.White
                            )
                        }

                        // Mode Title Badge
                        val modeTitle = when (uiState.mode) {
                            AppMode.CALCULATOR -> "🧮 Calculator"
                            AppMode.FREE_PRACTICE -> "✏️ Free Practice"
                            AppMode.PRACTICE_QUIZ -> when (uiState.quizScreenState) {
                                QuizScreenState.SETUP -> "🎯 Practice Quiz"
                                QuizScreenState.COUNTDOWN -> "🎯 Practice Quiz"
                                QuizScreenState.ACTIVE -> "⭐ Quiz"
                                QuizScreenState.RESULTS -> "📊 Last Results"
                                QuizScreenState.STICKER_ALBUM -> "🎨 Sticker Album"
                                QuizScreenState.BEST_RESULTS -> "🏆 Best Results"
                            }
                        }

                        Row(
                            modifier = Modifier
                                .background(AppYellow, RoundedCornerShape(16.dp))
                                .clickable { scope.launch { drawerState.open() } }
                                .padding(horizontal = 12.dp, vertical = 5.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = modeTitle,
                                color = AppTextDark,
                                fontSize = 14.sp,
                                fontWeight = FontWeight.Bold
                            )
                        }

                        // Sound Toggle
                        IconButton(
                            onClick = { viewModel.toggleSound() },
                            modifier = Modifier.size(36.dp)
                        ) {
                            Icon(
                                imageVector = if (uiState.soundEnabled) Icons.Default.VolumeUp else Icons.Default.VolumeMute,
                                contentDescription = "Sound Toggle",
                                tint = Color.White
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(6.dp))

                when (uiState.mode) {
                    AppMode.CALCULATOR -> {
                    // Operator Selector Menu Card for Calculator
                    androidx.compose.material3.Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 8.dp, vertical = 2.dp),
                        elevation = androidx.compose.material3.CardDefaults.cardElevation(defaultElevation = 3.dp),
                        colors = androidx.compose.material3.CardDefaults.cardColors(containerColor = Color.White),
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
                                    fontSize = 14.sp,
                                    fontWeight = FontWeight.Bold,
                                    modifier = Modifier.width(70.dp)
                                )
                                Row(
                                    modifier = Modifier.weight(1f),
                                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                                ) {
                                    com.example.ui.viewmodel.MathOperator.entries.forEach { op ->
                                        val isSelected = uiState.operator == op
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
                                                .clickable { viewModel.selectOperator(op) }
                                                .testTag("operator_${op.name.lowercase()}"),
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
                        }
                    }

                    Spacer(modifier = Modifier.height(6.dp))

                    // 2. Main Calculator Layout (3 Columns: Term1, Operator, Term2)
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
                            // Top Number Box (Term 1)
                            CyanDisplayBox(
                                text = uiState.term1,
                                isSelected = uiState.activeField == SelectedField.TERM1,
                                onClick = { viewModel.selectField(SelectedField.TERM1) },
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(54.dp),
                                testTag = "term1_display"
                            )

                            Spacer(modifier = Modifier.height(4.dp))

                            // Bottom Visual Grid Box (Term 1)
                            val term1Val = uiState.term1.toIntOrNull() ?: 0
                            if (term1Val > 15) {
                                Box(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .weight(1f)
                                        .background(Color(0xFFF8F9FA), RoundedCornerShape(16.dp))
                                        .border(1.dp, Color.LightGray, RoundedCornerShape(16.dp))
                                        .padding(8.dp),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Text(
                                        text = "There is no visual aid for numbers larger than 15.",
                                        color = Color.DarkGray,
                                        fontSize = 12.sp,
                                        textAlign = TextAlign.Center,
                                        fontWeight = FontWeight.Medium
                                    )
                                }
                            } else {
                                CyanVisualBox(
                                    count = term1Val,
                                    isSelected = uiState.activeField == SelectedField.TERM1,
                                    onClick = { viewModel.selectField(SelectedField.TERM1) },
                                    onCharacterTap = { count -> viewModel.onCharacterTapped(count) },
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .weight(1f),
                                    testTag = "term1_visual"
                                )
                            }
                        }

                        // Middle Operator Column
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
                                    text = uiState.operator.symbol,
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
                                    text = uiState.operator.symbol,
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
                            // Top Number Box (Term 2)
                            CyanDisplayBox(
                                text = uiState.term2,
                                isSelected = uiState.activeField == SelectedField.TERM2,
                                onClick = { viewModel.selectField(SelectedField.TERM2) },
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(54.dp),
                                testTag = "term2_display"
                            )

                            Spacer(modifier = Modifier.height(4.dp))

                            // Bottom Visual Grid Box (Term 2)
                            val term2Val = uiState.term2.toIntOrNull() ?: 0
                            if (term2Val > 15) {
                                Box(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .weight(1f)
                                        .background(Color(0xFFF8F9FA), RoundedCornerShape(16.dp))
                                        .border(1.dp, Color.LightGray, RoundedCornerShape(16.dp))
                                        .padding(8.dp),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Text(
                                        text = "There is no visual aid for numbers larger than 15.",
                                        color = Color.DarkGray,
                                        fontSize = 12.sp,
                                        textAlign = TextAlign.Center,
                                        fontWeight = FontWeight.Medium
                                    )
                                }
                            } else {
                                CyanVisualBox(
                                    count = term2Val,
                                    isSelected = uiState.activeField == SelectedField.TERM2,
                                    onClick = { viewModel.selectField(SelectedField.TERM2) },
                                    onCharacterTap = { count -> viewModel.onCharacterTapped(count) },
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .weight(1f),
                                    testTag = "term2_visual"
                                )
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(6.dp))

                    // 3. Equals Rows (Row A: = [Result], Row B: = [ResultVisual])
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(54.dp)
                            .padding(horizontal = 4.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Box(
                            modifier = Modifier.width(50.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = "=",
                                color = AppTextDark,
                                fontSize = 38.sp,
                                fontWeight = FontWeight.Black
                            )
                        }

                        // Answer Box / Calculated Sum
                        CyanDisplayBox(
                            text = if (uiState.isEntered) uiState.calculatedSum.toString() else "",
                            isSelected = false,
                            onClick = {},
                            modifier = Modifier
                                .weight(1f)
                                .fillMaxHeight(),
                            testTag = "calculated_sum_display"
                        )
                    }

                    Spacer(modifier = Modifier.height(6.dp))

                    // Row B (= [ResultVisual])
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(140.dp)
                            .padding(horizontal = 4.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Box(
                            modifier = Modifier.width(50.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = "=",
                                color = AppTextDark,
                                fontSize = 38.sp,
                                fontWeight = FontWeight.Black
                            )
                        }

                        // Answer Visual Grid Box
                        val sumVal = if (uiState.isEntered) uiState.calculatedSum else 0
                        if (uiState.isEntered && sumVal > 15) {
                            Box(
                                modifier = Modifier
                                    .weight(1f)
                                    .fillMaxHeight()
                                    .background(Color(0xFFF8F9FA), RoundedCornerShape(16.dp))
                                    .border(1.dp, Color.LightGray, RoundedCornerShape(16.dp))
                                    .padding(8.dp),
                                contentAlignment = Alignment.Center
                            ) {
                                Text(
                                    text = "There is no visual aid for numbers larger than 15.",
                                    color = Color.DarkGray,
                                    fontSize = 12.sp,
                                    textAlign = TextAlign.Center,
                                    fontWeight = FontWeight.Medium
                                )
                            }
                        } else {
                            CyanVisualBox(
                                count = sumVal,
                                isSelected = false,
                                onClick = {},
                                onCharacterTap = { count -> viewModel.onCharacterTapped(count) },
                                modifier = Modifier
                                    .weight(1f)
                                    .fillMaxHeight(),
                                testTag = "calculated_sum_visual"
                            )
                        }
                    }
                }
                AppMode.FREE_PRACTICE -> {
                        FreePracticeScreen(
                            uiState = uiState,
                            onSelectOperator = { viewModel.selectOperator(it) },
                            onSelectLevel = { viewModel.selectLevel(it) },
                            onCharacterTapped = { viewModel.onCharacterTapped(it) }
                        )
                    }
                    AppMode.PRACTICE_QUIZ -> {
                        // Practice Quiz Mode Router
                        when (uiState.quizScreenState) {
                            com.example.ui.viewmodel.QuizScreenState.SETUP -> {
                                QuizSetupScreen(
                                    uiState = uiState,
                                    onSelectOperator = { viewModel.selectOperator(it) },
                                    onSelectLevel = { viewModel.selectLevel(it) },
                                    onToggleShowTimer = { viewModel.setShowTimerDuringTest(it) },
                                    onStartQuiz = { viewModel.startQuizCountdown() },
                                    onOpenStickers = { viewModel.openStickerAlbum() }
                                )
                            }
                            com.example.ui.viewmodel.QuizScreenState.COUNTDOWN -> {
                                QuizCountdownScreen(
                                    countdownValue = uiState.countdownValue
                                )
                            }
                            com.example.ui.viewmodel.QuizScreenState.ACTIVE -> {
                                QuizActiveScreen(
                                    uiState = uiState,
                                    onCharacterTapped = { viewModel.onCharacterTapped(it) }
                                )
                            }
                            com.example.ui.viewmodel.QuizScreenState.RESULTS -> {
                                QuizResultsScreen(
                                    result = uiState.lastQuizResult,
                                    onPlayAgain = { viewModel.startQuizCountdown() },
                                    onOpenStickers = { viewModel.openStickerAlbum() },
                                    onBackToPracticeQuiz = { viewModel.returnToQuizSetup() }
                                )
                            }
                            com.example.ui.viewmodel.QuizScreenState.STICKER_ALBUM -> {
                                StickerAlbumScreen(
                                    unlockedStickers = uiState.unlockedStickersList,
                                    onBackToSetup = { viewModel.returnToQuizSetup() }
                                )
                            }
                            com.example.ui.viewmodel.QuizScreenState.BEST_RESULTS -> {
                                QuizBestResultsScreen(
                                    bestResultsList = uiState.allBestResultsList,
                                    onResetAll = { viewModel.resetAllBestResults() },
                                    onStartCombination = { op, lvl -> viewModel.startQuizWithCombination(op, lvl) },
                                    onBackToPracticeQuiz = { viewModel.returnToQuizSetup() }
                                )
                            }
                        }
                    }
                }

                Spacer(modifier = Modifier.height(10.dp))
                } // End of scrollable Column

                val showBottomControls = !(uiState.mode == AppMode.PRACTICE_QUIZ && 
                    (uiState.quizScreenState == QuizScreenState.STICKER_ALBUM || 
                     uiState.quizScreenState == QuizScreenState.RESULTS ||
                     uiState.quizScreenState == QuizScreenState.BEST_RESULTS ||
                     uiState.quizScreenState == QuizScreenState.SETUP ||
                     uiState.quizScreenState == QuizScreenState.COUNTDOWN))

                if (showBottomControls) {
                    // Bottom Fixed Controls Card (Clear, Number Pad, Enter)
                    androidx.compose.material3.Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(8.dp),
                        elevation = androidx.compose.material3.CardDefaults.cardElevation(defaultElevation = 4.dp),
                        colors = androidx.compose.material3.CardDefaults.cardColors(containerColor = Color.White),
                        shape = RoundedCornerShape(16.dp)
                    ) {
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(8.dp),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            // Clear Button above number buttons
                            ActionButton(
                                text = "Clear",
                                bgColor = AppYellow,
                                onClick = { viewModel.onClearPressed() },
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(46.dp),
                                testTag = "clear_button"
                            )

                            Spacer(modifier = Modifier.height(6.dp))

                            // Number Pad
                            NumberPad(
                                onDigitClick = { digit -> viewModel.onDigitPressed(digit) },
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(120.dp)
                            )

                            Spacer(modifier = Modifier.height(6.dp))

                            // Enter Button below number buttons
                            ActionButton(
                                text = "Enter",
                                bgColor = AppGreen,
                                textColor = AppTextDark,
                                onClick = { viewModel.onEnterPressed() },
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(46.dp),
                                testTag = "enter_button"
                            )
                        }
                    }
                }
            }
        }
    }
}
}



@Composable
fun ActionButton(
    text: String,
    bgColor: Color,
    textColor: Color = AppTextDark,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    testTag: String = ""
) {
    val interactionSource = remember { MutableInteractionSource() }
    val buttonShape = RoundedCornerShape(12.dp)

    Box(
        modifier = modifier
            .background(bgColor, buttonShape)
            .border(1.5.dp, AppBorderOrange, buttonShape)
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
            fontFamily = FontFamily.Serif
        )
    }
}
