package com.example.fundapp.database.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import com.example.fundapp.database.entity.FundEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface FundDao {
    @Query("SELECT * FROM funds ORDER BY createdAt DESC")
    fun getAllFunds(): Flow<List<FundEntity>>

    @Query("SELECT * FROM funds WHERE code = :code")
    suspend fun getFundByCode(code: String): FundEntity?

    @Insert
    suspend fun insertFund(fund: FundEntity)

    @Delete
    suspend fun deleteFund(fund: FundEntity)

    @Query("DELETE FROM funds")
    suspend fun deleteAllFunds()

    @Query("SELECT COUNT(*) FROM funds")
    suspend fun getFundCount(): Int
}