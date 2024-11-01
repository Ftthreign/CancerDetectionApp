package com.dicoding.asclepius.data.remote.retrofit

import com.dicoding.asclepius.BuildConfig
import com.dicoding.asclepius.data.remote.response.NewsResponse
import retrofit2.http.GET
import retrofit2.http.Query

interface ApiService {
    @GET("top-headlines")
    suspend fun getNews(
        @Query("q")
        query : String = "cancer",
        @Query("category")
        language : String = "health",
        @Query("apiKey")
        apiKey : String = BuildConfig.API_KEY
    ) : NewsResponse
}