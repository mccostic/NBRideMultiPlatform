package com.dovoh.android_mvi.feature.login.fakes

import com.dovoh.android_mvi.core.auth.model.UserDomainModel
import com.dovoh.android_mvi.core.common.ApiResult
import com.dovoh.android_mvi.feature.login.domain.AuthRepository

class FakeAuthRepository : AuthRepository {
    var demoObject: UserDomainModel? = null
    var isSuccessful = true
    var exception: Exception = fakeException

    override suspend fun login(
        username: String,
        password: String,
    ): ApiResult<UserDomainModel?> {
        if (isSuccessful) {
            return ApiResult.Success(demoObject)
        } else {
            throw exception
        }
    }
}
