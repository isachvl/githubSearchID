package com.example.githubsee.InfoRepo

import androidx.annotation.StringRes

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.githubsee.AppRepository
import com.example.githubsee.KeyValueStorage
import com.example.githubsee.R
import io.ktor.client.plugins.ClientRequestException
import io.ktor.http.HttpStatusCode
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.io.IOException
import kotlin.coroutines.cancellation.CancellationException


class RepositoryInfoViewModel(
    private val repository: AppRepository,
    private val storage: KeyValueStorage
) : ViewModel() {

    fun logout(){
        storage.authToken=null
    }

    private val _state = MutableStateFlow<State>(State.Loading)
    val state: StateFlow<State> = _state.asStateFlow()
    fun loadRepository(fullName: String) {

        _state.value = State.Loading

        viewModelScope.launch {
            try {
                val repo = repository.getRepository(fullName)

                _state.value = State.Loaded(
                    githubRepo = repo,
                    readmeState = ReadmeState.Loading
                )
                val readmeState = try {

                    val markdown = repository.getRepositoryReadme(
                        ownerName = repo.owner.login,
                        repositoryName = repo.name,
                        branchName = repo.defaultBranch
                    )

                    if (markdown.isBlank()) {
                        ReadmeState.Empty
                    } else {
                        ReadmeState.Loaded(markdown)
                    }

                } catch (e: CancellationException) {
                    throw e

                } catch (e: ClientRequestException) {
                    if (e.response.status == HttpStatusCode.NotFound) {
                        ReadmeState.Empty
                    } else {
                        ReadmeState.Error(R.string.repo_loading_error)
                    }

                } catch (e: IOException) {
                    ReadmeState.Error(R.string.repo_network_error)

                } catch (e: Exception) {
                    ReadmeState.Error(R.string.repo_loading_error)
                }
                _state.value = State.Loaded(
                    githubRepo = repo,
                    readmeState = readmeState
                )
            } catch (e: CancellationException) {
                throw e

            } catch (e: IOException) {
                _state.value = State.Error(R.string.repo_network_error)

            } catch (e: Exception) {
                _state.value = State.Error(R.string.repo_loading_error)
            }

        }

    }



    sealed interface State {
        object Loading : State

        data class Error(
            @StringRes val error: Int
        ) : State

        data class Loaded(
            val githubRepo: RepoDetails,
            val readmeState: ReadmeState
        ) : State
    }

    sealed interface ReadmeState {
        object Loading : ReadmeState
        object Empty : ReadmeState
        data class Error(@StringRes val error: Int) : ReadmeState
        data class Loaded(val markdown: String) : ReadmeState
    }


}