package com.example.fundapp.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.fundapp.database.dao.FundDao
import com.example.fundapp.database.entity.FundEntity

@Database(
    entities = [FundEntity::class],
    version = 1,
    exportSchema = false
)
abstract class FundDatabase : RoomDatabase() {
    abstract fun fundDao(): FundDao
}