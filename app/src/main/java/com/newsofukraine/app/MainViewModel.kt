package com.newsofukraine.app

import android.util.Log
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.newsofukraine.domain.model.News
import com.newsofukraine.domain.usecase.DeleteNewsUseCase
import com.newsofukraine.domain.usecase.FetchNewsUseCase
import com.newsofukraine.domain.usecase.SaveNewsUseCase
import com.newsofukraine.domain.usecase.ShowSavedNewsUseCase
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.consumeAsFlow
import kotlinx.coroutines.launch

class MainViewModel(
    private val deleteNewsUseCase: DeleteNewsUseCase,
    private val fetchNewsUseCase: FetchNewsUseCase,
    private val saveNewsUseCase: SaveNewsUseCase,
    private val showSavedNewsUseCase: ShowSavedNewsUseCase
): ViewModel() {

    val userIntent = Channel<MainIntent>(Channel.UNLIMITED)
    private val _state = MutableStateFlow<MainState>(MainState.Loading)
    val state: StateFlow<MainState> = _state

    init {
        handleIntent()
    }

    private fun handleIntent() {
        viewModelScope.launch {
            userIntent.consumeAsFlow().collect { collector ->
                when (collector) {
                    is MainIntent.FetchNews -> fetchNews()
                    is MainIntent.OpenNews -> openNews(collector.news.url)
                    is MainIntent.SaveNews -> saveNews(collector.news)
                    is MainIntent.DeleteNews -> deleteNews(collector.news)
                    is MainIntent.ShowSavedNews -> showSavedNews()
                }
            }
        }
    }

    private fun fetchNews() {
        viewModelScope.launch {
            _state.value = MainState.Loading

            Log.d("API_or_UI_Debug", "fetchNews in viewModel invoked")

            try {
                val newsList = fetchNewsUseCase.invoke()
                _state.value = MainState.NewsList(newsList)
            } catch (e: Exception) {
                _state.value = MainState.Error(e.message ?: "Unknown error")
            }
        }
    }

    private fun openNews(url: String) {
        TODO()
    }

    private fun saveNews(news: News) {
        viewModelScope.launch {
            _state.value = MainState.Loading
            try {
                saveNewsUseCase.invoke(news)
                _state.value = MainState.NewsList(listOf(news))
            } catch (e: Exception) {
                _state.value = MainState.Error(e.message ?: "Unknown error")
            }
        }
    }

    private fun deleteNews(news: News) {
        viewModelScope.launch {
            _state.value = MainState.Loading
            try {
                deleteNewsUseCase.invoke(news)
                _state.value = MainState.NewsList(emptyList())
            } catch (e: Exception) {
                _state.value = MainState.Error(e.message ?: "Unknown error")
            }
        }
    }

    private fun showSavedNews() {
        viewModelScope.launch {
            _state.value = MainState.Loading
            try {
                val savedNewsList = showSavedNewsUseCase.invoke()
                _state.value = MainState.SavedNewsList(savedNewsList)
            } catch (e: Exception) {
                _state.value = MainState.Error(e.message ?: "Unknown error")
            }
        }
    }
}
