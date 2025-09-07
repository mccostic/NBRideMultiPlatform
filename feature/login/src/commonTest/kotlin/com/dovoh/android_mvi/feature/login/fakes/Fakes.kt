package com.dovoh.android_mvi.feature.login.fakes


import com.dovoh.android_mvi.core.auth.model.UserDomainModel
import com.dovoh.android_mvi.feature.login.domain.LoginException

fun fakeUser(
    id: Int = 7,
    email: String = "alice@example.com",
    username: String = "alice",
    first: String = "Alice",
    last: String = "Liddell"
) = UserDomainModel(
    id = id,
    email = email,
    username = username,
    firstName = first,
    lastName = last,
    gender = "n/a",
    image = null
)
var fakeException: Exception = LoginException.Generic(message = "General error", code = "")