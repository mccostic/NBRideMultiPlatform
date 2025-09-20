package com.dovoh.android_mvi.core.mvi


// Keep this platform-agnostic. If you want localized strings later,
// return a key (enum/sealed) here and map to strings in UI.
fun CommonEffect.toUserMessage(): String? = when (this) {
    is CommonEffect.ServerIssue  -> "Invalid credentials!"
    is CommonEffect.NetworkIssue -> "Network Error!"
    is CommonEffect.Unauthorised -> "Authentication Error!"
    else -> "Something went wrong! \nPlease try again!"
}
