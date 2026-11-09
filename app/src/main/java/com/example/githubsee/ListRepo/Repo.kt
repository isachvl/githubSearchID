package com.example.githubsee.ListRepo

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class Repo(

    val id: Long,             // Уникальный ID репозитория на GitHub
    val name: String,         // Название репозитория

    @SerialName("full_name")
    val fullName: String,     // Полное название: владелец/репозиторий

    val description: String? = null, // Описание репозитория (может отсутствовать)

    @SerialName("html_url")
    val htmlUrl: String,      // Ссылка на репозиторий на сайте GitHub

    val private: Boolean = false, // true  приватный, false  публичный

    val language: String? = null, // Основной язык программирования (может отсутствовать)

    val owner: RepoOwner      // Информация о владельце репозитория


)
@Serializable
data class RepoOwner(
    val login: String
)