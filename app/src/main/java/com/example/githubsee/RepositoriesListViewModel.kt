package com.example.githubsee
import kotlinx.coroutines.flow.StateFlow

class RepositoriesListViewModel {
    //val state: StateFlow<State>{


 //   }

    sealed interface State {
        object Loading : State
        data class Loaded(val repos: List<Repo>) : State
        data class Error(val error: String) : State
        object Empty : State
    }

    // TODO:
}