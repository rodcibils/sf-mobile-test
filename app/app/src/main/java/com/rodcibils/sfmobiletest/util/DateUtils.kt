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
     * @return A human-readable countdown string (e.g. "1d 2h 10m 5s") or "Expired" / "Invalid date"
     * @throws Exception if the date is not parseable or its invalid
     */
    fun calculateCountdownText(
        expiresAt: String,
        nowMillis: Long = System.currentTimeMillis(),
        nowInstant: Instant? = null,
    ): String {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val now = nowInstant ?: Instant.now()
            val expiration = ZonedDateTime.parse(expiresAt).toInstant()
            val duration = Duration.between(now, expiration)
            if (duration.isNegative) "Expired" else formatDetailedDuration(duration)
        } else {
            val sdf = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.US)
            sdf.timeZone = TimeZone.getTimeZone("UTC")
            val expirationDate = sdf.parse(expiresAt) ?: return "Invalid date"
            val remainingMillis = expirationDate.time - nowMillis

            if (remainingMillis < 0) {
                "Expired"
            } else {
                formatMillisAsDetailedDuration(remainingMillis)
            }
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
}
