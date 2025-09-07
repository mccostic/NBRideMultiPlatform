package com.dovoh.android_mvi.feature.login

import app.cash.turbine.ReceiveTurbine
import app.cash.turbine.testIn
import app.cash.turbine.turbineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.test.TestScope
import kotlinx.coroutines.test.runTest

/**
 * Run a VM test collecting state & effect together in the same TestScope.
 */
fun <S, E> runViewModelTest(
    state: Flow<S>,
    effect: Flow<E>,
    block: suspend TestScope.(state: ReceiveTurbine<S>, effect: ReceiveTurbine<E>) -> Unit
) = runTest {
    turbineScope {
        val sTurbine = state.testIn(this)
        val eTurbine = effect.testIn(this)
        block(sTurbine, eTurbine)
        sTurbine.cancel()
        eTurbine.cancel()
    }
}
