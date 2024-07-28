package com.newsofukraine.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class NewsEntity(
    @PrimaryKey val title: String = "",
    val urlToImage: String = "",
    val author: String = "Unknown",
    val description: String = "",
    val url: String = ""
)