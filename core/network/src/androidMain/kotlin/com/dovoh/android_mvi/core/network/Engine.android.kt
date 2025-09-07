package com.dovoh.android_mvi.core.network

import io.ktor.client.engine.*
import io.ktor.client.engine.okhttp.*

actual fun engine(): HttpClientEngineFactory<*> = OkHttp
