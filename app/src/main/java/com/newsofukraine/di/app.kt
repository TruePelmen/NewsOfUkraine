package com.newsofukraine.di

import com.newsofukraine.app.MainViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val appModule = module {
    viewModel {
        MainViewModel(
            get(),
            get(),
            get(),
            get()
        )
    }
}