package com.dovoh.android_mvi.feature.login.di


import com.dovoh.android_mvi.core.auth.model.UserDomainModel
import com.dovoh.android_mvi.core.common.Mapper
import com.dovoh.android_mvi.core.network.auth.mapper.LoginResponseToDomainMapper
import com.dovoh.android_mvi.feature.login.data.AuthApiImp
import com.dovoh.android_mvi.feature.login.domain.AuthApi
import com.dovoh.android_mvi.feature.login.domain.AuthRepository
import com.dovoh.android_mvi.feature.login.domain.LoginUseCase
import com.dovoh.android_mvi.feature.login.fakes.FakeAuthRepository
import com.dovoh.android_mvi.feature.login.fakes.FakeLoginUseCase
import com.dovoh.android_mvi.feature.login.presentation.LoginViewModel
import com.dovoh.android_mvi.feature.login.presentation.mapper.UserDomainToUiMapper
import com.dovoh.android_mvi.feature.login.presentation.model.UserUiModel
import io.ktor.client.HttpClient
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.logging.DEFAULT
import io.ktor.client.plugins.logging.LogLevel
import io.ktor.client.plugins.logging.Logger
import io.ktor.client.plugins.logging.Logging
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.json.Json
import org.koin.core.module.dsl.viewModel
import org.koin.dsl.module

val testModule = module(true) {

    // Api

    single {
        Json {
            prettyPrint = true
            isLenient = true
            ignoreUnknownKeys = true
        }
    }
    single {
        HttpClient {
            install(Logging) {
                level = LogLevel.ALL
                logger = Logger.DEFAULT
            }
            install(ContentNegotiation) {
                json(get())
            }
        }
    }

    // Mappers

    single<LoginUseCase> { FakeLoginUseCase() }

    single { LoginResponseToDomainMapper() }
    single<Mapper<UserDomainModel, UserUiModel>> { UserDomainToUiMapper() }

    single<AuthRepository> { FakeAuthRepository() }
    viewModel { LoginViewModel(get(), get()) }
}