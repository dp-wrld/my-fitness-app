package com.fitlife.app.data.repository

import com.fitlife.app.data.local.dao.ChallengeDao
import com.fitlife.app.data.local.entity.ChallengeEntity
import com.fitlife.app.data.local.entity.ChallengeLogEntity
import com.fitlife.app.domain.repository.ChallengeRepository
import javax.inject.Inject

class ChallengeRepositoryImpl @Inject constructor(private val dao: ChallengeDao) : ChallengeRepository {
    override fun getAllChallenges() = dao.getAllChallenges()
    override fun getActiveChallenges() = dao.getActiveChallenges()
    override suspend fun insertChallenge(challenge: ChallengeEntity) = dao.insertChallenge(challenge)
    override suspend fun updateChallenge(challenge: ChallengeEntity) = dao.updateChallenge(challenge)
    override suspend fun deleteChallenge(challenge: ChallengeEntity) = dao.deleteChallenge(challenge)
    override suspend fun insertChallengeLog(log: ChallengeLogEntity) = dao.insertChallengeLog(log)
    override fun getLogsForChallenge(challengeId: Long) = dao.getLogsForChallenge(challengeId)
    override fun getCompletedDaysCount(challengeId: Long) = dao.getCompletedDaysCount(challengeId)
}
