package com.rodcibils.sfmobiletest.util

import android.os.Build
import androidx.annotation.RequiresApi
import java.text.SimpleDateFormat
import java.time.Duration
import java.time.Instant
import java.time.ZonedDateTime
import java.util.Locale
import java.util.TimeZone

/**
 * Utility class for handling date and time formatting
 */
object DateUtils {
    /**
     * Calculates the countdown between the provided reference time and a future expiration date.
     *
     * @param expiresAt ISO 8601 timestamp (e.g. "2025-05-16T12:51:36.774Z")
     * @param nowMillis The current time in milliseconds (used for tests and old API fallback)
     * @param nowInstant The current [Instant] (used for tests on API >= 26)
     * @return A human-readable countdown string (e.g. "1d 2h 10m 5s"), "Expired",
     *         or "Error parsing expiration" if parsing fails.
     */
    fun calculateCountdownText(
        expiresAt: String,
        nowMillis: Long = System.currentTimeMillis(),
        nowInstant: Instant? = null,
    ): String {
        return try {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                val now = nowInstant ?: Instant.now()
                val expiration = ZonedDateTime.parse(expiresAt).toInstant()
                val duration = Duration.between(now, expiration)
                if (duration.isNegative) "Expired" else formatDetailedDuration(duration)
            } else {
                val sdf = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.US)
                sdf.timeZone = TimeZone.getTimeZone("UTC")
                val expirationDate = sdf.parse(expiresAt) ?: return "Error parsing expiration"
                val remainingMillis = expirationDate.time - nowMillis

                if (remainingMillis < 0) {
                    "Expired"
                } else {
                    formatMillisAsDetailedDuration(remainingMillis)
                }
            }
        } catch (e: Exception) {
            /**
             * TODO: This error fallback might be improved, or maybe the string can be
             *  different/more user friendly
             */
            "Error parsing expiration"
        }
    }

    /**
     * Formats a [Duration] into a human-readable string (e.g. "1d 3h 5m 12s").
     */
    @RequiresApi(Build.VERSION_CODES.O)
    private fun formatDetailedDuration(duration: Duration): String {
        var seconds = duration.seconds
        val days = seconds / (24 * 3600)
        seconds %= (24 * 3600)
        val hours = seconds / 3600
        seconds %= 3600
        val minutes = seconds / 60
        seconds %= 60

        return buildString {
            if (days > 0) append("${days}d ")
            if (hours > 0 || days > 0) append("${hours}h ")
            if (minutes > 0 || hours > 0 || days > 0) append("${minutes}m ")
            append("${seconds}s")
        }.trim()
    }

    /**
     * Formats milliseconds into a human-readable string (e.g. "2h 15m 42s").
     */
    private fun formatMillisAsDetailedDuration(millis: Long): String {
        var seconds = millis / 1000
        val days = seconds / (24 * 3600)
        seconds %= (24 * 3600)
        val hours = seconds / 3600
        seconds %= 3600
        val minutes = seconds / 60
        seconds %= 60

        return buildString {
            if (days > 0) append("${days}d ")
            if (hours > 0 || days > 0) append("${hours}h ")
            if (minutes > 0 || hours > 0 || days > 0) append("${minutes}m ")
            append("${seconds}s")
        }.trim()
    }

    /**
     * Parses an ISO 8601 date-time string into a Unix timestamp in milliseconds.
     *
     * This method is compatible with API 24+ and expects the input string to follow
     * the ISO 8601 format with milliseconds and a 'Z' suffix (e.g. "2025-05-16T12:51:36.774Z").
     *
     * The result is in UTC time. If the input is invalid or parsing fails, the method returns 0L.
     *
     * @param iso The ISO 8601 string to parse.
     * @return The corresponding timestamp in milliseconds since epoch (UTC), or 0L if parsing
     * fails.
     */
    private fun parseIsoToMillis(iso: String): Long {
        return try {
            val formatter = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.US)
            formatter.timeZone = TimeZone.getTimeZone("UTC")
            formatter.parse(iso)?.time ?: 0L
        } catch (e: Exception) {
            0L
        }
    }

    /**
     * Calculates how many milliseconds remain until the given ISO 8601 expiration time.
     *
     * @param expiresAt ISO 8601 timestamp (e.g. "2025-05-16T12:51:36.774Z")
     * @param nowMillis Optional reference time in milliseconds (defaults to current system time)
     * @return Milliseconds until expiration (can be negative if already expired)
     */
    fun getMillisUntilExpiration(
        expiresAt: String,
        nowMillis: Long = System.currentTimeMillis(),
    ): Long {
        return try {
            val expirationMillis = parseIsoToMillis(expiresAt)
            expirationMillis - nowMillis
        } catch (e: Exception) {
            -1L // treat parse errors as already expired
        }
    }

    /**
     * Returns true if the given ISO 8601 expiration date is in the past (i.e., expired).
     *
     * @param expiresAt ISO 8601 timestamp (e.g. "2025-05-16T12:51:36.774Z")
     * @param nowMillis Optional reference time in milliseconds (defaults to current system time).
     * @return `true` if the expiration time is in the past, `false` otherwise.
     */
    fun isExpired(
        expiresAt: String,
        nowMillis: Long = System.currentTimeMillis(),
    ): Boolean {
        return try {
            val expirationMillis = parseIsoToMillis(expiresAt)
            expirationMillis < nowMillis
        } catch (e: Exception) {
            false // if the date is invalid, treat it as not expired (or adjust if needed)
        }
    }
}
