package com.dovoh.android_mvi.feature.login.domain

import com.dovoh.android_mvi.core.auth.model.UserDomainModel
import com.dovoh.android_mvi.core.common.ApiResult

interface AuthRepository {
    suspend fun login(username: String, password: String): ApiResult<UserDomainModel?>
}
