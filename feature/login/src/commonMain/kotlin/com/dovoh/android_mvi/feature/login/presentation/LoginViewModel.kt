package com.dovoh.android_mvi.feature.login.presentation

import com.dovoh.android_mvi.core.auth.model.UserDomainModel
import com.dovoh.android_mvi.core.common.BusinessException
import com.dovoh.android_mvi.core.common.Mapper
import com.dovoh.android_mvi.core.logging.Log
import com.dovoh.android_mvi.core.mvi.MviViewModel
import com.dovoh.android_mvi.feature.login.domain.LoginException
import com.dovoh.android_mvi.feature.login.domain.LoginUseCase
import com.dovoh.android_mvi.feature.login.presentation.model.UserUiModel

class LoginViewModel(
    private val login: LoginUseCase,
    private val mapper: Mapper<UserDomainModel, UserUiModel>
) : MviViewModel<LoginIntent, LoginState, LoginEffect>(LoginState()) {

    override suspend fun handleIntent(intent: LoginIntent) = when (intent) {
        is LoginIntent.EmailChanged -> setState { copy(email = intent.value, error = null) }
        is LoginIntent.PasswordChanged -> setState { copy(password = intent.value, error = null) }
        LoginIntent.Submit -> submit()
        LoginIntent.GoToRegister -> postEffect(LoginEffect.NavigateRegister)
        is LoginIntent.ShowDialog -> {
            setState { copy(error = intent.message, showErrorDialog = true) }
        }

        LoginIntent.HideDialog -> {
            setState { copy(error = null, showErrorDialog = false) }
        }
    }

    private fun submit() {
        val s = state.value
        if (s.email.isBlank() || s.password.isBlank()) {
            setState { copy(error = "Email and Password are required.") }
            return
        }

        setState { copy(loading = true, error = null) }

        launchApi(
            call = { login(s.email, s.password) },
            onSuccess = {
                it?.let {
                    mapper.map(it).let {
                        Log.d("TESTING_LOGGED_IN_USR", it.toString())
                    }
                }

                setState { copy(loading = false) }
                postEffect(LoginEffect.NavigateHome)
            }
        ).invokeOnCompletion {
            setState { copy(loading = false) }
        }
    }

    /**
     * Handle domain/business errors that were mapped by BusinessErrorRegistry.
     * Keep UI-specific messaging here; base VM handles generic/network/parsing/etc.
     */

    override fun onBusinessError(e: BusinessException) {
        Log.d("onBusinessError", "Error ${e.message}")
        when (e) {
            is LoginException.InvalidCredentials ->
                setState { copy(loading = false, error = e.message ?: "Invalid credentials") }

            is LoginException.AccountLocked ->
                setState { copy(loading = false, error = e.message ?: "Account locked") }

            is LoginException.EmailNotVerified -> {
                setState { copy(loading = false, error = e.message ?: "Email not verified") }
                // If you have a flow for verifying email:
                // postEffect(LoginEffect.NavigateVerifyEmail)
            }

            is LoginException.TooManyAttempts ->
                setState { copy(loading = false, error = e.message ?: "Too many attempts") }

            else ->
                setState { copy(loading = false, error = e.message ?: "Login failed") }
        }
    }
}
