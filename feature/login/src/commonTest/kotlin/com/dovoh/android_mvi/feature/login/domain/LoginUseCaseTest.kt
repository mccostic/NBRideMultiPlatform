package com.dovoh.android_mvi.feature.login.domain


import com.dovoh.android_mvi.core.common.ApiResult
import com.dovoh.android_mvi.feature.login.fakes.FakeAuthRepository
import com.dovoh.android_mvi.feature.login.fakes.fakeUser
import com.dovoh.android_mvi.feature.login.testing.BaseKoinTest
import com.dovoh.android_mvi.feature.login.testing.injectAs
import kotlinx.coroutines.test.runTest
import org.koin.core.module.Module
import org.koin.dsl.module
import org.koin.test.inject
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

/*class LoginUseCaseTest : BaseKoinTest() {

    override val testModule: Module = module {
        // Bind the real use case impl under the LoginUseCase interface
        single<LoginUseCase> { LoginUseCaseImpl(get()) }
        // Repo will be overridden per this test
    }

    override val overrideModule: Module = module {
        single<AuthRepository> { FakeAuthRepository() }
    }

    private val useCase by inject<LoginUseCase>()
    private val repo by injectAs<AuthRepository, FakeAuthRepository>()

    @Test
    fun `invoke forwards to repository and returns success`() = runTest {
        val expected = fakeUser()
        repo.next = ApiResult.Success(expected)

        val result = useCase("me@x.com", "pass")
        assertTrue(result is ApiResult.Success)
        assertEquals(expected, result.data)
        assertEquals(1, repo.calls)
        assertEquals("me@x.com", repo.lastEmail)
    }
}*/
