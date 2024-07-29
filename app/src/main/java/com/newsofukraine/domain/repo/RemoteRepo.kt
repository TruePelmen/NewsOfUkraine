package com.newsofukraine.domain.repo

import com.newsofukraine.domain.model.News

interface RemoteRepo {
    suspend fun getNews(): List<News>
    suspend fun searchNews(query: String): List<News>
}