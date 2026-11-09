package com.example.githubsee.ListRepo

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

class RepositoriesListViewModel(
    private val repository: AppRepository,
    private val storage: KeyValueStorage
) : ViewModel() {

    private val _state = MutableStateFlow<State>(State.Loading)
    val state: StateFlow<State> = _state.asStateFlow()

    init {
        if (storage.authToken.isNullOrBlank()) {
            _state.value = State.Unauthorized
        } else {
            loadRepositories()
        }
    }
    
    fun logout() {
        storage.authToken = null
        _state.value = State.Unauthorized
    }
    fun loadRepositories() {

        _state.value = State.Loading

        viewModelScope.launch {
            try {
                val repos = repository.getRepositories()

                _state.value = if (repos.isEmpty()) {
                    State.Empty
                } else {
                    State.Loaded(repos)
                }

            } catch (e: CancellationException) {
                throw e

            } catch (e: ClientRequestException) {
                _state.value =
                    if (e.response.status == HttpStatusCode.Unauthorized) {
                        State.Error(R.string.repo_invalid_token)
                    } else {
                        State.Error(R.string.repo_loading_error)
                    }

            } catch (e: IllegalStateException) {
                _state.value = State.Error(R.string.repo_invalid_token)

            } catch (e: IOException) {
                _state.value = State.Error(R.string.repo_network_error)

            } catch (e: Exception) {
                _state.value = State.Error(R.string.repo_loading_error)
            }
        }
    }

    sealed interface State {

        object Loading : State

        data class Loaded(
            val repos: List<Repo>
        ) : State

        data class Error(
            @StringRes val error: Int
        ) : State
        object Unauthorized : State
        object Empty : State
    }
}