package com.dovoh.android_mvi.core.common


import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext

suspend inline fun <T> safeApiCall(
    dispatcher: CoroutineDispatcher,
    crossinline block: suspend () -> T
): ApiResult<T> = withContext(dispatcher) {
    try {
        ApiResult.Success(block())
    } catch (t: Throwable) {
        val mapped: AppException = ErrorMapper.fromThrowable(t)
        ApiResult.Error(mapped)
    }
}

