package com.dovoh.android_mvi.core.logging

import io.github.aakira.napier.Antilog
import io.github.aakira.napier.DebugAntilog
import io.github.aakira.napier.LogLevel
import io.github.aakira.napier.Napier

object Log {
    fun d(msg: String, tag: String? = null) = Napier.d(msg,null, tag)
    fun i(msg: String, tag: String? = null) = Napier.i(msg, null, tag)
    fun w(msg: String, tag: String? = null, throwable: Throwable? = null) = Napier.w(msg, throwable, tag)
    fun e(msg: String, tag: String? = null, throwable: Throwable? = null) = Napier.e(msg, throwable, tag)
}
/**
 * Initialize Napier logging once from your platform launcher (e.g. Android/iOS MainActivity).
 *
 * @param enableDebug If true, install DebugAntilog for debug builds. If false, no-op (silent).
 */
fun initLogging(enableDebug: Boolean = true) {
    if (enableDebug) {
        Napier.base(DebugAntilog())
    } else {
        // You could plug in a crash reporting antilog here (e.g., CrashlyticsAntilog)
        Napier.base(CrashlyticsAntiLog())
    }
}

class CrashlyticsAntiLog: Antilog(){
    override fun performLog(
        priority: LogLevel,
        tag: String?,
        throwable: Throwable?,
        message: String?,
    ) {

    }
}