package com.example.fundapp.model

data class FundWithAmount(
    val fund: Fund,
    val amount: Double = 0.0
) {
    val estimatedEarnings: Double
        get() = amount * fund.changePercent / 100
}