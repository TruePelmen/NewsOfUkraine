package com.newsofukraine.data.remote

import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface NewsApiService {
    @GET("top-headlines")
    suspend fun getNews(
        @Query("country") country: String,
        @Query("apiKey") apiKey: String
    ): Response<NewsResponse>
}