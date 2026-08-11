package com.example.ui.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.data.QuizDatabase
import com.example.data.QuizRepository
import com.example.data.QuizBestTimeEntity
import com.example.data.UnlockedStickerEntity
import com.example.data.LastQuizResultEntity
import com.example.ui.audio.AudioHelper
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

enum class SelectedField {
    TERM1, TERM2
}

enum class AppMode {
    CALCULATOR, FREE_PRACTICE, PRACTICE_QUIZ
}

enum class QuizScreenState {
    SETUP,
    COUNTDOWN,
    ACTIVE,
    RESULTS,
    STICKER_ALBUM,
    BEST_RESULTS,
    ABOUT
}

enum class MathOperator(val symbol: String, val speechName: String, val label: String) {
    ADD("+", "plus", "Addition"),
    SUBTRACT("-", "minus", "Subtraction"),
    MULTIPLY("×", "times", "Multiplication"),
    DIVIDE("÷", "divided by", "Division")
}

enum class MathLevel(val label: String, val maxVal: Int) {
    RANGE_10("0 to 10", 10),
    RANGE_12("0 to 12", 12),
    RANGE_100("0 to 100", 100)
}

data class QuizProblem(
    val term1: Int,
    val term2: Int,
    val operator: MathOperator = MathOperator.ADD
) {
    val answer: Int
        get() = when (operator) {
            MathOperator.ADD -> term1 + term2
            MathOperator.SUBTRACT -> term1 - term2
            MathOperator.MULTIPLY -> term1 * term2
            MathOperator.DIVIDE -> if (term2 != 0) term1 / term2 else 0
        }
}

data class StickerItem(
    val name: String,
    val emoji: String,
    val description: String
)

val AVAILABLE_STICKERS = listOf(
    StickerItem("Super Star", "🌟", "Bright Shining Star"),
    StickerItem("Rocket Ship", "🚀", "Math Speedster"),
    StickerItem("Smart Owl", "🦉", "Wise Math Genius"),
    StickerItem("Magic Unicorn", "🦄", "Magical Calculator"),
    StickerItem("Brave Lion", "🦁", "King of Numbers"),
    StickerItem("Trophy Champion", "🏆", "Quiz Victor"),
    StickerItem("Cool Robot", "🤖", "Logic Machine"),
    StickerItem("Yummy Ice Cream", "🍦", "Sweet Success"),
    StickerItem("Party Balloon", "🎈", "Celebration Time"),
    StickerItem("Rainbow Magic", "🌈", "Colorful Genius"),
    StickerItem("Golden Crown", "👑", "Math Royalty"),
    StickerItem("Dolphin Jump", "🐬", "Graceful Solver"),
    StickerItem("Cute Puppy", "🐶", "Loyal Buddy"),
    StickerItem("Space Astronaut", "👨‍🚀", "Reaching for the Stars"),
    StickerItem("Panda Bear", "🐼", "Math Master"),
    StickerItem("Firework Pop", "🎆", "Sparkling Victory"),
    StickerItem("Lucky Clover", "🍀", "Super Lucky Solver"),
    StickerItem("Golden Medal", "🥇", "Top Honor"),
    StickerItem("Bright Idea", "💡", "Great Thinker"),
    StickerItem("Lightning Fast", "⚡", "Super Fast Speed"),
    StickerItem("Target Bullseye", "🎯", "Exact Hit"),
    StickerItem("Diamond Mind", "💎", "Precious Knowledge"),
    StickerItem("Fire Streak", "🔥", "Unstoppable Momentum"),
    StickerItem("Brain Power", "🧠", "Sharp Mind"),
    StickerItem("Cool Dino", "🦖", "Mighty Calculator"),
    StickerItem("Magic Wand", "🪄", "Spellbinding Math"),
    StickerItem("Steady Turtle", "🐢", "Determined Solver"),
    StickerItem("Alien Genius", "👽", "Out of This World"),
    StickerItem("Pizza Victory", "🍕", "Slice of Success"),
    StickerItem("Superhero Hero", "🦸", "Math Defender")
)

