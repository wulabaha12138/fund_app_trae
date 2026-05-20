package com.example.fundapp.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.fundapp.repository.FundRepository

class FundViewModelFactory : ViewModelProvider.Factory {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(FundViewModel::class.java)) {
            return FundViewModel(FundRepository()) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}