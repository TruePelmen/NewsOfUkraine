package com.newsofukraine.app

import com.newsofukraine.domain.model.News

sealed class MainState {
    data object Loading: MainState()
    data class NewsList(val news: List<News>) : MainState()
    data class Error(val error: String?): MainState()
}