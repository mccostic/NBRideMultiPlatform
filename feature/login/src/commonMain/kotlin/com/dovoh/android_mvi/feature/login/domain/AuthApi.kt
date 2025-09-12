package com.dovoh.android_mvi.feature.login.domain

import com.dovoh.android_mvi.core.auth.model.UserDomainModel

fun interface AuthApi {
    suspend fun login(email: String, password: String): UserDomainModel
}
