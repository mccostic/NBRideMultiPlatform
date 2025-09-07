package com.dovoh.android_mvi.feature.login.fakes


import com.dovoh.android_mvi.core.auth.model.UserDomainModel
import com.dovoh.android_mvi.core.common.Mapper
import com.dovoh.android_mvi.feature.login.presentation.model.UserUiModel

/**
 * Minimal mapper used by tests.
 */
class TestUserDomainToUiMapper : Mapper<UserDomainModel, UserUiModel> {
    override fun map(input: UserDomainModel): UserUiModel =
        UserUiModel(
            id = input.id,
            email = input.email,
            username = input.username,
            firstName = input.firstName,
            lastName = input.lastName,
            gender = input.gender,
            image = input.image
        )
}
