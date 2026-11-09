package com.example.githubsee
import com.example.githubsee.InfoRepo.RepoDetails
import com.example.githubsee.ListRepo.Repo
import io.ktor.client.HttpClient
import io.ktor.client.request.get
import io.ktor.client.request.bearerAuth
import io.ktor.client.call.body
import io.ktor.client.request.header
import io.ktor.client.request.parameter
import io.ktor.client.statement.bodyAsText

class AppRepository(private val client: HttpClient,private val storage: KeyValueStorage) {



    suspend fun getRepository(fullName: String): RepoDetails {

        val token = storage.authToken

        if (token.isNullOrBlank()) {
            throw IllegalStateException()
        }

        val response = client.get("https://api.github.com/repos/$fullName") {
            bearerAuth(token)
        }

        return response.body<RepoDetails>()
    }
    suspend fun getRepositoryReadme(
        ownerName: String,
        repositoryName: String,
        branchName: String
    ): String {

        val token = storage.authToken

        if (token.isNullOrBlank()) {
            throw IllegalStateException()
        }

        val response = client.get(
            "https://api.github.com/repos/$ownerName/$repositoryName/readme"
        ) {
            bearerAuth(token)

            header("Accept", "application/vnd.github.raw+json")

            parameter("ref", branchName)
        }

        return response.bodyAsText()
    }
    suspend fun getRepositories(): List<Repo> {
        val token = storage.authToken

        if (token.isNullOrBlank()) {
            throw IllegalStateException()
        }

        val response = client.get("https://api.github.com/user/repos") {
            bearerAuth(token)
            parameter("per_page", 20)
        }

        return response.body<List<Repo>>()
    }
    suspend fun signIn(token: String): UserInfo {
        val response = client.get("https://api.github.com/user") {
            bearerAuth(token)
        }

        return response.body<UserInfo>()
    }

    // TODO:
}