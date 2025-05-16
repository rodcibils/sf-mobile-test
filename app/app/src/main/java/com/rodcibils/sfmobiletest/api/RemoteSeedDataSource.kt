package com.rodcibils.sfmobiletest.api

import io.ktor.client.HttpClient
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.client.statement.HttpResponse
import io.ktor.client.statement.bodyAsText
import io.ktor.http.ContentType
import io.ktor.http.HttpStatusCode
import io.ktor.http.contentType
import kotlinx.serialization.Serializable

class RemoteSeedDataSource(
    /**
     * TODO: replace with DI
     */
    private val client: HttpClient = HttpClientProvider.client,
) {
    @Serializable
    private data class SeedRequest(val seed: String)

    /**
     * @param seed Seed scanned from QR with camera, that we want to know if its a valid seed or not
     * @return true if the seed is valid on backend, false if otherwise.
     * @throws Exception if there is any network error.
     */
    suspend fun isSeedValid(seed: String): Boolean {
        val response: HttpResponse =
            client.post("${BASE_URL}/validate") {
                contentType(ContentType.Application.Json)
                setBody(SeedRequest(seed))
            }

        return when (response.status) {
            HttpStatusCode.OK -> true
            HttpStatusCode.BadRequest -> false
            else -> throw IllegalStateException(
                "Unexpected response: ${response.status.value} - ${response.bodyAsText()}",
            )
        }
    }
}
