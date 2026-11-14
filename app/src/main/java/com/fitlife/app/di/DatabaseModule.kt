package com.fitlife.app.di

import android.content.Context
import androidx.room.Room
import com.fitlife.app.data.local.FitLifeDatabase
import com.fitlife.app.data.local.dao.*
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    @Provides
    @Singleton
    fun provideFitLifeDatabase(@ApplicationContext context: Context): FitLifeDatabase {
        return Room.databaseBuilder(
            context,
            FitLifeDatabase::class.java,
            "fitlife_database"
        ).build()
    }

    @Provides fun provideWorkoutDao(db: FitLifeDatabase): WorkoutDao = db.workoutDao()
    @Provides fun provideStepDao(db: FitLifeDatabase): StepDao = db.stepDao()
    @Provides fun provideMealDao(db: FitLifeDatabase): MealDao = db.mealDao()
    @Provides fun provideChallengeDao(db: FitLifeDatabase): ChallengeDao = db.challengeDao()
    @Provides fun provideWorkoutPlanDao(db: FitLifeDatabase): WorkoutPlanDao = db.workoutPlanDao()
    @Provides fun provideUserProfileDao(db: FitLifeDatabase): UserProfileDao = db.userProfileDao()
}
