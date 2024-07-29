package com.newsofukraine.app.screens

import android.net.Uri
import android.widget.Toast
import androidx.browser.customtabs.CustomTabsIntent
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.viewModelScope
import com.newsofukraine.app.main.MainIntent
import com.newsofukraine.app.main.MainState
import com.newsofukraine.app.main.MainViewModel
import kotlinx.coroutines.launch

@Composable
fun MainScreen(
    vm: MainViewModel,
    onRetryButtonClick: () -> Unit,
    modifier: Modifier
) {
    val context = LocalContext.current
    val state by vm.state.collectAsState()

    val onNewsClick: (String) -> Unit = { url ->
        val customTabsIntent = CustomTabsIntent.Builder().build()
        customTabsIntent.launchUrl(context, Uri.parse(url))
    }

    val tabs = listOf("Свіжі новини", "Збережені новини")
    var selectedTabIndex by remember { mutableIntStateOf(0) }

    Column(modifier = modifier) {
        TabRow(
            selectedTabIndex = selectedTabIndex,
            modifier = Modifier.fillMaxWidth()
        ) {
            tabs.forEachIndexed { index, title ->
                Tab(
                    selected = selectedTabIndex == index,
                    onClick = {
                        selectedTabIndex = index
                        if (selectedTabIndex == 1) {
                            vm.viewModelScope.launch {
                                vm.userIntent.send(MainIntent.FetchNews)
                            }
                        }
                    },
                    text = { Text(text = title) }
                )
            }
        }

        when (state) {
            is MainState.Loading -> {
                LoadingScreen()
            }

            is MainState.NewsList -> {
                NewsList(news = (state as MainState.NewsList).news, onNewsClick = onNewsClick, vm)
            }

            is MainState.Error -> {
                ErrorScreen(onRetryButtonClick)
                Toast.makeText(
                    LocalContext.current,
                    (state as MainState.Error).error,
                    Toast.LENGTH_SHORT
                ).show()
            }

            is MainState.SavedNewsList -> {
                SavedNewsList(
                    news = (state as MainState.SavedNewsList).news,
                    onNewsClick = onNewsClick
                )
            }

            is MainState.SearchedNewsList -> {
                NewsList(
                    news = (state as MainState.SearchedNewsList).news,
                    onNewsClick = onNewsClick,
                    vm = vm
                )
            }
        }
    }
}
