package com.rodcibils.sfmobiletest.api

import com.rodcibils.sfmobiletest.model.QRCodeSeed
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.client.statement.HttpResponse
import io.ktor.client.statement.bodyAsText
import io.ktor.http.ContentType
import io.ktor.http.HttpStatusCode
import io.ktor.http.contentType
import kotlinx.serialization.Serializable

/**
 * Handles remote API communication for seed validation and retrieval.
 *
 * @param client Ktor HTTP client used for making network requests.
 */
class RemoteSeedDataSource(
    private val client: HttpClient,
) {
    @Serializable
    private data class SeedRequest(val seed: String)

    /**
     * Validates a QR seed against the backend.
     *
     * @param seed The seed string scanned from a QR code.
     * @return `true` if the seed is valid, `false` if invalid.
     * @throws Exception if there is a network error or an unexpected response.
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

    /**
     * Fetches a fresh [QRCodeSeed] from the backend.
     *
     * @return A [QRCodeSeed] object containing the seed and its expiration.
     * @throws Exception if the network call fails or the response cannot be parsed.
     */
    suspend fun getSeed(): QRCodeSeed {
        return client.get("${BASE_URL}/seed").body()
    }
}
