package com.example.githubsee

import android.app.Application
import com.example.githubsee.di.appModule
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin

class GitHubSEEApplication : Application() {

    override fun onCreate() {
        super.onCreate()

        startKoin {
            androidContext(this@GitHubSEEApplication)
            modules(appModule)
        }
    }
}