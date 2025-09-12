package com.dovoh.android_mvi.feature.register.data

import com.dovoh.android_mvi.core.auth.model.UserDomainModel
import com.dovoh.android_mvi.core.common.ApiResult
import com.dovoh.android_mvi.core.common.safeApiCall
import com.dovoh.android_mvi.feature.register.domain.RegisterApi
import com.dovoh.android_mvi.feature.register.domain.RegisterRepository
import kotlinx.coroutines.*

class RegisterRepositoryImpl(private val api: RegisterApi): RegisterRepository {
    override suspend fun register(name: String, email: String, pass: String): ApiResult<UserDomainModel?> =
        safeApiCall(Dispatchers.IO) {
            api.register(name, email, pass)
        }
}


