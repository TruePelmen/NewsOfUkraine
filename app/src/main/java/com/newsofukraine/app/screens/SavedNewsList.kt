package com.newsofukraine.app.screens

import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.HorizontalDivider
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.newsofukraine.app.components.NewsItem
import com.newsofukraine.domain.model.News

@Composable
fun SavedNewsList(news: List<News>, onNewsClick: (String) -> Unit) {
    LazyColumn(
        modifier = Modifier.padding(8.dp)
    ) {
        items(items = news) { item ->
            NewsItem(news = item, onNewsClick = onNewsClick, {})
            HorizontalDivider(
                modifier = Modifier.padding(vertical = 8.dp),
                color = Color.LightGray
            )
        }
    }
}