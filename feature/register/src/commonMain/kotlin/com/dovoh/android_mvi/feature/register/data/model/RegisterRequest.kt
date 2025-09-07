package com.dovoh.android_mvi.feature.register.data.model

import kotlinx.serialization.Serializable

@Serializable data class RegisterRequest(val name: String, val email: String, val password: String)