data class QuizResultData(
    val operator: MathOperator = MathOperator.ADD,
    val level: MathLevel = MathLevel.RANGE_10,
    val totalStarsGained: Int = 10,
    val totalStarsLost: Int = 0,
    val successPercentage: Float = 100f,
    val totalTimeSeconds: Long = 0,
    val isNewBestTime: Boolean = false,
    val previousBestTimeSeconds: Long? = null,
    val stickerWon: StickerItem = AVAILABLE_STICKERS[0],
    val completedTimestamp: Long = System.currentTimeMillis()
)

data class MathUiState(
    val term1: String = "10",
    val term2: String = "0",
    val activeField: SelectedField = SelectedField.TERM1,
    val isEntered: Boolean = true,
    val calculatedSum: Int = 10,
    val mode: AppMode = AppMode.CALCULATOR,
    val operator: MathOperator = MathOperator.ADD,
    val level: MathLevel = MathLevel.RANGE_10,
    val quizProblem: QuizProblem = QuizProblem(4, 3, MathOperator.ADD),
    val quizUserAnswer: String = "",
    val quizFeedback: String? = null,
    val soundEnabled: Boolean = true,

    // Practice Quiz Flow State
    val quizScreenState: QuizScreenState = QuizScreenState.SETUP,
    val showTimerDuringTest: Boolean = false,
    val countdownValue: Int = 3,
    val currentStars: Int = 0,
    val totalStarsGained: Int = 0,
    val totalStarsLost: Int = 0,
    val elapsedTimeSeconds: Long = 0,
    val currentBestTimeSeconds: Long? = null,
    val lastQuizResult: QuizResultData? = null,
    val unlockedStickersList: List<UnlockedStickerEntity> = emptyList(),
    val allBestResultsList: List<QuizBestTimeEntity> = emptyList()
)

class MathViewModel(application: Application) : AndroidViewModel(application) {

    private val audioHelper = AudioHelper(application)
    private val repository: QuizRepository

    private val _uiState = MutableStateFlow(MathUiState())
    val uiState: StateFlow<MathUiState> = _uiState.asStateFlow()

    private var countdownJob: Job? = null
    private var quizTimerJob: Job? = null
    private var calculatorTtsJob: Job? = null

    init {
        val database = QuizDatabase.getDatabase(application)
        repository = QuizRepository(database.quizDao())
        audioHelper.soundEnabled = true

        // Observe unlocked stickers
        viewModelScope.launch {
            repository.unlockedStickers.collect { stickers ->
                _uiState.update { it.copy(unlockedStickersList = stickers) }
            }
        }

        // Observe all best quiz results
        viewModelScope.launch {
            repository.allBestResults.collect { results ->
                _uiState.update { it.copy(allBestResultsList = results) }
            }
        }

        // Load initial best time
        loadBestTime(MathOperator.ADD, MathLevel.RANGE_10)

        // Load last quiz result
        viewModelScope.launch {
            val lastEntity = repository.getLastQuizResultEntity()
            if (lastEntity != null) {
                val op = try { MathOperator.valueOf(lastEntity.operator) } catch (_: Exception) { MathOperator.ADD }
                val lvl = try { MathLevel.valueOf(lastEntity.level) } catch (_: Exception) { MathLevel.RANGE_10 }
                val sticker = AVAILABLE_STICKERS.find { it.name == lastEntity.stickerName }
                    ?: StickerItem(lastEntity.stickerName, lastEntity.stickerEmoji, lastEntity.stickerDescription)
                val resultData = QuizResultData(
                    operator = op,
                    level = lvl,
                    totalStarsGained = lastEntity.totalStarsGained,
                    totalStarsLost = lastEntity.totalStarsLost,
                    successPercentage = lastEntity.successPercentage,
                    totalTimeSeconds = lastEntity.totalTimeSeconds,
                    isNewBestTime = lastEntity.isNewBestTime,
                    previousBestTimeSeconds = lastEntity.previousBestTimeSeconds,
                    stickerWon = sticker,
                    completedTimestamp = lastEntity.completedTimestamp
                )
                _uiState.update { it.copy(lastQuizResult = resultData) }
            }
        }
    }

    private fun scheduleCalculatorTts() {
        calculatorTtsJob?.cancel()
        calculatorTtsJob = viewModelScope.launch {
            delay(2000)
            if (_uiState.value.mode == AppMode.CALCULATOR) {
                val state = _uiState.value
                val v1 = state.term1.toIntOrNull() ?: 0
                val v2 = state.term2.toIntOrNull() ?: 0
                val res = state.calculatedSum
                val opSpeech = state.operator.speechName
                audioHelper.speakText("$v1 $opSpeech $v2 equals $res!")
            }
        }
    }

