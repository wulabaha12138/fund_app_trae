package com.example.fundapp.repository

import com.example.fundapp.database.dao.FundDao
import com.example.fundapp.database.entity.FundEntity
import com.example.fundapp.model.Fund
import com.example.fundapp.model.FundWithAmount
import com.example.fundapp.model.Stock
import com.example.fundapp.network.FundApiService
import com.example.fundapp.network.response.FundResponse
import com.example.fundapp.network.response.HoldingResponse
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.withContext
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class FundRepository @Inject constructor(
    private val fundDao: FundDao,
    private val fundApiService: FundApiService
) {
    fun getAllFundsWithAmount(): Flow<List<FundWithAmount>> {
        return fundDao.getAllFunds().combine(getAllFundData()) { entities, funds ->
            entities.map { entity ->
                val fund = funds.firstOrNull { it.code == entity.code }
                FundWithAmount(
                    fund ?: Fund(
                        code = entity.code,
                        name = entity.name,
                        netValue = 0.0,
                        change = 0.0,
                        changePercent = 0.0,
                        holdings = emptyList()
                    ),
                    entity.amount
                )
            }
        }
    }

    private suspend fun getAllFundData(): List<Fund> {
        return fundDao.getAllFunds().firstOrNull()?.map { entity ->
            getFundData(entity.code)
        } ?: emptyList()
    }

    suspend fun getFundData(code: String): Fund {
        return try {
            val response = fundApiService.getFundData(code)
            convertToFund(response)
        } catch (e: Exception) {
            // Mock data for demonstration
            createMockFund(code)
        }
    }

    suspend fun addFund(code: String, amount: Double = 0.0): Boolean {
        return try {
            val response = fundApiService.getFundData(code)
            val fundEntity = FundEntity(
                code = response.code,
                name = response.name,
                amount = amount
            )
            fundDao.insertFund(fundEntity)
            true
        } catch (e: Exception) {
            // Mock data for demonstration
            val fundEntity = FundEntity(
                code = code,
                name = "测试基金$code",
                amount = amount
            )
            fundDao.insertFund(fundEntity)
            true
        }
    }

    suspend fun deleteFund(code: String) {
        fundDao.getFundByCode(code)?.let {
            fundDao.deleteFund(it)
        }
    }

    suspend fun deleteAllFunds() {
        fundDao.deleteAllFunds()
    }

    suspend fun getFundCount(): Int {
        return fundDao.getFundCount()
    }

    private fun convertToFund(response: FundResponse): Fund {
        return Fund(
            code = response.code,
            name = response.name,
            netValue = response.netValue,
            change = response.change,
            changePercent = response.changePercent,
            holdings = response.holdings.map { convertToStock(it) },
            isEstimated = response.isEstimated,
            updateTime = response.updateTime
        )
    }

    private fun convertToStock(holding: HoldingResponse): Stock {
        return Stock(
            code = holding.code,
            name = holding.name,
            proportion = holding.proportion,
            change = holding.change,
            price = holding.price
        )
    }

    private fun createMockFund(code: String): Fund {
        val mockHoldings = listOf(
            Stock("600519", "贵州茅台", 10.5, 2.35, 1680.0),
            Stock("000858", "五粮液", 8.2, -1.25, 145.5),
            Stock("601318", "中国平安", 6.8, 0.85, 48.2),
            Stock("000001", "平安银行", 5.5, -0.55, 12.8),
            Stock("600036", "招商银行", 7.2, 1.15, 35.6)
        )
        val estimatedChange = mockHoldings.sumOf { it.proportion * it.change } / 100
        return Fund(
            code = code,
            name = "测试基金$code",
            netValue = 1.2345,
            change = estimatedChange * 0.012345,
            changePercent = estimatedChange,
            holdings = mockHoldings,
            isEstimated = true,
            updateTime = "2024-01-15 10:30"
        )
    }
}