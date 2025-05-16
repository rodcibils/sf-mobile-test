package com.rodcibils.sfmobiletest.util

import android.os.Build
import org.junit.Test
import java.time.Instant
import java.time.temporal.ChronoUnit
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertTrue

class DateUtilsTest {
    @Test
    fun `calculateCountdownText returns formatted countdown on API 26+`() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val now = Instant.parse("2025-01-01T00:00:00.000Z")
            val expiresAt = "2025-01-01T00:00:05.000Z"

            val result = DateUtils.calculateCountdownText(expiresAt, nowInstant = now)
            assertEquals("5s", result)
        }
    }

    @Test
    fun `calculateCountdownText returns expired on API 26+`() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val now = Instant.parse("2025-01-01T00:00:05.000Z")
            val expiresAt = "2025-01-01T00:00:00.000Z"

            val result = DateUtils.calculateCountdownText(expiresAt, nowInstant = now)
            assertEquals("Expired", result)
        }
    }

    @Test
    fun `calculateCountdownText returns formatted value on legacy API`() {
        val nowMillis = 1735689600000L // "2025-01-01T00:00:00.000Z"
        val expiresAt = "2025-01-01T00:00:05.000Z"

        val result = DateUtils.calculateCountdownText(expiresAt, nowMillis = nowMillis)
        assertEquals("5s", result)
    }

    @Test
    fun `calculateCountdownText returns expired on legacy API`() {
        val nowMillis = 1735689605000L // 5s after expiration
        val expiresAt = "2025-01-01T00:00:00.000Z"

        val result = DateUtils.calculateCountdownText(expiresAt, nowMillis = nowMillis)
        assertEquals("Expired", result)
    }

    @Test
    fun `calculateCountdownText handles invalid date format`() {
        val result = DateUtils.calculateCountdownText("invalid-date")
        assertEquals("Error parsing expiration", result)
    }

    @Test
    fun `getMillisUntilExpiration returns positive value for future time`() {
        val now = System.currentTimeMillis()
        val future = Instant.ofEpochMilli(now + 10_000).toString() // exactly 10s later

        val result = DateUtils.getMillisUntilExpiration(future, now)

        assertTrue(result in 9990L..10010L) // allow very tight margin
    }

    @Test
    fun `getMillisUntilExpiration returns negative value for past time`() {
        val now = System.currentTimeMillis()
        val past = Instant.ofEpochMilli(now - 10_000).toString() // exactly 10 seconds before now

        val result = DateUtils.getMillisUntilExpiration(past, now)
        assertTrue(result < 0)
    }

    @Test
    fun `getMillisUntilExpiration handles invalid input`() {
        val result = DateUtils.getMillisUntilExpiration("invalid-date", nowMillis = 1000L)
        assertEquals(-1L, result)
    }

    @Test
    fun `isExpired returns true for past date`() {
        val now = System.currentTimeMillis()
        val past = Instant.ofEpochMilli(now).minus(5, ChronoUnit.SECONDS).toString()

        assertTrue(DateUtils.isExpired(past, now))
    }

    @Test
    fun `isExpired returns false for future date`() {
        val now = System.currentTimeMillis()
        val future = Instant.ofEpochMilli(now).plus(5, ChronoUnit.SECONDS).toString()

        assertFalse(DateUtils.isExpired(future, now))
    }

    @Test
    fun `isExpired returns false for invalid date`() {
        assertFalse(DateUtils.isExpired("invalid-date"))
    }
}
