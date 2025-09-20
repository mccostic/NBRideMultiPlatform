package com.dovoh.android_mvi.feature.login.di

import com.dovoh.android_mvi.feature.login.domain.AuthRepository
import com.dovoh.android_mvi.feature.login.domain.LoginUseCase
import com.dovoh.android_mvi.feature.login.fakes.FakeAuthRepository
import com.dovoh.android_mvi.feature.login.fakes.FakeLoginUseCase
import com.dovoh.android_mvi.feature.login.presentation.LoginViewModel
import com.dovoh.android_mvi.feature.login.presentation.mapper.UserDomainToUiMapper
import org.koin.dsl.module

/**
 * The base module used across login tests.
 * Bind real mappers, and bind use case to a fake by default (override per test if needed).
 */

val loginTestModule = module {
    single<AuthRepository> { FakeAuthRepository() }
    single<LoginUseCase> { FakeLoginUseCase() }
    single { UserDomainToUiMapper() }
    single { LoginViewModel(get(), get()) }
}
