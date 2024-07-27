package com.newsofukraine.di

import android.app.Application
import androidx.room.Room
import com.newsofukraine.data.local.LocalDataBase
import com.newsofukraine.data.local.LocalRepoImplementation
import com.newsofukraine.data.remote.RemoteRepoImplementation
import com.newsofukraine.domain.repo.LocalRepo
import com.newsofukraine.domain.repo.RemoteRepo
import org.koin.dsl.module

fun provideDataBase(application: Application) = Room.databaseBuilder(
    application,
    LocalDataBase::class.java, "database-name"
).build()

fun provideNewsDao(postDataBase: LocalDataBase) = postDataBase.newsDao()

val dataModule = module{
    single<LocalRepo> {
        LocalRepoImplementation(get())
    }

    single<RemoteRepo> {
        RemoteRepoImplementation()
    }

    single { provideDataBase(get()) }
    single { provideNewsDao(get()) }
}