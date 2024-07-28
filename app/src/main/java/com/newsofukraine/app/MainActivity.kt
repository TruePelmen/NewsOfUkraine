package com.newsofukraine.app

import android.os.Bundle
import android.util.Log
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
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.Divider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextOverflow
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

        lifecycleScope.launch {
            viewModel.userIntent.send(MainIntent.FetchNews)
        }

        enableEdgeToEdge()
        setContent {
            NewsofUkraineTheme {
                Surface(color = MaterialTheme.colorScheme.background){
                    MainScreen(
                        viewModel,
                        onRetryButtonClick,
                        Modifier.fillMaxSize()
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
    val state by vm.state.collectAsState()

    when (state) {
        is MainState.Loading -> {
            LoadingScreen()
            Log.d("API_or_UI_Debug", "LoadingScreen in mainActivity invoked")
        }
        is MainState.NewsList -> {
            NewsList(news = (state as MainState.NewsList).news)
            Log.d("API_or_UI_Debug", "NewsList in mainActivity invoked")
        }
        is MainState.Error -> {
            ErrorScreen(onRetryButtonClick)
            Log.d("API_or_UI_Debug", "Error in mainActivity invoked")
            Toast.makeText(LocalContext.current, (state as MainState.Error).error, Toast.LENGTH_SHORT).show()
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
fun NewsList(news: List<News>) {
    LazyColumn(
        modifier = Modifier.padding(8.dp)
    ) {
        items(items = news) { item ->
            NewsItem(news = item)
            Divider(
                modifier = Modifier.padding(vertical = 8.dp),
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
            .height(120.dp)
            .padding(8.dp)
    ) {
        val painter = rememberImagePainter(data = news.urlToImage)
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
                .fillMaxSize()
                .padding(4.dp)
        ) {
            Text(
                text = news.title ?: "Untitled",
                fontWeight = FontWeight.Bold,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis,
                style = MaterialTheme.typography.titleSmall            )
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
    }
}
