package com.example.fundapp.database

import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.fundapp.FundApplication
import com.example.fundapp.database.dao.FundDao
import com.example.fundapp.database.entity.FundEntity

@Database(
    entities = [FundEntity::class],
    version = 1,
    exportSchema = false
)
abstract class FundDatabase : RoomDatabase() {
    abstract fun fundDao(): FundDao

    companion object {
        private var instance: FundDatabase? = null

        fun getInstance(): FundDatabase {
            if (instance == null) {
                synchronized(FundDatabase::class.java) {
                    if (instance == null) {
                        instance = Room.databaseBuilder(
                            FundApplication.instance.applicationContext,
                            FundDatabase::class.java,
                            "fund_db"
                        ).build()
                    }
                }
            }
            return instance!!
        }
    }
}