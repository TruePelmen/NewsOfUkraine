package com.newsofukraine.data.remote

import com.newsofukraine.domain.model.News
import retrofit2.Call
import retrofit2.http.GET

interface NewsApiService {
    @GET("top-headlines?country=ua&apiKey=c905c414c03444f293d203635ec3c00a")
    fun getNews(): Call<List<News>>
}