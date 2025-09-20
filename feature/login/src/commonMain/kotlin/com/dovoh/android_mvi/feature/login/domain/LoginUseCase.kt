package com.dovoh.android_mvi.feature.login.domain

import com.dovoh.android_mvi.core.auth.model.UserDomainModel
import com.dovoh.android_mvi.core.common.ApiResult

class LoginUseCaseImpl(
    private val repo: AuthRepository
) : LoginUseCase {
    override suspend operator fun invoke(email: String, pass: String): ApiResult<UserDomainModel?> =
        repo.login(email, pass)
}

fun interface LoginUseCase {
    suspend operator fun invoke(email: String, pass: String): ApiResult<UserDomainModel?>
}
