package com.fitlife.app.data.repository

import com.fitlife.app.data.local.dao.UserProfileDao
import com.fitlife.app.data.local.entity.UserProfileEntity
import com.fitlife.app.domain.repository.UserProfileRepository
import javax.inject.Inject

class UserProfileRepositoryImpl @Inject constructor(private val dao: UserProfileDao) : UserProfileRepository {
    override fun getProfile() = dao.getProfile()
    override suspend fun insertOrUpdateProfile(profile: UserProfileEntity) = dao.insertOrUpdate(profile)
    override suspend fun updateProfile(profile: UserProfileEntity) = dao.updateProfile(profile)
}
