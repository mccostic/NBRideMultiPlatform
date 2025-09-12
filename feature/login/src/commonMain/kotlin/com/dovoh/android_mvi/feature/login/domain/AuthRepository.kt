package com.dovoh.android_mvi.feature.login.domain

import com.dovoh.android_mvi.core.auth.model.UserDomainModel
import com.dovoh.android_mvi.core.common.ApiResult

fun interface AuthRepository {
    suspend fun login(username: String, password: String): ApiResult<UserDomainModel?>
}
