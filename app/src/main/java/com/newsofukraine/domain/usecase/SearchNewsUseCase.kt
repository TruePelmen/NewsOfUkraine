package com.newsofukraine.domain.usecase

import com.newsofukraine.domain.model.News
import com.newsofukraine.domain.repo.RemoteRepo

class SearchNewsUseCase(private val newsRepository: RemoteRepo) {
    suspend operator fun invoke(query: String): List<News> {
        return newsRepository.searchNews(query)
    }
}
