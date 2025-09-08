package com.dovoh.android_mvi.di

import com.dovoh.android_mvi.core.auth.InMemoryTokenStore
import com.dovoh.android_mvi.core.auth.TokenRepository
import com.dovoh.android_mvi.core.auth.TokenRepositoryImpl
import com.dovoh.android_mvi.core.auth.TokenStore
import com.dovoh.android_mvi.core.auth.model.UserDomainModel
import com.dovoh.android_mvi.core.common.Mapper
import com.dovoh.android_mvi.core.network.KtorClientFactory
import com.dovoh.android_mvi.core.network.auth.mapper.LoginResponseToDomainMapper
import com.dovoh.android_mvi.di.core.BuildKonfig
import com.dovoh.android_mvi.feature.login.data.AuthApiImp
import com.dovoh.android_mvi.feature.login.data.AuthRepositoryImpl
import com.dovoh.android_mvi.feature.login.domain.AuthApi
import com.dovoh.android_mvi.feature.login.domain.AuthRepository
import com.dovoh.android_mvi.feature.login.domain.LoginUseCase
import com.dovoh.android_mvi.feature.login.domain.LoginUseCaseImpl
import com.dovoh.android_mvi.feature.login.presentation.LoginViewModel
import com.dovoh.android_mvi.feature.login.presentation.mapper.UserDomainToUiMapper
import com.dovoh.android_mvi.feature.login.presentation.model.UserUiModel
import com.dovoh.android_mvi.feature.register.data.RegisterApiImpl
import com.dovoh.android_mvi.feature.register.data.RegisterRepositoryImpl
import com.dovoh.android_mvi.feature.register.domain.RegisterApi
import com.dovoh.android_mvi.feature.register.domain.RegisterRepository
import com.dovoh.android_mvi.feature.register.domain.RegisterUseCase
import com.dovoh.android_mvi.feature.register.domain.RegisterUseCaseImpl
import com.dovoh.android_mvi.feature.register.presentation.RegisterViewModel
import org.koin.dsl.module


val coreModule = module {
    single<TokenStore> { InMemoryTokenStore() }
    single<TokenRepository>{ TokenRepositoryImpl(get()) }
    single { KtorClientFactory(host =BuildKonfig.BASE_URL, tokens = get()).create() }
}

val loginMappersModule = module {
    single { LoginResponseToDomainMapper() }
    single<Mapper<UserDomainModel, UserUiModel>> { UserDomainToUiMapper() }
}

val loginModule = module {
    single<AuthApi>{ AuthApiImp(get(), base = BuildKonfig.BASE_URL,get()) }
    single<AuthRepository>{ AuthRepositoryImpl(get(), get()) }
    factory<LoginUseCase>{ LoginUseCaseImpl(get()) }
    factory { LoginViewModel(get(),get()) }
}

val registerModule = module {
    single<RegisterApi>{ RegisterApiImpl(get(), base =BuildKonfig.BASE_URL) }
    single<RegisterRepository>{ RegisterRepositoryImpl(get()) }
    factory<RegisterUseCase>{ RegisterUseCaseImpl(get()) }
    factory { RegisterViewModel(get()) }
}

val appModule = module {
    includes(coreModule,loginMappersModule, loginModule, registerModule)
}
