package com.dovoh.android_mvi.feature.login.di

import com.dovoh.android_mvi.core.auth.model.UserDomainModel
import com.dovoh.android_mvi.core.common.Mapper
import com.dovoh.android_mvi.core.network.auth.mapper.LoginResponseToDomainMapper
import com.dovoh.android_mvi.feature.login.domain.AuthRepository
import com.dovoh.android_mvi.feature.login.domain.LoginUseCase
import com.dovoh.android_mvi.feature.login.fakes.FakeAuthRepository
import com.dovoh.android_mvi.feature.login.fakes.FakeLoginUseCase
import com.dovoh.android_mvi.feature.login.presentation.LoginViewModel
import com.dovoh.android_mvi.feature.login.presentation.mapper.UserDomainToUiMapper
import com.dovoh.android_mvi.feature.login.presentation.model.UserUiModel
import org.koin.dsl.module

val testModule = module {
    // fakes
    single<LoginUseCase> { FakeLoginUseCase() }
    single<AuthRepository> { FakeAuthRepository() }

    // mappers
    single { LoginResponseToDomainMapper() }
    single<Mapper<UserDomainModel, UserUiModel>> { UserDomainToUiMapper() }

    // ViewModel as a factory (avoid koin-android viewModel DSL in commonTest)
    factory { LoginViewModel(get(), get()) }
}