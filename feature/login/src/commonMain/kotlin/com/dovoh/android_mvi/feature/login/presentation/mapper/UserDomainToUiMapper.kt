package com.dovoh.android_mvi.feature.login.presentation.mapper

import com.dovoh.android_mvi.core.auth.model.UserDomainModel
import com.dovoh.android_mvi.core.common.Mapper
import com.dovoh.android_mvi.feature.login.presentation.model.UserUiModel

class UserDomainToUiMapper : Mapper<UserDomainModel, UserUiModel> {
    override fun map(input: UserDomainModel): UserUiModel =
        UserUiModel(
            email = input.email,
            firstName = input.firstName,
            gender = input.lastName,
            id = input.id,
            image = input.image,
            lastName = input.lastName,
            username = input.username
        )
}
