package com.rodcibils.sfmobiletest.model

import android.os.Build
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter

/**
 * @param seed Random String of the seed
 * @param expiresAt Date string in ISO format
 */
@Serializable
data class QRCodeSeed(
    val seed: String,
    @SerialName("expires_at") val expiresAt: String,
) {
    /**
     * Formats the ISO-8601 expiration date into a human-readable format.
     * @return Formatted expiration date as a string.
     */
    fun getFormattedExpiration(): String {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            try {
                val zoned = ZonedDateTime.parse(expiresAt)
                zoned.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"))
            } catch (e: Exception) {
                expiresAt
            }
        } else {
            try {
                expiresAt
                    .replace("T", " ")
                    .replace(Regex("\\..*Z$"), "")
            } catch (e: Exception) {
                expiresAt
            }
        }
    }
}