    private fun loadBestTime(op: MathOperator, level: MathLevel) {
        viewModelScope.launch {
            val best = repository.getBestTimeSeconds(op.name, level.name)
            _uiState.update { it.copy(currentBestTimeSeconds = best) }
        }
    }

    fun selectField(field: SelectedField) {
        if (_uiState.value.mode != AppMode.CALCULATOR) {
            audioHelper.playPopSound()
        }
        _uiState.update { it.copy(activeField = field, isEntered = false) }
    }

    fun selectOperator(op: MathOperator) {
        val isCalc = _uiState.value.mode == AppMode.CALCULATOR
        if (!isCalc) {
            audioHelper.playPopSound()
        }
        _uiState.update { state ->
            val newProblem = generateNextProblem(op, state.level, state.quizProblem)
            val v1 = state.term1.toIntOrNull() ?: 0
            val v2 = state.term2.toIntOrNull() ?: 0
            val res = when (op) {
                MathOperator.ADD -> v1 + v2
                MathOperator.SUBTRACT -> v1 - v2
                MathOperator.MULTIPLY -> v1 * v2
                MathOperator.DIVIDE -> if (v2 != 0) v1 / v2 else 0
            }
            state.copy(
                operator = op,
                quizProblem = newProblem,
                quizUserAnswer = "",
                isEntered = if (isCalc) true else false,
                calculatedSum = if (isCalc) res else state.calculatedSum
            )
        }
        loadBestTime(op, _uiState.value.level)
        if (isCalc) {
            scheduleCalculatorTts()
        }
    }

    fun selectLevel(level: MathLevel) {
        val isCalc = _uiState.value.mode == AppMode.CALCULATOR
        if (!isCalc) {
            audioHelper.playPopSound()
        }
        _uiState.update { state ->
            val maxVal = level.maxVal
            val t1 = if (isCalc) state.term1 else (state.term1.toIntOrNull() ?: 0).coerceAtMost(maxVal).toString()
            val t2 = if (isCalc) state.term2 else (state.term2.toIntOrNull() ?: 0).coerceAtMost(maxVal).toString()
            val newProblem = generateNextProblem(state.operator, level, state.quizProblem)
            val v1 = t1.toIntOrNull() ?: 0
            val v2 = t2.toIntOrNull() ?: 0
            val res = when (state.operator) {
                MathOperator.ADD -> v1 + v2
                MathOperator.SUBTRACT -> v1 - v2
                MathOperator.MULTIPLY -> v1 * v2
                MathOperator.DIVIDE -> if (v2 != 0) v1 / v2 else 0
            }
            state.copy(
                level = level,
                term1 = t1,
                term2 = t2,
                quizProblem = newProblem,
                quizUserAnswer = "",
                isEntered = if (isCalc) true else false,
                calculatedSum = if (isCalc) res else state.calculatedSum
            )
        }
        loadBestTime(_uiState.value.operator, level)
        if (isCalc) {
            scheduleCalculatorTts()
        }
    }

    fun setShowTimerDuringTest(show: Boolean) {
        if (_uiState.value.mode != AppMode.CALCULATOR) {
            audioHelper.playPopSound()
        }
        _uiState.update { it.copy(showTimerDuringTest = show) }
    }

    fun startQuizCountdown() {
        audioHelper.playPopSound()
        countdownJob?.cancel()
        quizTimerJob?.cancel()

        _uiState.update {
            it.copy(
                quizScreenState = QuizScreenState.COUNTDOWN,
                countdownValue = 3
            )
        }

        countdownJob = viewModelScope.launch {
            for (i in 3 downTo 1) {
                _uiState.update { it.copy(countdownValue = i) }
                audioHelper.speakText(i.toString())
                delay(1000)
            }
            _uiState.update { it.copy(countdownValue = 0) }
            audioHelper.speakText("Go!")
            delay(500)
            beginActiveQuiz()
        }
    }

