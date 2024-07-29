package com.newsofukraine.app.main

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.lifecycleScope
import com.newsofukraine.app.screens.MainScreen
import com.newsofukraine.app.ui.theme.NewsofUkraineTheme
import com.newsofukraine.di.appModule
import com.newsofukraine.di.dataModule
import com.newsofukraine.di.domainModule
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
