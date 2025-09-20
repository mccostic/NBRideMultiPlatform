package com.dovoh.android_mvi.feature.login.data

import com.dovoh.android_mvi.core.auth.TokenRepository
import com.dovoh.android_mvi.core.auth.model.UserDomainModel
import com.dovoh.android_mvi.core.common.ApiResult
import com.dovoh.android_mvi.core.common.safeApiCall
import com.dovoh.android_mvi.feature.login.domain.AuthApi
import com.dovoh.android_mvi.feature.login.domain.AuthRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO

class AuthRepositoryImpl(
    private val api: AuthApi,
    private val tokens: TokenRepository
) : AuthRepository {
    override suspend fun login(username: String, password: String): ApiResult<UserDomainModel> =
        safeApiCall(Dispatchers.IO) {
            val result = api.login(username, password)
            tokens.save(result.accessToken, result.refreshToken)
            result
        }
}
