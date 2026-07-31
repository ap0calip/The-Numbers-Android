package com.example.data

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface QuizDao {
    @Query("SELECT * FROM quiz_best_times WHERE key = :key LIMIT 1")
    suspend fun getBestTime(key: String): QuizBestTimeEntity?

    @Query("SELECT * FROM quiz_best_times")
    fun getAllBestTimes(): Flow<List<QuizBestTimeEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun saveBestTime(bestTime: QuizBestTimeEntity)

    @Query("DELETE FROM quiz_best_times")
    suspend fun clearAllBestTimes()

    @Query("SELECT * FROM unlocked_stickers ORDER BY earnedTimestamp DESC")
    fun getAllUnlockedStickers(): Flow<List<UnlockedStickerEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertSticker(sticker: UnlockedStickerEntity)

    @Query("SELECT * FROM last_quiz_result WHERE id = 1 LIMIT 1")
    suspend fun getLastQuizResult(): LastQuizResultEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun saveLastQuizResult(result: LastQuizResultEntity)
}
