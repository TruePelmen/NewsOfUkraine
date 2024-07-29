package com.newsofukraine.app.screens

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.HorizontalDivider
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewModelScope
import com.newsofukraine.app.components.NewsItem
import com.newsofukraine.app.components.SearchBar
import com.newsofukraine.app.main.MainIntent
import com.newsofukraine.app.main.MainViewModel
import com.newsofukraine.domain.model.News
import kotlinx.coroutines.launch

@Composable
fun NewsList(news: List<News>, onNewsClick: (String) -> Unit, vm: MainViewModel) {
    var searchQuery by remember { mutableStateOf("") }

    Column {
        SearchBar(
            query = searchQuery,
            onQueryChange = { newQuery -> searchQuery = newQuery },
            onSearch = {
                vm.viewModelScope.launch {
                    vm.userIntent.send(MainIntent.SearchNews(searchQuery))
                }
            }
        )
        LazyColumn(
            modifier = Modifier.padding(8.dp)
        ) {
            items(items = news) { item ->
                NewsItem(
                    news = item,
                    onNewsClick = onNewsClick,
                    onBookmarkClick = { /* implement bookmark click */ })
                HorizontalDivider(
                    modifier = Modifier.padding(vertical = 8.dp),
                    color = Color.LightGray
                )
            }
        }
    }
}


