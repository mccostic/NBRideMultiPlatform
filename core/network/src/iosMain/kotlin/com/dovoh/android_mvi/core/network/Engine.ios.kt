package com.dovoh.android_mvi.core.network

import io.ktor.client.engine.*
import io.ktor.client.engine.darwin.*

actual fun engine(): HttpClientEngineFactory<*> = Darwin
