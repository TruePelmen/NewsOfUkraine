package com.newsofukraine.data.local

import androidx.room.Entity

@Entity
data class NewsEntity(
    val title: String = "",
    val img: String = "",
    val author: String = "Unknown",
    val description: String = ""
)