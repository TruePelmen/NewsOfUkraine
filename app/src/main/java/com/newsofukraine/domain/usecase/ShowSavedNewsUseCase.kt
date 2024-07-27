package com.newsofukraine.domain.usecase

import com.newsofukraine.domain.model.News
import com.newsofukraine.domain.repo.LocalRepo

class ShowSavedNewsUseCase(private val localRepo: LocalRepo) {
    suspend fun invoke(): List<News> {
        return localRepo.getSavedNews()
    }
}