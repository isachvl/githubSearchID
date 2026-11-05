package com.example.githubsee

import kotlinx.serialization.Serializable

@Serializable
data class UserInfo(
    val id: Long,
    val login: String,
    val name: String? = null
)