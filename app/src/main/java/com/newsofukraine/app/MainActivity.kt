package com.newsofukraine.app

import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.browser.customtabs.CustomTabsIntent
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.viewModelScope
import coil.compose.rememberImagePainter
import com.newsofukraine.R
import com.newsofukraine.app.ui.theme.NewsofUkraineTheme
import com.newsofukraine.di.appModule
import com.newsofukraine.di.dataModule
import com.newsofukraine.di.domainModule
import com.newsofukraine.domain.model.News
import kotlinx.coroutines.launch
import org.koin.android.ext.android.get
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.GlobalContext.startKoin


class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        startKoin {
            androidContext(application)
            modules(listOf(appModule, domainModule, dataModule))
        }

        val viewModel: MainViewModel = get<MainViewModel>()

        val onRetryButtonClick: () -> Unit = {
            lifecycleScope.launch {
                viewModel.userIntent.send(MainIntent.FetchNews)
            }
        }

        lifecycleScope.launch {
            viewModel.userIntent.send(MainIntent.FetchNews)
        }

        enableEdgeToEdge()
        setContent {
            NewsofUkraineTheme {
                Scaffold(Modifier.padding(5.dp)) { innerPadding ->
                    MainScreen(
                        viewModel,
                        onRetryButtonClick,
                        Modifier.padding(innerPadding)
                    )
                }
            }
        }
    }
}

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
                Log.d("API_or_UI_Debug", "LoadingScreen in mainActivity invoked")
            }

            is MainState.NewsList -> {
                NewsList(news = (state as MainState.NewsList).news, onNewsClick = onNewsClick)
                Log.d("API_or_UI_Debug", "NewsList in mainActivity invoked")
            }

            is MainState.Error -> {
                ErrorScreen(onRetryButtonClick)
                Log.d("API_or_UI_Debug", "Error in mainActivity invoked")
                Toast.makeText(
                    LocalContext.current,
                    (state as MainState.Error).error,
                    Toast.LENGTH_SHORT
                ).show()
            }

            is MainState.SavedNewsList -> {
                NewsList(news = (state as MainState.SavedNewsList).news, onNewsClick = onNewsClick)
            }

            is MainState.SearchedNewsList -> {
                NewsList(news = (state as MainState.SearchedNewsList).news, onNewsClick = onNewsClick)
            }
        }
    }
}

@Composable
fun LoadingScreen() {
    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        CircularProgressIndicator()
    }
}

@Composable
fun ErrorScreen(onButtonClick: () -> Unit) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        contentAlignment = Alignment.Center
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Text(text = "Something went wrong =(", style = MaterialTheme.typography.bodyLarge)
            Spacer(modifier = Modifier.height(8.dp))
            Button(onClick = onButtonClick) {
                Text(text = "Retry")
            }
        }
    }
}

@Composable
fun NewsList(news: List<News>, onNewsClick: (String) -> Unit) {
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

@Composable
fun NewsItem(news: News, onNewsClick: (String) -> Unit, onBookmarkClick: () -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(120.dp)
            .padding(8.dp)
            .clickable { onNewsClick(news.url) }
    ) {
        val painter = rememberImagePainter(
            data = news.urlToImage,
            builder = {
                placeholder(R.drawable.empty)
                error(R.drawable.empty)
            }
        )

        Image(
            painter = painter,
            contentDescription = null,
            modifier = Modifier
                .size(100.dp)
                .clip(RoundedCornerShape(8.dp)),
            contentScale = ContentScale.Crop
        )

        Spacer(modifier = Modifier.width(8.dp))

        Column(
            modifier = Modifier
                .weight(1f) // to take remaining space
                .padding(4.dp)
        ) {
            Text(
                text = news.title ?: "Untitled",
                fontWeight = FontWeight.Bold,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis,
                style = MaterialTheme.typography.titleSmall
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = news.author ?: "Unknown Author",
                style = MaterialTheme.typography.bodySmall,
                color = Color.Gray
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = news.description ?: "No Description Available",
                maxLines = 3,
                overflow = TextOverflow.Ellipsis,
                style = MaterialTheme.typography.bodyMedium
            )
        }

        IconButton(
            onClick = onBookmarkClick,
            modifier = Modifier
                .align(Alignment.CenterVertically)
                .padding(16.dp)
        ) {
            Icon(Icons.Default.FavoriteBorder, contentDescription = "Bookmark")
        }
    }
}
