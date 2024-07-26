package com.newsofukraine.domain.repo

import com.newsofukraine.domain.model.News

interface RemoteRepo {
    fun getNews(): List<News>
}