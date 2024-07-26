package com.newsofukraine.app

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.consumeAsFlow
import kotlinx.coroutines.launch

class MainViewModel: ViewModel() {

    val userIntent = Channel<MainIntent>(Channel.UNLIMITED)

    var state = mutableStateOf<MainState>(MainState.Loading)
        private set

    init {
        handleIntent()
    }

    private fun handleIntent() {
        viewModelScope.launch {
            userIntent.consumeAsFlow().collect { collector ->
                when (collector) {
                    is MainIntent.FetchNews -> fetchNews()
                    is MainIntent.OpenNews -> openNews()
                    is MainIntent.SaveNews -> saveNews()
                    is MainIntent.DeleteNews -> deleteNews()
                    is MainIntent.ShowSavedNews -> showSavedNews()
                }
            }
        }
    }

    private fun fetchNews(){

    }

    private fun openNews(){

    }

    private fun saveNews(){

    }

    private fun deleteNews(){

    }

    private fun showSavedNews(){

    }

}