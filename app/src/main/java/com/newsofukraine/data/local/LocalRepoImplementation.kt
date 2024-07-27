package com.newsofukraine.data.local

import com.newsofukraine.domain.model.News
import com.newsofukraine.domain.repo.LocalRepo

class LocalRepoImplementation(private val newsDao: NewsDao): LocalRepo {
    override suspend fun insertNews(news: News) {
        newsDao.insert(news.toEntity())
    }

    override suspend fun getSavedNews(): List<News> {
        return newsDao.read().map { it.toDomain() }
    }

    override suspend fun deleteNews(news: News) {
        newsDao.delete(news.toEntity())
    }

    private fun NewsEntity.toDomain(): News {
        return News(title, img, author, description, url)
    }

    private fun News.toEntity(): NewsEntity {
        return NewsEntity(title, img, author, description, url)
    }
}