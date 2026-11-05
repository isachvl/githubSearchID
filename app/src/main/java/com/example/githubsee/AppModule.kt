package com.example.githubsee.di

import com.example.githubsee.AppRepository
import com.example.githubsee.AuthViewModel
import io.ktor.client.HttpClient
import io.ktor.client.engine.okhttp.OkHttp
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.json.Json
import org.koin.androidx.viewmodel.dsl.viewModel
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

    single { AppRepository(get()) }

    viewModel { AuthViewModel(get()) }
}