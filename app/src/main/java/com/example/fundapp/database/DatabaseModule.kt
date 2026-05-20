package com.example.fundapp.database

import android.content.Context
import androidx.room.Room
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
    fun provideFundDatabase(@ApplicationContext context: Context): FundDatabase {
        return Room.databaseBuilder(
            context,
            FundDatabase::class.java,
            "fund_database"
        ).build()
    }

    @Provides
    @Singleton
    fun provideFundDao(database: FundDatabase) = database.fundDao()
}