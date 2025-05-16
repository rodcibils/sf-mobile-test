package com.rodcibils.sfmobiletest.local

import android.content.Context
import androidx.core.content.edit
import androidx.security.crypto.EncryptedSharedPreferences
import androidx.security.crypto.MasterKeys
import com.rodcibils.sfmobiletest.model.QRCodeSeed
import com.rodcibils.sfmobiletest.util.DateUtils
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

/**
 * Handles secure local persistence of a [QRCodeSeed] using encrypted shared preferences.
 *
 * This class allows storing a seed and retrieving it later, automatically checking if it has expired.
 *
 * @param context Application context (required to initialize secure storage).
 */
class LocalSeedDataSource(context: Context) {
    /**
     * TODO: Migrate to Jetpack DataStore + AES encryption for future-proof secure local storage.
     *  EncryptedSharedPreferences was deprecated (yet still supported)
     */
    @Suppress("DEPRECATION")
    private val prefs =
        EncryptedSharedPreferences.create(
            "qr_seed_store",
            MasterKeys.getOrCreate(MasterKeys.AES256_GCM_SPEC),
            context,
            EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
            EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM,
        )

    private val json = Json { ignoreUnknownKeys = true }

    companion object {
        private const val SEED_KEY = "local_qr_seed"
    }

    /**
     * Stores a [QRCodeSeed] instance in encrypted local storage.
     *
     * @param seed The QR code seed to be stored.
     */
    fun saveSeed(seed: QRCodeSeed) {
        val serialized = json.encodeToString(seed)
        prefs.edit { putString(SEED_KEY, serialized) }
    }

    /**
     * Retrieves the stored [QRCodeSeed] from encrypted local storage.
     *
     * If the seed is expired, it is removed and `null` is returned.
     *
     * @return A non-expired [QRCodeSeed], or `null` if expired or not present.
     */
    fun getSeed(): QRCodeSeed? {
        val serialized = prefs.getString(SEED_KEY, null) ?: return null
        return try {
            val seed = json.decodeFromString<QRCodeSeed>(serialized)
            if (DateUtils.isExpired(seed.expiresAt)) {
                prefs.edit { remove(SEED_KEY) }
                null
            } else {
                seed
            }
        } catch (e: Exception) {
            null
        }
    }
}
