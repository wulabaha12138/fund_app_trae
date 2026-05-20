package com.example.fundapp.database.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "funds")
data class FundEntity(
    @PrimaryKey
    val code: String,
    val name: String,
    val amount: Double = 0.0,
    val createdAt: Long = System.currentTimeMillis()
)