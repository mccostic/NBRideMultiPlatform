package com.dovoh.android_mvi.feature.login.data

import com.dovoh.android_mvi.core.auth.model.UserDomainModel
import com.dovoh.android_mvi.core.network.auth.mapper.LoginResponseToDomainMapper
import com.dovoh.android_mvi.core.network.model.LoginRequest
import com.dovoh.android_mvi.core.network.model.LoginResponse
import com.dovoh.android_mvi.feature.login.domain.AuthApi
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.http.ContentType
import io.ktor.http.contentType


class AuthApiImp(private val http: HttpClient, private val base: String,private val mapper:LoginResponseToDomainMapper):AuthApi {
    override suspend fun login(email: String, password: String): UserDomainModel {
        val dto: LoginResponse = http.post("$base/auth/login") {
            contentType(ContentType.Application.Json)
            setBody(LoginRequest(email, password))
        }.body()
        return mapper.map(dto)
    }
}




