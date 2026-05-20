package com.example.fundapp.model

data class Fund(
    val code: String,
    val name: String,
    val netValue: Double,
    val change: Double,
    val changePercent: Double,
    val holdings: List<Stock>,
    val isEstimated: Boolean = false,
    val updateTime: String = ""
)