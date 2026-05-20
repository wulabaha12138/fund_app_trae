package com.example.fundapp.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.fundapp.model.FundWithAmount
import com.example.fundapp.repository.FundRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class FundViewModel(
    private val fundRepository: FundRepository
) : ViewModel() {

    private val _funds = MutableLiveData<List<FundWithAmount>>()
    val funds: LiveData<List<FundWithAmount>> = _funds

    private val _isRefreshing = MutableLiveData(false)
    val isRefreshing: LiveData<Boolean> = _isRefreshing

    private val _errorMessage = MutableLiveData<String?>()
    val errorMessage: LiveData<String?> = _errorMessage

    private var refreshJob: kotlinx.coroutines.Job? = null

    init {
        loadFunds()
        startAutoRefresh()
    }

    fun loadFunds() {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                fundRepository.getAllFundsWithAmount().collect { funds ->
                    _funds.postValue(funds)
                }
            } catch (e: Exception) {
                _errorMessage.postValue(e.message)
            }
        }
    }

    fun refreshFunds() {
        _isRefreshing.value = true
        viewModelScope.launch(Dispatchers.IO) {
            try {
                delay(1000)
                loadFunds()
                _isRefreshing.postValue(false)
            } catch (e: Exception) {
                _errorMessage.postValue(e.message)
                _isRefreshing.postValue(false)
            }
        }
    }

    fun addFund(code: String, amount: Double = 0.0) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val success = fundRepository.addFund(code, amount)
                if (success) {
                    loadFunds()
                } else {
                    _errorMessage.postValue("添加失败")
                }
            } catch (e: Exception) {
                _errorMessage.postValue(e.message)
            }
        }
    }

    fun deleteFund(code: String) {
        viewModelScope.launch(Dispatchers.IO) {
            fundRepository.deleteFund(code)
            loadFunds()
        }
    }

    fun deleteAllFunds() {
        viewModelScope.launch(Dispatchers.IO) {
            fundRepository.deleteAllFunds()
            loadFunds()
        }
    }

    suspend fun getFundCount(): Int {
        return fundRepository.getFundCount()
    }

    private fun startAutoRefresh() {
        refreshJob = viewModelScope.launch(Dispatchers.IO) {
            while (true) {
                delay(5 * 60 * 1000)
                if (isTradingTime()) {
                    refreshFunds()
                }
            }
        }
    }

    private fun isTradingTime(): Boolean {
        val now = java.util.Calendar.getInstance()
        val hour = now.get(java.util.Calendar.HOUR_OF_DAY)
        val minute = now.get(java.util.Calendar.MINUTE)
        val dayOfWeek = now.get(java.util.Calendar.DAY_OF_WEEK)

        if (dayOfWeek == java.util.Calendar.SATURDAY || dayOfWeek == java.util.Calendar.SUNDAY) {
            return false
        }

        return (hour == 9 && minute >= 30) ||
               (hour in 10..11 && minute <= 30) ||
               (hour in 13..14) ||
               (hour == 15 && minute == 0)
    }

    override fun onCleared() {
        super.onCleared()
        refreshJob?.cancel()
    }

    fun clearErrorMessage() {
        _errorMessage.value = null
    }
}