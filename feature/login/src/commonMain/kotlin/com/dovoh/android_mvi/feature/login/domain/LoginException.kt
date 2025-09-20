package com.dovoh.android_mvi.feature.login.domain

import com.dovoh.android_mvi.core.common.BusinessException

sealed class LoginException(
    code: String? = null,
    message: String? = null,
    cause: Throwable? = null
) : BusinessException(feature = "login", code = code, message = message, cause = cause) {

    class InvalidCredentials(message: String? = "Invalid email or password", cause: Throwable? = null) :
        LoginException(code = "LOGIN_INVALID_CREDENTIALS", message = message, cause = cause)

    class AccountLocked(message: String? = "Your account is locked", cause: Throwable? = null) :
        LoginException(code = "LOGIN_ACCOUNT_LOCKED", message = message, cause = cause)

    class EmailNotVerified(message: String? = "Please verify your email", cause: Throwable? = null) :
        LoginException(code = "LOGIN_EMAIL_NOT_VERIFIED", message = message, cause = cause)

    class TooManyAttempts(message: String? = "Too many attempts. Try later.", cause: Throwable? = null) :
        LoginException(code = "LOGIN_TOO_MANY_ATTEMPTS", message = message, cause = cause)

    /** Fallback for any other LOGIN_* codes you decide to support later */
    class Generic(code: String?, message: String?, cause: Throwable? = null) :
        LoginException(code = code, message = message, cause = cause)
}
