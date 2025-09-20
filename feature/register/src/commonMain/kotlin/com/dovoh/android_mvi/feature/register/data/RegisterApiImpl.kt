package com.dovoh.android_mvi.feature.register.data

import com.dovoh.android_mvi.core.auth.model.UserDomainModel
import com.dovoh.android_mvi.feature.register.data.model.RegisterRequest
import com.dovoh.android_mvi.feature.register.domain.RegisterApi
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.post
import io.ktor.client.request.setBody

class RegisterApiImpl(private val http: HttpClient, private val base: String) : RegisterApi {
    override suspend fun register(name: String, email: String, password: String): UserDomainModel =
        http.post("$base/auth/register") { setBody(RegisterRequest(name, email, password)) }.body()
}
