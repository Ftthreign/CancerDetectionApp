package com.dicoding.asclepius.data

import com.dicoding.asclepius.data.local.room.CancerHistoryDao
import com.dicoding.asclepius.data.remote.retrofit.ApiService

class AppRepository(
    private val apiService: ApiService,
    private val cancerHistoryDao: CancerHistoryDao
) {

    companion object {
        @Volatile
        private var instance : AppRepository? = null
        fun getInstance(
            apiService: ApiService,
            cancerHistoryDao: CancerHistoryDao
        ) : AppRepository =
            instance ?: synchronized(this) {
                instance ?: AppRepository(
                    apiService,
                    cancerHistoryDao
                )
            }.also { instance = it }
    }
}