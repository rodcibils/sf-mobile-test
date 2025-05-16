package com.rodcibils.sfmobiletest.api

import io.ktor.client.HttpClient
import io.ktor.client.engine.okhttp.OkHttp
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.serialization.kotlinx.json.json

/**
 * TODO: check with ipconfig you local machine ip in order to be able to contact the local API
 *  endpoint with the app.
 */
internal const val BASE_URL = "http://192.168.100.147:3000"

object HttpClientProvider {
    val client =
        HttpClient(OkHttp) {
            install(ContentNegotiation) {
                json()
            }
        }
}
