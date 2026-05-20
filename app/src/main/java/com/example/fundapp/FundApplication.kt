package com.example.fundapp

import android.app.Application

class FundApplication : Application() {
    companion object {
        lateinit var instance: FundApplication
            private set
    }

    override fun onCreate() {
        super.onCreate()
        instance = this
    }
}