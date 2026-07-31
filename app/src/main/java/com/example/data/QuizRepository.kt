package com.example.data

import kotlinx.coroutines.flow.Flow

class QuizRepository(private val quizDao: QuizDao) {

    val allBestResults: Flow<List<QuizBestTimeEntity>> = quizDao.getAllBestTimes()

    suspend fun getBestTimeSeconds(operator: String, level: String): Long? {
        val key = "${operator}_${level}"
        return quizDao.getBestTime(key)?.bestTimeSeconds
    }

    suspend fun saveBestResult(
        operator: String,
        level: String,
        timeSeconds: Long,
        totalStarsGained: Int,
        totalStarsLost: Int,
        successPercentage: Float,
        stickerName: String,
        stickerEmoji: String,
        stickerDescription: String
    ): Boolean {
        val key = "${operator}_${level}"
        val existing = quizDao.getBestTime(key)
        val isNewBest = existing == null || timeSeconds < existing.bestTimeSeconds
        if (isNewBest) {
            quizDao.saveBestTime(
                QuizBestTimeEntity(
                    key = key,
                    operator = operator,
                    level = level,
                    bestTimeSeconds = timeSeconds,
                    totalStarsGained = totalStarsGained,
                    totalStarsLost = totalStarsLost,
                    successPercentage = successPercentage,
                    stickerName = stickerName,
                    stickerEmoji = stickerEmoji,
                    stickerDescription = stickerDescription,
                    timestamp = System.currentTimeMillis()
                )
            )
        }
        return isNewBest
    }

    suspend fun clearAllBestResults() {
        quizDao.clearAllBestTimes()
    }

    val unlockedStickers: Flow<List<UnlockedStickerEntity>> = quizDao.getAllUnlockedStickers()

    suspend fun addSticker(stickerName: String, stickerEmoji: String, operator: String, level: String) {
        quizDao.insertSticker(
            UnlockedStickerEntity(
                stickerName = stickerName,
                stickerEmoji = stickerEmoji,
                operator = operator,
                level = level
            )
        )
    }

    suspend fun getLastQuizResultEntity(): LastQuizResultEntity? {
        return quizDao.getLastQuizResult()
    }

    suspend fun saveLastQuizResultEntity(entity: LastQuizResultEntity) {
        quizDao.saveLastQuizResult(entity)
    }
}

