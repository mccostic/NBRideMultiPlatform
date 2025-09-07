package com.dovoh.android_mvi.core.network


import com.dovoh.android_mvi.core.auth.TokenRepository
import com.dovoh.android_mvi.core.logging.Log
import io.ktor.client.*
import io.ktor.client.engine.*
import io.ktor.client.plugins.DefaultRequest
import io.ktor.client.plugins.auth.*
import io.ktor.client.plugins.auth.providers.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.client.plugins.logging.*
import io.ktor.client.request.accept
import io.ktor.http.ContentType
import io.ktor.http.contentType
import io.ktor.serialization.kotlinx.json.*
import kotlinx.serialization.json.Json


class KtorClientFactory(private val host: String, private val tokens: TokenRepository) {
    fun create(): HttpClient = HttpClient(engine()) {
        expectSuccess = true
        install(DefaultRequest) {
            contentType(ContentType.Application.Json)
            accept(ContentType.Application.Json)
        }
        install(ContentNegotiation) {
            json(
                Json {
                    ignoreUnknownKeys = true   // <-- important (DummyJSON returns many extra fields)
                    isLenient = true
                    encodeDefaults = true
                    explicitNulls = false
                }
            )
        }
        install(Logging) {
            logger = object : Logger { override fun log(message: String) { Log.d(message, "Ktor") } }
            level = LogLevel.ALL
        }
        install(Auth) {
            bearer {
                loadTokens {
                    val access = tokens.access()
                    val refresh = tokens.refresh()
                    access?.let { BearerTokens(it, refresh ?: "") }
                }
                sendWithoutRequest { req -> req.url.host == host }
                refreshTokens { null } // wire refresh later
            }
        }
    }
}

expect fun engine(): HttpClientEngineFactory<*>
