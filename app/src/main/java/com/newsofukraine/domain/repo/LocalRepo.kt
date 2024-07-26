package com.newsofukraine.domain.repo

import com.newsofukraine.domain.model.News

interface LocalRepo {

    suspend fun getSavedNews(): List<News>

    suspend fun getDeleteNews()
}