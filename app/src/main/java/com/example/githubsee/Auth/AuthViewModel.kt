package com.example.githubsee.Auth

import android.text.BoringLayout
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.annotation.StringRes
import com.example.githubsee.AppRepository
import com.example.githubsee.KeyValueStorage
import com.example.githubsee.R
import io.ktor.client.plugins.ClientRequestException
import io.ktor.http.HttpStatusCode
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlin.coroutines.cancellation.CancellationException

class AuthViewModel(
    private val repository: AppRepository,
    private val storage: KeyValueStorage
) : ViewModel() {


    val token = MutableStateFlow("")



    fun onTokenChanged(value: String) {
        token.value = value

        if (_state.value is State.InvalidInput) {
            _state.value = State.Idle
        }
    }

    private val _state = MutableStateFlow<State>(State.Idle)
    val state: StateFlow<State> = _state.asStateFlow()
    private val _actions = MutableSharedFlow<Action>()
    val actions: Flow<Action> = _actions.asSharedFlow()

    val Authflag: Boolean

    init {
        val savedToken = storage.authToken

        Authflag = savedToken.isNullOrBlank()

        if (Authflag) {
            token.value = ""
        } else {
            _state.value = State.Authorized
        }
    }



    fun onSignButtonPressed() {
        val enteredToken = token.value.trim()
        if (enteredToken.isBlank()) {
            _state.value = State.InvalidInput(R.string.Enter_token)
            return
        }
        _state.value = State.Loading
        viewModelScope.launch {
            try {
                repository.signIn(enteredToken)
                storage.authToken = enteredToken
                _state.value = State.Idle
                _actions.emit(Action.RouteToMain)




            } catch (e: CancellationException) {
                throw e
            } catch (e: ClientRequestException) {
                _state.value = if (e.response.status == HttpStatusCode.Unauthorized) {
                    State.InvalidInput(R.string.Invalid_token)
                } else {
                    State.InvalidInput(R.string.network_error)
                }
            } catch (e: Exception) {
                _state.value = State.InvalidInput(R.string.network_error)
            }
        }
    }

    sealed interface State {
        object Idle : State
        object Loading : State
        object Authorized : State
        data class InvalidInput(
            @StringRes val reason: Int
        ) : State
    }

    sealed interface Action {
        data class ShowError(
            @StringRes val reason: Int
        ) : Action

        object RouteToMain : Action
    }
}