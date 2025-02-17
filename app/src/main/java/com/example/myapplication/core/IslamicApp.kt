package com.example.myapplication.core

import android.app.Application
import com.example.myapplication.islamic_tube.di.IslamicTubeModule
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin

class IslamicApp : Application() {
    override fun onCreate() {
        super.onCreate()
        startKoin {
            androidLogger()
            androidContext(this@IslamicApp)
            modules(listOf(IslamicTubeModule))
        }
    }
}