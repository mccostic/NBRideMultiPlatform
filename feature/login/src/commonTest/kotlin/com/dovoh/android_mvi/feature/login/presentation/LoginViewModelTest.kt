package com.dovoh.android_mvi.feature.login.presentation

import com.dovoh.android_mvi.core.auth.model.UserDomainModel
import com.dovoh.android_mvi.core.common.AppException
import com.dovoh.android_mvi.core.mvi.CommonEffect
import com.dovoh.android_mvi.feature.login.di.loginTestModule
import com.dovoh.android_mvi.feature.login.domain.LoginUseCase
import com.dovoh.android_mvi.feature.login.fakes.FakeLoginUseCase
import com.dovoh.android_mvi.feature.login.fakes.fakeException
import com.dovoh.android_mvi.feature.login.fakes.fakeUser
import com.dovoh.android_mvi.feature.login.testing.BaseViewModelTest
import com.dovoh.android_mvi.feature.login.testing.injectAs
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runCurrent
import kotlinx.coroutines.test.runTest
import org.koin.test.KoinTest
import org.koin.test.inject
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertTrue

@OptIn(ExperimentalCoroutinesApi::class)
class LoginViewModelTest : BaseViewModelTest() {

    override val overrideModule = loginTestModule

    private val vm by inject<LoginViewModel>()
    private val fakeUseCase by injectAs<LoginUseCase, FakeLoginUseCase>()



    @Test
    fun `success NavigateHome effect`() = runTest {
        fakeUseCase.demoObject = fakeUser()
        fakeUseCase.isSuccessful = true

        vm.sendIntent(LoginIntent.EmailChanged("user@x.com"))
        vm.sendIntent(LoginIntent.PasswordChanged("pass"))
        vm.sendIntent(LoginIntent.Submit)
        runCurrent()
        assertEquals(LoginEffect.NavigateHome, vm.effect.first())
        val final = vm.state.value
        assertFalse(final.loading)
        assertEquals(null, final.error)
    }

    @Test
    fun `blank credentials validation error`() = runTest {
        vm.sendIntent(LoginIntent.Submit)
        runCurrent()

        val s = vm.state.value
        assertFalse(s.loading)
        assertTrue(s.error?.contains("required", ignoreCase = true) == true)
    }


    @Test
    fun `HTTP 400 common effect`() = runTest {
        fakeUseCase.isSuccessful = false
        fakeUseCase.demoObject = null
        fakeUseCase.exception = AppException.Server(400)

        vm.sendIntent(LoginIntent.EmailChanged("bad"))
        vm.sendIntent(LoginIntent.PasswordChanged("creds"))
        vm.sendIntent(LoginIntent.Submit)
        runCurrent()
        val ce = vm.commonEffect.first()
        assertTrue(ce is CommonEffect.ServerIssue && ce.code == 400)
    }

    @Test
    fun `Business error stays in state`() = runTest {
        fakeUseCase.isSuccessful = false
        fakeUseCase.exception = fakeException

        vm.sendIntent(LoginIntent.EmailChanged("x"))
        vm.sendIntent(LoginIntent.PasswordChanged("y"))
        vm.sendIntent(LoginIntent.Submit)
        runCurrent()

        val s = vm.state.value
        assertFalse(s.loading)
        assertEquals(fakeException.message, s.error)
    }
}
