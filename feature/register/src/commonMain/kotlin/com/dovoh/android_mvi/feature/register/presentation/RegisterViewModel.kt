package com.dovoh.android_mvi.feature.register.presentation


import com.dovoh.android_mvi.core.common.ApiResult
import com.dovoh.android_mvi.core.common.BusinessException
import com.dovoh.android_mvi.core.mvi.MviViewModel
import com.dovoh.android_mvi.feature.register.domain.RegisterUseCase

class RegisterViewModel(
    private val register: RegisterUseCase
) : MviViewModel<RegisterIntent, RegisterState, RegisterEffect>(RegisterState()) {

    override suspend fun handleIntent(intent: RegisterIntent) = when (intent) {
        is RegisterIntent.NameChanged -> setState { copy(name = intent.value, error = null) }
        is RegisterIntent.EmailChanged -> setState { copy(email = intent.value, error = null) }
        is RegisterIntent.PasswordChanged -> setState { copy(password = intent.value, error = null) }
        RegisterIntent.Submit -> submit()
        RegisterIntent.Back -> postEffect(RegisterEffect.NavigateBack)
        RegisterIntent.HideDialog -> {
            setState { copy(error = null, showErrorDialog = false) }
        }
        is RegisterIntent.ShowDialog -> {
            setState { copy(error = intent.message, showErrorDialog = true) }
        }
    }

    override fun onBusinessError(e: BusinessException) {

    }

    private suspend fun submit() {
        val s = state.value
        if (s.name.isBlank() || s.email.isBlank() || s.password.isBlank()) {
            setState { copy(error = "All fields are required.") }
            return
        }
        setState { copy(loading = true, error = null) }
        when (val res = register(s.name, s.email, s.password)) {
            is ApiResult.Success -> { setState { copy(loading = false, success = true) }; postEffect(
                RegisterEffect.Registered) }
            is ApiResult.Error ->  setState { copy(loading = false, error = res.error.message ?: "Registration failed", showErrorDialog = true) }
        }
    }
}
