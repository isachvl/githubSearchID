package com.example.githubsee.InfoRepo

import com.example.githubsee.ListRepo.RepoOwner
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class RepoDetails(
    val id: Long,
    val name: String,

    @SerialName("html_url")
    val htmlUrl: String,

    @SerialName("default_branch")
    val defaultBranch: String,

    val owner: RepoOwner,

    @SerialName("stargazers_count")
    val stars: Int,

    @SerialName("forks_count")
    val forks: Int,

    @SerialName("subscribers_count")
    val watchers: Int,

    val license: RepoLicense? = null
)

@Serializable
data class RepoLicense(
    @SerialName("spdx_id")
    val spdxId: String? = null
)

