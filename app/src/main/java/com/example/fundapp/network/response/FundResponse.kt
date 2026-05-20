package com.example.fundapp.network.response

import com.google.gson.annotations.SerializedName

data class FundResponse(
    @SerializedName("code")
    val code: String,
    @SerializedName("name")
    val name: String,
    @SerializedName("netValue")
    val netValue: Double,
    @SerializedName("change")
    val change: Double,
    @SerializedName("changePercent")
    val changePercent: Double,
    @SerializedName("holdings")
    val holdings: List<HoldingResponse>,
    @SerializedName("updateTime")
    val updateTime: String = "",
    @SerializedName("isEstimated")
    val isEstimated: Boolean = false
)

data class HoldingResponse(
    @SerializedName("code")
    val code: String,
    @SerializedName("name")
    val name: String,
    @SerializedName("proportion")
    val proportion: Double,
    @SerializedName("change")
    val change: Double,
    @SerializedName("price")
    val price: Double
)