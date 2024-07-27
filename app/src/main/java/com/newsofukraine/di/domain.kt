package com.newsofukraine.di

import com.newsofukraine.domain.usecase.DeleteNewsUseCase
import com.newsofukraine.domain.usecase.FetchNewsUseCase
import com.newsofukraine.domain.usecase.SaveNewsUseCase
import com.newsofukraine.domain.usecase.ShowSavedNewsUseCase
import org.koin.dsl.module

val domainModule = module{
    factory <ShowSavedNewsUseCase> {
        ShowSavedNewsUseCase(get())
    }

    factory <FetchNewsUseCase> {
        FetchNewsUseCase(get())
    }

    factory <DeleteNewsUseCase> {
        DeleteNewsUseCase(get())
    }

    factory <SaveNewsUseCase> {
        SaveNewsUseCase(get())
    }
}