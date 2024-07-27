package com.newsofukraine.data.remote

import com.newsofukraine.domain.model.News

data class NewsResponse(
    val status: String,
    val totalResults: Int,
    val articles: List<News>
)