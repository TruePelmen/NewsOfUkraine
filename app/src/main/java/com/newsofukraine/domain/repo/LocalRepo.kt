package com.newsofukraine.domain.repo

import com.newsofukraine.domain.model.News

interface LocalRepo {
    suspend fun insertNews(news: News)
    suspend fun getSavedNews(): List<News>
    suspend fun deleteNews(news: News)
}