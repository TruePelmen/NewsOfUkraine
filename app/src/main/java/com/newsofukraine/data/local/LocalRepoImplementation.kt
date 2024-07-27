package com.newsofukraine.data.local

import com.newsofukraine.domain.model.News
import com.newsofukraine.domain.repo.LocalRepo

class LocalRepoImplementation(private val newsDao: NewsDao): LocalRepo {
    override suspend fun getSavedNews(): List<News> {
        return newsDao.read().map { it.toDomain() }
    }

    override suspend fun getDeleteNews(news: News) {
        newsDao.delete(news.toEntity())
    }

    private fun NewsEntity.toDomain(): News {
        return News(title, img, author, description)
    }

    private fun News.toEntity(): NewsEntity {
        return NewsEntity(title, img, author, description)
    }
}