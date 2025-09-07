package org.example.project.nbride


import com.dovoh.android_mvi.core.logging.initLogging
import com.dovoh.android_mvi.di.appModule
import org.koin.core.context.startKoin

/** Call from platform launchers once. */
fun initAppDI(loggingDebug: Boolean = true) {
    initLogging(loggingDebug)
    startKoin { modules(appModule) }
}