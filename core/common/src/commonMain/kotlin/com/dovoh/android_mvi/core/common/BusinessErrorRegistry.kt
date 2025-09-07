package com.dovoh.android_mvi.core.common

/**
 * Central registry so features can register their own ApiError.code -> BusinessException mapping.
 */
object BusinessErrorRegistry {
    private val factories = mutableMapOf<String, (ApiError) -> BusinessException>()

    /** Register or replace a mapping for a specific backend error code. */
    fun register(code: String, factory: (ApiError) -> BusinessException) {
        factories[code] = factory
    }

    /** Try to map an ApiError to a BusinessException, or return null if unknown. */
    fun toBusiness(apiError: ApiError?): BusinessException? {
        val c = apiError?.code ?: return null
        val f = factories[c] ?: return null
        return f(apiError)
    }
}
