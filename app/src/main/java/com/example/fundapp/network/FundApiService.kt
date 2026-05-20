package com.example.fundapp.network

import retrofit2.http.GET
import retrofit2.http.Query

interface FundApiService {
    @GET("/fund")
    suspend fun getFundData(@Query("code") code: String): FundResponse

    @GET("/stock")
    suspend fun getStockData(@Query("codes") codes: String): StockResponse
}