    private fun beginActiveQuiz() {
        val op = _uiState.value.operator
        val level = _uiState.value.level
        val firstProblem = generateNextProblem(op, level, _uiState.value.quizProblem)

        _uiState.update {
            it.copy(
                quizScreenState = QuizScreenState.ACTIVE,
                currentStars = 0,
                totalStarsGained = 0,
                totalStarsLost = 0,
                elapsedTimeSeconds = 0,
                quizProblem = firstProblem,
                quizUserAnswer = "",
                quizFeedback = null
            )
        }

        // Start active quiz timer
        quizTimerJob?.cancel()
        quizTimerJob = viewModelScope.launch {
            while (true) {
                delay(1000)
                _uiState.update { it.copy(elapsedTimeSeconds = it.elapsedTimeSeconds + 1) }
            }
        }
    }

    fun onDigitPressed(digit: Int) {
        val state = _uiState.value
        if (state.mode != AppMode.CALCULATOR) {
            audioHelper.playPopSound()
        }

        if (state.mode == AppMode.CALCULATOR) {
            val currentVal = if (state.activeField == SelectedField.TERM1) state.term1 else state.term2

            val newValString = if (!state.isEntered || currentVal == "0") {
                digit.toString()
            } else {
                val appended = currentVal + digit.toString()
                if (appended.length > 7) currentVal else appended
            }

            val newTerm1 = if (state.activeField == SelectedField.TERM1) newValString else state.term1
            val newTerm2 = if (state.activeField == SelectedField.TERM2) newValString else state.term2

            val v1 = newTerm1.toIntOrNull() ?: 0
            val v2 = newTerm2.toIntOrNull() ?: 0
            val res = when (state.operator) {
                MathOperator.ADD -> v1 + v2
                MathOperator.SUBTRACT -> v1 - v2
                MathOperator.MULTIPLY -> v1 * v2
                MathOperator.DIVIDE -> if (v2 != 0) v1 / v2 else 0
            }

            _uiState.update {
                it.copy(
                    term1 = newTerm1,
                    term2 = newTerm2,
                    isEntered = true,
                    calculatedSum = res
                )
            }
            scheduleCalculatorTts()
        } else {
            // Free Practice or Practice Quiz digit input
            val currentAns = state.quizUserAnswer
            val newAns = if (currentAns.length >= 5) currentAns else currentAns + digit.toString()
            _uiState.update { it.copy(quizUserAnswer = newAns, quizFeedback = null) }
        }
    }

    fun onClearPressed() {
        val state = _uiState.value
        if (state.mode != AppMode.CALCULATOR) {
            audioHelper.playPopSound()
        }

        if (state.mode == AppMode.CALCULATOR) {
            calculatorTtsJob?.cancel()
            val activeVal = if (state.activeField == SelectedField.TERM1) state.term1 else state.term2
            val (newTerm1, newTerm2) = if (activeVal == "0") {
                Pair("0", "0")
            } else {
                if (state.activeField == SelectedField.TERM1) Pair("0", state.term2) else Pair(state.term1, "0")
            }

            val v1 = newTerm1.toIntOrNull() ?: 0
            val v2 = newTerm2.toIntOrNull() ?: 0
            val res = when (state.operator) {
                MathOperator.ADD -> v1 + v2
                MathOperator.SUBTRACT -> v1 - v2
                MathOperator.MULTIPLY -> v1 * v2
                MathOperator.DIVIDE -> if (v2 != 0) v1 / v2 else 0
            }

            _uiState.update {
                it.copy(
                    term1 = newTerm1,
                    term2 = newTerm2,
                    isEntered = false,
                    calculatedSum = res
                )
            }
        } else {
            _uiState.update { it.copy(quizUserAnswer = "", quizFeedback = null) }
        }
    }

