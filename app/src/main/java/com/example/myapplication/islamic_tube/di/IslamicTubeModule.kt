package com.example.myapplication.islamic_tube.di

import androidx.room.Room
import com.example.myapplication.core.data.networking.HttpClientFactory
import com.example.myapplication.islamic_tube.data.local.CategoryDatabase
import com.example.myapplication.islamic_tube.data.repository.IslamicTubeRepositoryImpl
import com.example.myapplication.islamic_tube.domain.repository.IslamicTubeRepository
import com.example.myapplication.islamic_tube.presentation.details.mvi.DetailsViewModel
import com.example.myapplication.islamic_tube.presentation.home.HomeViewModel
import io.ktor.client.engine.android.Android
import org.koin.android.ext.koin.androidContext
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val IslamicTubeModule = module {
    single { HttpClientFactory.create(Android.create()) }

    single {
        Room.databaseBuilder(androidContext(), CategoryDatabase::class.java, "islamic_tube_db")
            .fallbackToDestructiveMigration()
            .build()
    }

    single { get<CategoryDatabase>().categoryDao() }

    single<IslamicTubeRepository> { IslamicTubeRepositoryImpl(get(), get()) }

    viewModel { HomeViewModel(get()) }
    viewModel { DetailsViewModel(get()) }
}