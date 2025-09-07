package com.dovoh.android_mvi.feature.login.domain

import com.dovoh.android_mvi.core.auth.model.UserDomainModel

interface AuthApi {
    suspend fun login(email: String, password: String): UserDomainModel
}
