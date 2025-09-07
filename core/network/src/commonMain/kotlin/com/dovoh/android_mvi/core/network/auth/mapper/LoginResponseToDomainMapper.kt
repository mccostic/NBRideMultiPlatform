package com.dovoh.android_mvi.core.network.auth.mapper


import com.dovoh.android_mvi.core.auth.model.UserDomainModel
import com.dovoh.android_mvi.core.common.Mapper
import com.dovoh.android_mvi.core.network.model.LoginResponse

class LoginResponseToDomainMapper : Mapper<LoginResponse, UserDomainModel> {
    override fun map(input: LoginResponse): UserDomainModel =
        UserDomainModel(
            accessToken = input.accessToken,
            email = input.email,
            firstName = input.firstName,
            gender = input.lastName,
            id = input.id,
            image = input.image,
            lastName = input.lastName,
            refreshToken = input.refreshToken,
            username = input.username
        )
}