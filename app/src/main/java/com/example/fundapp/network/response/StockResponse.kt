package com.example.fundapp.network.response

import com.google.gson.annotations.SerializedName

data class StockResponse(
    @SerializedName("data")
    val data: List<StockData>
)

data class StockData(
    @SerializedName("code")
    val code: String,
    @SerializedName("name")
    val name: String,
    @SerializedName("change")
    val change: Double,
    @SerializedName("price")
    val price: Double
)