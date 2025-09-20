package com.dovoh.android_mvi.core.mvi

sealed interface CommonEffect {
    data class Toast(val message: String) : CommonEffect
    object NavigateToLogin : CommonEffect
    object NetworkIssue : CommonEffect            // “Check your connection”
    data class ServerIssue(val code: Int) : CommonEffect
    object ParsingIssue : CommonEffect
    object UnknownIssue : CommonEffect
    object Unauthorised : CommonEffect
}
