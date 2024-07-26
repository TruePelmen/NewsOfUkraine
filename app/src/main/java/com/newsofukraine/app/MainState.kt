package com.newsofukraine.app

sealed class MainState {
    data object Loading: MainState()
    data object News : MainState()
    data class Error(val error: String?): MainState()
}