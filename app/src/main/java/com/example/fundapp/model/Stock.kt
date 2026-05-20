package com.example.fundapp.model

data class Stock(
    val code: String,
    val name: String,
    val proportion: Double,
    val change: Double,
    val price: Double
)