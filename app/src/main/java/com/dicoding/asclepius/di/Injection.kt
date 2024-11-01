package com.dicoding.asclepius.di

import android.content.Context
import com.dicoding.asclepius.data.AppRepository
import com.dicoding.asclepius.data.local.room.CancerHistoryDatabase
import com.dicoding.asclepius.data.remote.retrofit.ApiConfig

object Injection {
    fun provideRepository(context : Context) : AppRepository {
        val apiService  = ApiConfig.getApiService()
        val db = CancerHistoryDatabase.getInstance(context)
        val dao = db.cancerHistoryDao()
        return AppRepository.getInstance(apiService, dao)
    }
}