package com.newsofukraine.data.remote

import android.util.Log
import com.newsofukraine.domain.model.News
import com.newsofukraine.domain.repo.LocalRepo
import com.newsofukraine.domain.repo.RemoteRepo
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class RemoteRepoImplementation : RemoteRepo {
    private val apiKey = "c905c414c03444f293d203635ec3c00a"
    private val BASE_URL = "https://newsapi.org/v2/"

    private fun getRetrofit() = Retrofit.Builder()
        .baseUrl(BASE_URL)
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    override suspend fun getNews(): List<News> {
        val retrofit = getRetrofit()
        val newsApiService = retrofit.create(NewsApiService::class.java)

        val response = newsApiService.getNews(country = "ua", apiKey = apiKey)

        if (response.isSuccessful && response.body() != null) {
            val newsList = response.body()!!.articles
            val returnList: MutableList<News> = mutableListOf()
            newsList.forEach {
                returnList.add(
                    News(
                        title = it.title,
                        urlToImage = it.urlToImage ?: "",
                        author = it.author ?: "Unknown",
                        description = it.description ?: "",
                        url = it.url
                    )
                )
            }
            return returnList
        } else {
            Log.e(
                "API_or_UI_Debug",
                "API response failed with error: ${response.errorBody()?.string()}"
            )
            return emptyList()
        }
    }

    override suspend fun searchNews(query: String): List<News> {
        val retrofit = getRetrofit()
        val newsApiService = retrofit.create(NewsApiService::class.java)
        val response = newsApiService.searchNews(query, apiKey = apiKey, country = "ua")
        val allNews = response.body()?.articles

        val returnList: MutableList<News> = mutableListOf()
        allNews?.forEach {
            returnList.add(
                News(
                    title = it.title,
                    urlToImage = it.urlToImage ?: "",
                    author = it.author ?: "Unknown",
                    description = it.description ?: "",
                    url = it.url
                )
            )
        }
        return returnList

    }
}



