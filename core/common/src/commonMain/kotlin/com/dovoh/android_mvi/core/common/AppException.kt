package com.dovoh.android_mvi.core.common

import kotlinx.serialization.Serializable

/** Generic app-level exceptions (transport, parsing, etc.) */
sealed class AppException(
    message: String? = null,
    cause: Throwable? = null
) : Exception(message, cause) {

    class Network(message: String? = null, cause: Throwable? = null) : AppException(message, cause)
    class Server(val statusCode: Int, val apiError: ApiError? = null, cause: Throwable? = null)
        : AppException(apiError?.message ?: "Server error ($statusCode)", cause)

    class Unauthorized(cause: Throwable? = null) : AppException("Unauthorized", cause)
    class Forbidden(cause: Throwable? = null) : AppException("Forbidden", cause)
    class NotFound(cause: Throwable? = null) : AppException("Not found", cause)
    class Parsing(message: String? = "Failed to parse response", cause: Throwable? = null)
        : AppException(message, cause)

    /** Fallback when we can’t categorize */
    class Unknown(message: String? = "Something went wrong", cause: Throwable? = null)
        : AppException(message, cause)
}

/**
 * Base for **domain/business** exceptions.
 * Each feature creates its own sealed subclass of BusinessException.
 */
abstract class BusinessException(
    /** Used to route to screen/feature: e.g., "login", "register", "checkout" */
    val feature: String,
    /** Optional backend code that triggered this domain error */
    val code: String? = null,
    message: String? = null,
    cause: Throwable? = null
) : AppException(message, cause)

/** Your backend error payload (adjust to your schema) */
@Serializable
data class ApiError(
    val code: String? = null,       // e.g., "LOGIN_INVALID_CREDENTIALS"
    val message: String = "Unexpected error",
    val details: Map<String, String?>? = null
)
