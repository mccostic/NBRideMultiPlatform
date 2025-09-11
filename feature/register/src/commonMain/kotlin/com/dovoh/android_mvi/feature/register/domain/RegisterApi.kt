package com.dovoh.android_mvi.feature.register.domain


import com.dovoh.android_mvi.core.auth.model.UserDomainModel

fun interface RegisterApi {
    suspend fun register(name: String,email: String, password: String): UserDomainModel
}
