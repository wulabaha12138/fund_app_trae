package com.example.fundapp.network

import com.example.fundapp.network.response.FundResponse
import com.example.fundapp.network.response.StockResponse
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query

interface FundApiService {
    @GET("/fund")
    suspend fun getFundData(@Query("code") code: String): FundResponse

    @GET("/stock")
    suspend fun getStockData(@Query("codes") codes: String): StockResponse

    companion object {
        private const val BASE_URL = "https://api.example.com/"

        fun create(): FundApiService {
            return Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build()
                .create(FundApiService::class.java)
        }
    }
}