    fun onEnterPressed() {
        val state = _uiState.value

        if (state.mode == AppMode.CALCULATOR) {
            calculatorTtsJob?.cancel()
            val val1 = state.term1.toIntOrNull() ?: 0
            val val2 = state.term2.toIntOrNull() ?: 0
            val result = when (state.operator) {
                MathOperator.ADD -> val1 + val2
                MathOperator.SUBTRACT -> val1 - val2
                MathOperator.MULTIPLY -> val1 * val2
                MathOperator.DIVIDE -> if (val2 != 0) val1 / val2 else 0
            }

            val opName = state.operator.speechName
            audioHelper.speakText("$val1 $opName $val2 equals $result!")

            _uiState.update {
                it.copy(isEntered = true, calculatedSum = result)
            }
        } else if (state.mode == AppMode.FREE_PRACTICE) {
            val userAns = state.quizUserAnswer.toIntOrNull()
            val target = state.quizProblem

            if (userAns == target.answer) {
                audioHelper.playSuccessSound()
                val nextProblem = generateNextProblem(state.operator, state.level, state.quizProblem)
                _uiState.update {
                    it.copy(
                        quizProblem = nextProblem,
                        quizUserAnswer = "",
                        quizFeedback = "Correct! 🎉 Great job!"
                    )
                }
            } else {
                audioHelper.playNegativeSound()
                _uiState.update {
                    it.copy(
                        quizUserAnswer = "",
                        quizFeedback = "Incorrect! Try again ❌"
                    )
                }
            }
        } else if (state.mode == AppMode.PRACTICE_QUIZ && state.quizScreenState == QuizScreenState.ACTIVE) {
            val userAns = state.quizUserAnswer.toIntOrNull()
            val target = state.quizProblem

            if (userAns == target.answer) {
                audioHelper.playSuccessSound()
                val newStarsGained = state.totalStarsGained + 1
                val newCurrentStars = state.currentStars + 1

                if (newCurrentStars >= 10) {
                    // Complete quiz!
                    finishQuiz(newStarsGained, state.totalStarsLost)
                } else {
                    val nextProblem = generateNextProblem(state.operator, state.level, state.quizProblem)
                    _uiState.update {
                        it.copy(
                            currentStars = newCurrentStars,
                            totalStarsGained = newStarsGained,
                            quizProblem = nextProblem,
                            quizUserAnswer = "",
                            quizFeedback = "Correct! +1 Star ⭐"
                        )
                    }
                }
            } else {
                audioHelper.playNegativeSound()
                val newStarsLost = state.totalStarsLost + 1
                val newCurrentStars = (state.currentStars - 1).coerceAtLeast(0)

                _uiState.update {
                    it.copy(
                        currentStars = newCurrentStars,
                        totalStarsLost = newStarsLost,
                        quizUserAnswer = "",
                        quizFeedback = "Incorrect! -1 Star ❌"
                    )
                }
            }
        }
    }

    private fun finishQuiz(finalGained: Int, finalLost: Int) {
        quizTimerJob?.cancel()
        val state = _uiState.value
        val totalTime = state.elapsedTimeSeconds
        val op = state.operator
        val level = state.level

        val totalAttempts = finalGained + finalLost
        val successPct = if (totalAttempts > 0) (finalGained.toFloat() / totalAttempts) * 100f else 100f

        // Sticker reward
        val stickerIndex = (state.unlockedStickersList.size) % AVAILABLE_STICKERS.size
        val wonSticker = AVAILABLE_STICKERS[stickerIndex]

        viewModelScope.launch {
            val prevBest = repository.getBestTimeSeconds(op.name, level.name)
            val isNewBest = repository.saveBestResult(
                operator = op.name,
                level = level.name,
                timeSeconds = totalTime,
                totalStarsGained = finalGained,
                totalStarsLost = finalLost,
                successPercentage = successPct,
                stickerName = wonSticker.name,
                stickerEmoji = wonSticker.emoji,
                stickerDescription = wonSticker.description
            )
            repository.addSticker(wonSticker.name, wonSticker.emoji, op.name, level.name)

            val resultData = QuizResultData(
                operator = op,
                level = level,
                totalStarsGained = finalGained,
                totalStarsLost = finalLost,
                successPercentage = successPct,
                totalTimeSeconds = totalTime,
                isNewBestTime = isNewBest,
                previousBestTimeSeconds = prevBest,
                stickerWon = wonSticker
            )

            val lastEntity = LastQuizResultEntity(
                id = 1,
                operator = op.name,
                level = level.name,
                totalStarsGained = finalGained,
                totalStarsLost = finalLost,
                successPercentage = successPct,
                totalTimeSeconds = totalTime,
                isNewBestTime = isNewBest,
                previousBestTimeSeconds = prevBest,
                stickerName = wonSticker.name,
                stickerEmoji = wonSticker.emoji,
                stickerDescription = wonSticker.description,
                completedTimestamp = resultData.completedTimestamp
            )
            repository.saveLastQuizResultEntity(lastEntity)

            audioHelper.playStickerSound()

            _uiState.update {
                it.copy(
                    quizScreenState = QuizScreenState.RESULTS,
                    lastQuizResult = resultData,
                    quizFeedback = null
                )
            }
        }
    }

