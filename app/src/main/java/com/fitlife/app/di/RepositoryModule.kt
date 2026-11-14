package com.fitlife.app.di

import com.fitlife.app.data.repository.*
import com.fitlife.app.domain.repository.*
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {
    @Binds @Singleton abstract fun bindWorkoutRepository(impl: WorkoutRepositoryImpl): WorkoutRepository
    @Binds @Singleton abstract fun bindStepRepository(impl: StepRepositoryImpl): StepRepository
    @Binds @Singleton abstract fun bindMealRepository(impl: MealRepositoryImpl): MealRepository
    @Binds @Singleton abstract fun bindChallengeRepository(impl: ChallengeRepositoryImpl): ChallengeRepository
    @Binds @Singleton abstract fun bindPlanRepository(impl: WorkoutPlanRepositoryImpl): WorkoutPlanRepository
    @Binds @Singleton abstract fun bindProfileRepository(impl: UserProfileRepositoryImpl): UserProfileRepository
}
