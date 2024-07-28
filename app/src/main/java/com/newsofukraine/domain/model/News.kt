package com.newsofukraine.domain.model

data class News(
    val title: String = "",
    val urlToImage: String = "",
    val author: String = "Unknown",
    val description: String = "",
    val url: String = ""
)
