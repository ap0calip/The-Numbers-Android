package com.example.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "quiz_best_times")
data class QuizBestTimeEntity(
    @PrimaryKey val key: String,
    val operator: String,
    val level: String,
    val bestTimeSeconds: Long,
    val totalStarsGained: Int = 10,
    val totalStarsLost: Int = 0,
    val successPercentage: Float = 100f,
    val stickerName: String = "Super Star",
    val stickerEmoji: String = "🌟",
    val stickerDescription: String = "Bright Shining Star",
    val timestamp: Long = System.currentTimeMillis()
)

@Entity(tableName = "unlocked_stickers")
data class UnlockedStickerEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val stickerName: String,
    val stickerEmoji: String,
    val operator: String,
    val level: String,
    val earnedTimestamp: Long = System.currentTimeMillis()
)

@Entity(tableName = "last_quiz_result")
data class LastQuizResultEntity(
    @PrimaryKey val id: Int = 1,
    val operator: String,
    val level: String,
    val totalStarsGained: Int,
    val totalStarsLost: Int,
    val successPercentage: Float,
    val totalTimeSeconds: Long,
    val isNewBestTime: Boolean,
    val previousBestTimeSeconds: Long?,
    val stickerName: String,
    val stickerEmoji: String,
    val stickerDescription: String,
    val completedTimestamp: Long
)

