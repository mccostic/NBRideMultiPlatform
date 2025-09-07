package com.dovoh.android_mvi.core.common


sealed class ApiResult<out T> {
    data class Success<out T>(val data: T) : ApiResult<T>()
    data class Error(val error: AppException) : ApiResult<Nothing>()
}

inline fun <T> ApiResult<T>.onSuccess(block: (T) -> Unit): ApiResult<T> {
    if (this is ApiResult.Success) block(data)
    return this
}
inline fun <T> ApiResult<T>.onError(block: (AppException) -> Unit): ApiResult<T> {
    if (this is ApiResult.Error) block(error)
    return this
}
fun <T> ApiResult<T>.getOrNull(): T? = (this as? ApiResult.Success)?.data
fun <T> ApiResult<T>.exceptionOrNull(): AppException? = (this as? ApiResult.Error)?.error
