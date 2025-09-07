package com.dovoh.android_mvi.core.auth.model

data class UserDomainModel(
    val accessToken: String?=null,
    val email: String?=null,
    val firstName: String?=null,
    val gender: String?=null,
    val id: Int?=null,
    val image: String?=null,
    val lastName: String?=null,
    val refreshToken: String?=null,
    val username: String?=null
)