package com.dovoh.android_mvi.feature.register.domain

import com.dovoh.android_mvi.core.auth.model.UserDomainModel
import com.dovoh.android_mvi.core.common.ApiResult

class RegisterUseCaseImpl(private val repo: RegisterRepository) : RegisterUseCase {
    override suspend operator fun invoke(name: String, email: String, pass: String): ApiResult<UserDomainModel?> =
        repo.register(name, email, pass)
}

fun interface RegisterUseCase {
    suspend operator fun invoke(name: String, email: String, pass: String): ApiResult<UserDomainModel?>
}
