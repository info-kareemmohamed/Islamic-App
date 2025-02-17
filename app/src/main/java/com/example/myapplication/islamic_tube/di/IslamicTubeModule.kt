package com.example.myapplication.islamic_tube.di

import com.example.myapplication.core.data.networking.HttpClientFactory
import com.example.myapplication.islamic_tube.data.repository.IslamicTubeRepositoryImpl
import com.example.myapplication.islamic_tube.domain.repository.IslamicTubeRepository
import com.example.myapplication.islamic_tube.presentation.home.HomeViewModel
import io.ktor.client.engine.android.Android
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val IslamicTubeModule = module {
    single { HttpClientFactory.create(Android.create()) }
    single<IslamicTubeRepository> { IslamicTubeRepositoryImpl(get()) }
    viewModel { HomeViewModel(get()) }
}