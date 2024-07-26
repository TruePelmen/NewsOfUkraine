package com.newsofukraine.app

sealed class MainIntent {
    data object ShowSavedNews: MainIntent()
    data object FetchNews: MainIntent()
    data object SaveNews: MainIntent()
    data object DeleteNews: MainIntent()
    data object OpenNews: MainIntent()
}