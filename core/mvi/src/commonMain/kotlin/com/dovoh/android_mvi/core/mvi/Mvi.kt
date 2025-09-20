package com.dovoh.android_mvi.core.mvi

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dovoh.android_mvi.core.common.ApiResult
import com.dovoh.android_mvi.core.common.AppException
import com.dovoh.android_mvi.core.common.BusinessException
import com.dovoh.android_mvi.core.common.ErrorMapper
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.consumeAsFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.flow.update

abstract class MviViewModel<I : Any, S : Any, E : Any>(
    initialState: S
) : ViewModel() {

    private val _state = MutableStateFlow(initialState)
    val state: StateFlow<S> = _state.asStateFlow()

    private val _intent = Channel<I>(Channel.UNLIMITED)
    private val _effect = Channel<E>(Channel.BUFFERED)
    val effect: Flow<E> = _effect.receiveAsFlow()

    // Common effects for generic (non-business) errors
    private val _commonEffect = Channel<CommonEffect>(Channel.BUFFERED)
    val commonEffect: Flow<CommonEffect> = _commonEffect.receiveAsFlow()

    /** Centralized handler for uncaught coroutine exceptions */
    private val exceptionHandler = CoroutineExceptionHandler { _, t ->
        if (t is CancellationException) return@CoroutineExceptionHandler
        val appEx = t as? AppException ?: runCatching { ErrorMapper.fromThrowable(t) }.getOrElse { AppException.Unknown(cause = t) }
        handleFailure(appEx)
    }

    /** Child scope = viewModelScope + our exception handler */
    protected val guardedScope: CoroutineScope =
        CoroutineScope(viewModelScope.coroutineContext + exceptionHandler)

    init {
        // Intents processed in guarded scope so uncaught errors flow to handler
        guardedScope.launch {
            _intent.consumeAsFlow().collect { handleIntent(it) }
        }
    }

    protected abstract suspend fun handleIntent(intent: I)



    protected fun setState(reducer: S.() -> S) {
        _state.update { it.reducer() }
    }


    protected fun postEffect(e: E) {
        _effect.trySend(e)
    }

    protected fun postCommon(e: CommonEffect) {
        _commonEffect.trySend(e)
    }

    fun sendIntent(i: I) { _intent.trySend(i) }

    /** Convenience: launch with the guarded scope */
    protected fun launchGuarded(block: suspend CoroutineScope.() -> Unit) =
        guardedScope.launch { block() }

    /** Helper to run an API call and route failures through the base handler. */
    protected fun <T> launchApi(
        call: suspend () -> ApiResult<T>,
        onSuccess: (T) -> Unit
    ) = guardedScope.launch {
        when (val r = call()) {
            is ApiResult.Success -> onSuccess(r.data)
            is ApiResult.Error   -> handleFailure(r.error)
        }
    }

    /** Base layer handles everything except Business; Business bubbles to child VM. */
    protected open fun handleFailure(e: AppException) {
        when (e) {
            is BusinessException -> onBusinessError(e)

            is AppException.Unauthorized -> launchGuarded {
                postCommon(CommonEffect.Unauthorised)
            }
            is AppException.Forbidden -> launchGuarded {
                postCommon(CommonEffect.Toast("Access denied"))
            }
            is AppException.NotFound -> launchGuarded {
                postCommon(CommonEffect.Toast("Not found"))
            }
            is AppException.Network  -> launchGuarded {
                postCommon(CommonEffect.NetworkIssue)
            }
            is AppException.Server  -> launchGuarded {
                postCommon(CommonEffect.ServerIssue(e.statusCode))
            }
            is AppException.Parsing  -> launchGuarded {
                postCommon(CommonEffect.ParsingIssue)
            }
            is AppException.Unknown      -> launchGuarded {
                postCommon(CommonEffect.UnknownIssue)
            }
        }
    }

    /** Child VMs must define what to do with domain/business errors. */
    protected abstract fun onBusinessError(e: BusinessException)
}
