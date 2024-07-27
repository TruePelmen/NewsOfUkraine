package com.newsofukraine.domain.usecase

import com.newsofukraine.domain.model.News
import com.newsofukraine.domain.repo.LocalRepo

class SaveNewsUseCase(private val localRepo: LocalRepo) {
    suspend fun invoke(news: News) {
        localRepo.insertNews(news)
    }
}