package com.newsofukraine.domain.usecase

import android.util.Log
import com.newsofukraine.domain.model.News
import com.newsofukraine.domain.repo.RemoteRepo

class FetchNewsUseCase(private val remoteRepo: RemoteRepo) {
    suspend fun invoke(): List<News> {
        return remoteRepo.getNews()
    }
}