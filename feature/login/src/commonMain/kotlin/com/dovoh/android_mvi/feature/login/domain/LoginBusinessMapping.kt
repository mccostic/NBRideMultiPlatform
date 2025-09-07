package com.dovoh.android_mvi.feature.login.domain

import com.dovoh.android_mvi.core.common.ApiError
import com.dovoh.android_mvi.core.common.BusinessErrorRegistry

fun registerLoginBusinessMappings() {
    BusinessErrorRegistry.register("LOGIN_INVALID_CREDENTIALS") { api: ApiError ->
        LoginException.InvalidCredentials(api.message)
    }
    BusinessErrorRegistry.register("LOGIN_ACCOUNT_LOCKED") { api ->
        LoginException.AccountLocked(api.message)
    }
    BusinessErrorRegistry.register("LOGIN_EMAIL_NOT_VERIFIED") { api ->
        LoginException.EmailNotVerified(api.message)
    }
    BusinessErrorRegistry.register("LOGIN_TOO_MANY_ATTEMPTS") { api ->
        LoginException.TooManyAttempts(api.message)
    }
    // Optional: catch-all for any future LOGIN_* code
    BusinessErrorRegistry.register("LOGIN_GENERIC") { api ->
        LoginException.Generic(api.code, api.message)
    }
}
