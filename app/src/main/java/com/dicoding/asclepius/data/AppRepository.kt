package com.dicoding.asclepius.data

import androidx.lifecycle.LiveData
import androidx.lifecycle.liveData
import com.dicoding.asclepius.data.local.entity.CancerHistoryEntity
import com.dicoding.asclepius.data.local.room.CancerHistoryDao
import com.dicoding.asclepius.data.remote.response.ArticlesItem
import com.dicoding.asclepius.data.remote.retrofit.ApiService
import com.dicoding.asclepius.helper.Result
import retrofit2.HttpException
import java.io.File

class AppRepository(
    private val apiService: ApiService,
    private val cancerHistoryDao: CancerHistoryDao
) {

    fun getNews() : LiveData<Result<List<ArticlesItem>>> = liveData {
        emit(Result.Loading)
        try {
            val res = apiService.getNews()
            val article = res.articles
            emit(Result.Success(article))
        } catch (e : HttpException) {
            emit(Result.Error(e.message.toString()))
        }
    }

    fun getCancerHistory() : LiveData<List<CancerHistoryEntity>> {
        return cancerHistoryDao.getCancerHistory()
    }

    suspend fun insertCancerHistory(cancerHistoryEntity: CancerHistoryEntity) {
        cancerHistoryDao.insertCancerHistory(cancerHistoryEntity)
    }

    suspend fun deleteCancerHistory(cancerHistoryEntity: CancerHistoryEntity) {
        cancerHistoryDao.deleteCancerHistory(cancerHistoryEntity)

        val fileData = File(cancerHistoryEntity.pathImage)
        if (fileData.exists()) fileData.delete()
    }

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