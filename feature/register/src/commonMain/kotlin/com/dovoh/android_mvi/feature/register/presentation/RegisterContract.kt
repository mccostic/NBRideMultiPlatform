package com.dovoh.android_mvi.feature.register.presentation

data class RegisterState(
    val name: String = "",
    val email: String = "",
    val password: String = "",
    val loading: Boolean = false,
    val error: String? = null,
    val success: Boolean = false,
    val showErrorDialog: Boolean = false
)

sealed interface RegisterIntent {
    data class NameChanged(val value: String) : RegisterIntent
    data class EmailChanged(val value: String) : RegisterIntent
    data class PasswordChanged(val value: String) : RegisterIntent
    data object Submit : RegisterIntent
    data object Back : RegisterIntent
    data object HideDialog : RegisterIntent
    data class ShowDialog(val message: String?) : RegisterIntent
}

sealed interface RegisterEffect {
    data object NavigateBack : RegisterEffect
    data object Registered : RegisterEffect
}
