package com.dovoh.android_mvi.feature.login.presentation

data class LoginState(
    val email: String = "",
    val password: String = "",
    val loading: Boolean = false,
    val error: String? = null,
    val showErrorDialog: Boolean = false
)

sealed interface LoginIntent {
    data class EmailChanged(val value: String) : LoginIntent
    data class PasswordChanged(val value: String) : LoginIntent
    data class ShowDialog(val message: String) : LoginIntent
    data object Submit : LoginIntent
    data object HideDialog : LoginIntent
    data object GoToRegister : LoginIntent
}

sealed interface LoginEffect {
    data object NavigateHome : LoginEffect
    data object NavigateRegister : LoginEffect
}
