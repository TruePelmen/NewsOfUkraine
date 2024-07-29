package com.newsofukraine.app.main

import com.newsofukraine.domain.model.News

sealed class MainIntent {
    data object ShowSavedNews : MainIntent()
    data object FetchNews : MainIntent()
    data class SaveNews(val news: News) : MainIntent()
    data class DeleteNews(val news: News) : MainIntent()
    data class SearchNews(val query: String) : MainIntent()
}
