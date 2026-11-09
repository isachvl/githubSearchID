package com.example.githubsee

import com.example.githubsee.Auth.AuthViewModel
import com.example.githubsee.InfoRepo.RepositoryInfoViewModel
import com.example.githubsee.ListRepo.RepositoriesListViewModel
import io.ktor.client.HttpClient
import io.ktor.client.engine.okhttp.OkHttp
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.json.Json
import org.koin.android.ext.koin.androidContext
import org.koin.core.module.dsl.viewModel
import org.koin.dsl.module

val appModule = module {

    single {
        HttpClient(OkHttp) {
            install(ContentNegotiation) {
                json(Json {
                    ignoreUnknownKeys = true
                })
            }

            expectSuccess = true
        }
    }

    viewModel { RepositoriesListViewModel(get(), get()) }
    viewModel { AuthViewModel(get(),get()) }
    viewModel { RepositoryInfoViewModel(get(),get()) }
    single{ KeyValueStorage(androidContext()) }

    single { AppRepository(get(),get()) }
}