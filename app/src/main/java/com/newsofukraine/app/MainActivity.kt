package com.newsofukraine.app

import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.material3.HorizontalDivider
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import coil.compose.rememberImagePainter
import com.newsofukraine.app.ui.theme.NewsofUkraineTheme
import com.newsofukraine.di.appModule
import com.newsofukraine.di.dataModule
import com.newsofukraine.di.domainModule
import com.newsofukraine.domain.model.News
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.GlobalContext.startKoin
import org.koin.android.ext.android.get
import androidx.compose.ui.layout.ContentScale
import androidx.compose.foundation.layout.Column
import androidx.compose.material3.Button
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.launch


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

        enableEdgeToEdge()
        setContent {
            NewsofUkraineTheme {
                MainScreen(
                    viewModel,
                    onRetryButtonClick,
                    Modifier.fillMaxSize())
            }
        }
    }
}

@Composable
fun MainScreen(
    vm: MainViewModel,
    onRetryButtonClick: () -> Unit,
    modifier: Modifier) {
    val state = vm.state.value
    when (val state = vm.state.value) {
        is MainState.Loading -> LoadingScreen()
        is MainState.NewsList -> NewsList(news = state.news)
        is MainState.Error -> {
            ErrorScreen(onRetryButtonClick)
            Toast.makeText(LocalContext.current, state.error, Toast.LENGTH_SHORT).show()
        }
        is MainState.SavedNewsList -> TODO()
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
    Text(text = "Something went wrong =(")
    Button(onClick = onButtonClick ){
        Text(text= "Retry")
    }
}

@Composable
fun NewsList(news: List<News>) {
    LazyColumn {
        items(items = news) {
            NewsItem(news = it)
            HorizontalDivider(
                modifier = Modifier.padding(top = 4.dp, bottom = 4.dp),
                color = Color.LightGray
            )
        }
    }
}

@Composable
fun NewsItem(news: News) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(100.dp)
    ) {
        val painter = rememberImagePainter(data = news.img)
        Image(
            painter = painter,
            contentDescription = null,
            modifier = Modifier.size(100.dp),
            contentScale = ContentScale.FillHeight
        )
        Column(modifier = Modifier
            .fillMaxSize()
            .padding(start = 4.dp)) {
            Text(text = news.title, fontWeight = FontWeight.Bold)
            Text(text = news.author)
            Text(text = news.description)
        }
    }
}