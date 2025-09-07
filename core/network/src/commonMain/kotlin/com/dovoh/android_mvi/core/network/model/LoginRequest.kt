package com.dovoh.android_mvi.core.network.model

@kotlinx.serialization.Serializable
data class LoginRequest(
    val username: String,
    val password: String
)