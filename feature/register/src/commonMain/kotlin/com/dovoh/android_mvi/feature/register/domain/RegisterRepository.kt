package com.dovoh.android_mvi.feature.register.domain

import com.dovoh.android_mvi.core.auth.model.UserDomainModel
import com.dovoh.android_mvi.core.common.ApiResult

fun interface RegisterRepository {
    suspend fun register(name: String, email: String, pass: String): ApiResult<UserDomainModel?>
}
