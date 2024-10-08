package com.newsofukraine.app.main

import com.newsofukraine.domain.model.News

sealed class MainState {
    data object Loading : MainState()
    data class NewsList(val news: List<News>) : MainState()
    data class Error(val error: String?) : MainState()
    data class SavedNewsList(val news: List<News>) : MainState()
    data class SearchedNewsList(val query: String, val news: List<News>): MainState()
}
