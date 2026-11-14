package com.fitlife.app.data.local.dao

import androidx.room.*
import com.fitlife.app.data.local.entity.ChallengeEntity
import com.fitlife.app.data.local.entity.ChallengeLogEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface ChallengeDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertChallenge(challenge: ChallengeEntity): Long

    @Update
    suspend fun updateChallenge(challenge: ChallengeEntity)

    @Delete
    suspend fun deleteChallenge(challenge: ChallengeEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertChallengeLog(log: ChallengeLogEntity)

    @Query("SELECT * FROM challenges ORDER BY startDate DESC")
    fun getAllChallenges(): Flow<List<ChallengeEntity>>

    @Query("SELECT * FROM challenges WHERE isActive = 1")
    fun getActiveChallenges(): Flow<List<ChallengeEntity>>

    @Query("SELECT * FROM challenge_logs WHERE challengeId = :challengeId ORDER BY day ASC")
    fun getLogsForChallenge(challengeId: Long): Flow<List<ChallengeLogEntity>>

    @Query("SELECT COUNT(*) FROM challenge_logs WHERE challengeId = :challengeId AND isCompleted = 1")
    fun getCompletedDaysCount(challengeId: Long): Flow<Int>
}