    fun openStickerAlbum() {
        audioHelper.playPopSound()
        _uiState.update { it.copy(quizScreenState = QuizScreenState.STICKER_ALBUM) }
    }

    fun openStickerAlbumFromMenu() {
        audioHelper.playPopSound()
        countdownJob?.cancel()
        quizTimerJob?.cancel()
        _uiState.update {
            it.copy(
                mode = AppMode.PRACTICE_QUIZ,
                quizScreenState = QuizScreenState.STICKER_ALBUM
            )
        }
    }

    fun openQuizResultsFromMenu() {
        audioHelper.playPopSound()
        countdownJob?.cancel()
        quizTimerJob?.cancel()
        _uiState.update {
            it.copy(
                mode = AppMode.PRACTICE_QUIZ,
                quizScreenState = QuizScreenState.RESULTS
            )
        }
    }

    fun openBestResultsFromMenu() {
        audioHelper.playPopSound()
        countdownJob?.cancel()
        quizTimerJob?.cancel()
        _uiState.update {
            it.copy(
                mode = AppMode.PRACTICE_QUIZ,
                quizScreenState = QuizScreenState.BEST_RESULTS
            )
        }
    }

    fun openAboutFromMenu() {
        audioHelper.playPopSound()
        countdownJob?.cancel()
        quizTimerJob?.cancel()
        _uiState.update {
            it.copy(
                mode = AppMode.PRACTICE_QUIZ,
                quizScreenState = QuizScreenState.ABOUT
            )
        }
    }

    fun resetAllBestResults() {
        audioHelper.playPopSound()
        viewModelScope.launch {
            repository.clearAllBestResults()
            audioHelper.speakText("All best results reset")
        }
    }

    fun startQuizWithCombination(op: MathOperator, lvl: MathLevel) {
        audioHelper.playPopSound()
        selectOperator(op)
        selectLevel(lvl)
        startQuizCountdown()
    }

    fun setAppMode(newMode: AppMode) {
        if (_uiState.value.mode != AppMode.CALCULATOR) {
            audioHelper.playPopSound()
        }
        countdownJob?.cancel()
        quizTimerJob?.cancel()
        calculatorTtsJob?.cancel()
        _uiState.update { state ->
            val nextProblem = generateNextProblem(state.operator, state.level, state.quizProblem)
            val isCalc = newMode == AppMode.CALCULATOR
            val v1 = state.term1.toIntOrNull() ?: 0
            val v2 = state.term2.toIntOrNull() ?: 0
            val res = when (state.operator) {
                MathOperator.ADD -> v1 + v2
                MathOperator.SUBTRACT -> v1 - v2
                MathOperator.MULTIPLY -> v1 * v2
                MathOperator.DIVIDE -> if (v2 != 0) v1 / v2 else 0
            }
            state.copy(
                mode = newMode,
                quizProblem = nextProblem,
                quizUserAnswer = "",
                quizFeedback = null,
                isEntered = if (isCalc) true else state.isEntered,
                calculatedSum = if (isCalc) res else state.calculatedSum,
                quizScreenState = if (newMode == AppMode.PRACTICE_QUIZ) QuizScreenState.SETUP else state.quizScreenState
            )
        }
        if (newMode == AppMode.CALCULATOR) {
            scheduleCalculatorTts()
        }
    }

    fun returnToQuizSetup() {
        if (_uiState.value.mode != AppMode.CALCULATOR) {
            audioHelper.playPopSound()
        }
        countdownJob?.cancel()
        quizTimerJob?.cancel()
        _uiState.update { it.copy(quizScreenState = QuizScreenState.SETUP) }
        loadBestTime(_uiState.value.operator, _uiState.value.level)
    }

    fun onCharacterTapped(countNumber: Int) {
        if (_uiState.value.mode == AppMode.CALCULATOR) {
            audioHelper.speakNumber(countNumber, includePopSound = false)
        } else {
            audioHelper.speakNumber(countNumber, includePopSound = true)
        }
    }

