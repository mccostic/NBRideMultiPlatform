package com.dovoh.android_mvi.feature.login.fakes

import com.dovoh.android_mvi.core.auth.model.UserDomainModel
import com.dovoh.android_mvi.core.common.ApiResult
import com.dovoh.android_mvi.feature.login.domain.LoginUseCase




class FakeLoginUseCase : LoginUseCase {
    var exception: Exception = fakeException
    var demoObject: UserDomainModel? = null
    var isSuccessful = true

    override suspend fun invoke(
        email: String,
        pass: String,
    ): ApiResult<UserDomainModel?> {
        if (isSuccessful) {
            return ApiResult.Success(demoObject)
        } else {
            throw exception
        }
    }
}