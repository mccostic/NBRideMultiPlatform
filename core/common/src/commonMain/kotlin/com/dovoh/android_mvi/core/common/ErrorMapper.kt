package com.dovoh.android_mvi.core.common

import io.ktor.client.network.sockets.ConnectTimeoutException
import io.ktor.client.plugins.ClientRequestException
import io.ktor.client.plugins.HttpRequestTimeoutException
import io.ktor.client.plugins.RedirectResponseException
import io.ktor.client.plugins.ResponseException
import io.ktor.client.plugins.ServerResponseException
import io.ktor.http.HttpStatusCode
import io.ktor.util.network.UnresolvedAddressException
import kotlinx.serialization.SerializationException
import kotlin.coroutines.cancellation.CancellationException

object ErrorMapper {

    /**
     * NON-SUSPEND mapper:
     * - Safe to use from places like CoroutineExceptionHandler.
     * - Does NOT read/parse the response body (so no business mapping).
     * - Still buckets into generic AppException types.
     */
    fun fromThrowable(t: Throwable): AppException = when (t) {
        is CancellationException         -> throw t
        is RedirectResponseException     -> mapResponseExceptionShallow(t)
        is ClientRequestException        -> mapResponseExceptionShallow(t)
        is ServerResponseException       -> mapResponseExceptionShallow(t)
        is ResponseException             -> mapResponseExceptionShallow(t)
        is HttpRequestTimeoutException   -> AppException.Network("Request timed out", t)
        is ConnectTimeoutException       -> AppException.Network("Connection timed out", t)
        is UnresolvedAddressException    -> AppException.Network("Unable to resolve host", t)
        is SerializationException        -> AppException.Parsing(cause = t)
        else                             -> AppException.Unknown(cause = t)
    }


    /** Generic mapping without reading body (non-suspending). */
    private fun mapResponseExceptionShallow(e: ResponseException): AppException {
        return when (e.response.status) {
            HttpStatusCode.Unauthorized -> AppException.Unauthorized(e)
            HttpStatusCode.Forbidden    -> AppException.Forbidden(e)
            HttpStatusCode.NotFound     -> AppException.NotFound(e)
            else                        -> AppException.Server(
                statusCode = e.response.status.value,
                apiError   = null,
                cause      = e
            )
        }
    }

}