    fun toggleMode() {
        if (_uiState.value.mode != AppMode.CALCULATOR) {
            audioHelper.playPopSound()
        }
        countdownJob?.cancel()
        quizTimerJob?.cancel()
        calculatorTtsJob?.cancel()
        _uiState.update { state ->
            val nextMode = when (state.mode) {
                AppMode.CALCULATOR -> AppMode.FREE_PRACTICE
                AppMode.FREE_PRACTICE -> AppMode.PRACTICE_QUIZ
                AppMode.PRACTICE_QUIZ -> AppMode.CALCULATOR
            }
            val isCalc = nextMode == AppMode.CALCULATOR
            val v1 = state.term1.toIntOrNull() ?: 0
            val v2 = state.term2.toIntOrNull() ?: 0
            val res = when (state.operator) {
                MathOperator.ADD -> v1 + v2
                MathOperator.SUBTRACT -> v1 - v2
                MathOperator.MULTIPLY -> v1 * v2
                MathOperator.DIVIDE -> if (v2 != 0) v1 / v2 else 0
            }
            val nextProblem = generateNextProblem(state.operator, state.level, state.quizProblem)
            state.copy(
                mode = nextMode,
                quizProblem = nextProblem,
                quizUserAnswer = "",
                quizFeedback = null,
                isEntered = if (isCalc) true else state.isEntered,
                calculatedSum = if (isCalc) res else state.calculatedSum,
                quizScreenState = if (nextMode == AppMode.PRACTICE_QUIZ) QuizScreenState.SETUP else state.quizScreenState
            )
        }
        if (_uiState.value.mode == AppMode.CALCULATOR) {
            scheduleCalculatorTts()
        }
    }

    fun toggleSound() {
        val next = !_uiState.value.soundEnabled
        audioHelper.soundEnabled = next
        _uiState.update { it.copy(soundEnabled = next) }
        if (next && _uiState.value.mode != AppMode.CALCULATOR) audioHelper.playPopSound()
    }

    private fun generateNextProblem(
        operator: MathOperator,
        level: MathLevel,
        previousProblem: QuizProblem? = null
    ): QuizProblem {
        val maxVal = level.maxVal

        fun createOne(): QuizProblem {
            return when (operator) {
                MathOperator.ADD -> {
                    val t1 = (0..maxVal).random()
                    val t2 = (0..(maxVal - t1).coerceAtLeast(0)).random()
                    QuizProblem(t1, t2, operator)
                }
                MathOperator.SUBTRACT -> {
                    val t1 = (0..maxVal).random()
                    val t2 = (0..t1).random()
                    QuizProblem(t1, t2, operator)
                }
                MathOperator.MULTIPLY -> {
                    val mMax = if (level == MathLevel.RANGE_100) 10 else maxVal
                    val t1 = (0..mMax).random()
                    val t2 = (0..mMax).random()
                    QuizProblem(t1, t2, operator)
                }
                MathOperator.DIVIDE -> {
                    val dMax = if (level == MathLevel.RANGE_100) 10 else maxVal
                    val t2 = (1..dMax).random()
                    val ans = (0..dMax).random()
                    val t1 = t2 * ans
                    QuizProblem(t1, t2, operator)
                }
            }
        }

        var bestCandidate: QuizProblem? = null
        for (attempt in 0 until 300) {
            val candidate = createOne()
            val hasRepeatedNumberInProblem = (candidate.term1 == candidate.term2)
            val isSameTerm1 = previousProblem != null && candidate.term1 == previousProblem.term1
            val isSameTerm2 = previousProblem != null && candidate.term2 == previousProblem.term2
            val isSameAnswer = previousProblem != null && candidate.answer == previousProblem.answer

            if (!hasRepeatedNumberInProblem && !isSameTerm1 && !isSameTerm2 && !isSameAnswer) {
                return candidate
            }

            if (bestCandidate == null) {
                bestCandidate = candidate
            } else {
                fun violations(p: QuizProblem): Int {
                    var v = 0
                    if (p.term1 == p.term2) v++
                    if (previousProblem != null) {
                        if (p.term1 == previousProblem.term1) v++
                        if (p.term2 == previousProblem.term2) v++
                        if (p.answer == previousProblem.answer) v++
                    }
                    return v
                }
                if (violations(candidate) < violations(bestCandidate)) {
                    bestCandidate = candidate
                }
            }
        }

        return bestCandidate ?: createOne()
    }

    override fun onCleared() {
        super.onCleared()
        countdownJob?.cancel()
        quizTimerJob?.cancel()
        calculatorTtsJob?.cancel()
        audioHelper.shutdown()
    }
}
