package com.application.sugarrush.di

import android.content.Context
import androidx.room.Room
import androidx.room.RoomDatabase
import com.application.sugarrush.data.database.RecipeDatabase
import com.application.sugarrush.util.constants.Companion.DATABASE_NAME
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {
    @Singleton
    @Provides
    fun provideDatabase(
        @ApplicationContext context:Context
    )= Room.databaseBuilder(
        context,
        RecipeDatabase::class.java,
        DATABASE_NAME
    ).build()

    @Singleton
    @Provides
    fun provideDao(database:RecipeDatabase)  =database.recipeDao()
}