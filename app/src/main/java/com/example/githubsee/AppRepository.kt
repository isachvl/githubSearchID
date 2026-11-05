package com.example.githubsee
import io.ktor.client.HttpClient
import io.ktor.client.request.get
import io.ktor.client.request.bearerAuth
import io.ktor.client.call.body

class AppRepository(private val client: HttpClient) {

//    suspend fun getRepositories(): List<Repo> {
//        // TODO:
//    }
//
//    suspend fun getRepository(repoId: String): RepoDetails {
//        // TODO:
//    }
//
//    suspend fun getRepositoryReadme(ownerName: String, repositoryName: String, branchName: String): String {
//        // TODO:
//    }

    suspend fun signIn(token: String): UserInfo {
        val response = client.get("https://api.github.com/user") {
            bearerAuth(token)
        }

        return response.body<UserInfo>()
    }

    // TODO:
}